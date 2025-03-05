package nnk.springboot.configuration;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class CustomAuthentificationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        log.info("Connexion réussie pour : {}", authentication.getName());

        Set<String> roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        log.info("Rôles de l'utilisateur : {}", roles);

        String redirectURL = "/home";

        if (roles.contains("ROLE_ADMIN")) {
            redirectURL = "/admin/home";
        } else if (roles.contains("ROLE_USER")) {
            redirectURL = "/bidList/list";
        }

        log.info("✅ CustomAuthentificationSuccessHandler appelé !");
        System.out.println("✅ CustomAuthentificationSuccessHandler appelé !");

        response.sendRedirect(request.getContextPath() + redirectURL);
    }
}
