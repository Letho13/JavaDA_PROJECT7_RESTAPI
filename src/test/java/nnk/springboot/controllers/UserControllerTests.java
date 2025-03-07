package nnk.springboot.controllers;

import nnk.springboot.domain.User;
import nnk.springboot.repositories.UserRepository;
import nnk.springboot.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTests {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private UserController userController;

    @Test
    void testHome() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");

        User user = new User();
        user.setUsername("testUser");
        user.setRole("ADMIN");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        List<User> users = List.of(new User());
        when(userService.getAllUsers()).thenReturn(users);

        String viewName = userController.home(model);

        verify(model).addAttribute("isAdmin", true);
        verify(model).addAttribute("username", "testUser");
        verify(model).addAttribute("users", users);
    }

    @Test
    void testAddUserForm() {
        User user = new User();
        String viewName = userController.addUser(user, model);
        verify(model).addAttribute("user", user);
    }

    @Test
    void testValidateUser_Success() {
        User user = new User();
        when(bindingResult.hasErrors()).thenReturn(false);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");

        String viewName = userController.validate(user, bindingResult, model);

        verify(userService).addUser(user);
        verify(model).addAttribute("users", user);
    }

    @Test
    void testValidateUser_Failure() {
        User user = new User();
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = userController.validate(user, bindingResult, model);
        verifyNoInteractions(userService);
    }

    @Test
    void testShowUpdateForm() {
        int id = 1;
        User user = new User();
        when(userService.getUserById(id)).thenReturn(user);

        String viewName = userController.showUpdateForm(id, model);
        verify(model).addAttribute("user", user);
    }

    @Test
    void testUpdateUser_Success() {
        int id = 1;
        User user = new User();
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.getUserById(id)).thenReturn(user);

        String viewName = userController.updateUser(id, user, bindingResult, model);
        verify(userService).updateUser(eq(id), any(User.class));
    }

    @Test
    void testUpdateUser_Failure() {
        int id = 1;
        User user = new User();
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = userController.updateUser(id, user, bindingResult, model);
        verifyNoInteractions(userService);
    }

    @Test
    void testDeleteUser_Success() {
        int id = 1;
        String viewName = userController.deleteUser(id, redirectAttributes);
        verify(userService).deleteById(id);
        verify(redirectAttributes).addFlashAttribute("successMessage", "User supprimé avec succès !");
    }

    @Test
    void testDeleteUser_Failure() {
        int id = 1;
        doThrow(new NoSuchElementException()).when(userService).deleteById(id);

        String viewName = userController.deleteUser(id, redirectAttributes);
        verify(redirectAttributes).addFlashAttribute("errorMessage", "Erreur : User introuvable !");
    }
}
