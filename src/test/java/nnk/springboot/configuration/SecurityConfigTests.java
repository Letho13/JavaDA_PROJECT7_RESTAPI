package nnk.springboot.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTests {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Test
    void testPasswordIsEncoded() {

        String rawPassword = "Test123@";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        assertNotEquals(rawPassword, encodedPassword, "Le mot de passe encodé ne doit pas être égal au mot de passe brut");

        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword), "Le mot de passe encodé doit correspondre au mot de passe brut");
    }

    @Test
    void testPublicPagesAreAccessible() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
            }

    @Test
    void testAdminPageWithoutAuthShouldRedirect() throws Exception {
        mockMvc.perform(get("/admin/home"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testUserCannotAccessAdminPage() throws Exception {
        mockMvc.perform(get("/admin/home"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testAdminCanAccessAdminPage() throws Exception {
        mockMvc.perform(get("/admin/home"))
                .andExpect(status().isOk());
    }

    @Test
    void testLoginFailureRedirectsToErrorPage() throws Exception {
        mockMvc.perform(get("/login?error"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testLogoutRedirectsToLoginPage() throws Exception {
        mockMvc.perform(get("/logout"))
                .andExpect(status().is3xxRedirection());
    }


}






