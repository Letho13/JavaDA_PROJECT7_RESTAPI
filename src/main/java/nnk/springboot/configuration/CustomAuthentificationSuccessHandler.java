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

    /**
     * Cette méthode est appelée après une connexion réussie d'un utilisateur.
     * Elle récupère les rôles de l'utilisateur authentifié et redirige l'utilisateur vers la page appropriée
     * en fonction de ses rôles.
     *
     * @param request La requête HTTP qui a initié la connexion.
     * @param response La réponse HTTP qui sera envoyée à l'utilisateur.
     * @param authentication L'objet {@link Authentication} contenant les détails de l'utilisateur authentifié,
     *                       y compris ses rôles.
     * @throws IOException Si une erreur survient lors de l'envoi de la réponse HTTP (par exemple, redirection échouée).
     * @throws ServletException Si une erreur de servlet se produit lors du traitement de la requête ou de la réponse.
     */

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
