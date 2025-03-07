package nnk.springboot.configuration;

import nnk.springboot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Slf4j
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    /**
     * Configure et fournit un gestionnaire d'authentification {@link AuthenticationManager}.
     *
     * Cette méthode crée un {@link ProviderManager} avec un {@link DaoAuthenticationProvider}
     * qui utilise le service utilisateur et l'encodeur de mot de passe fournis pour l'authentification.
     *
     * @param userService Le service de gestion des utilisateurs, utilisé pour charger les détails des utilisateurs.
     *@param passwordEncoder L'encodeur de mots de passe, utilisé pour hacher et vérifier les mots de passe des utilisateurs.
     * @return Un {@link AuthenticationManager} configuré pour gérer l'authentification des utilisateurs.
     * @throws Exception Si une erreur survient lors de la création du gestionnaire d'authentification.
     */

    @Bean
    public AuthenticationManager authenticationManager(UserService userService, PasswordEncoder passwordEncoder) throws Exception {
        return new ProviderManager(List.of(new DaoAuthenticationProvider() {{
            setUserDetailsService(userService);
            setPasswordEncoder(passwordEncoder);
        }}));
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * Configure la chaîne de filtres de sécurité pour gérer l'accès aux pages de l'application.
     * <p>
     * Cette méthode définit les règles d'autorisation des requêtes HTTP, la configuration de la page
     * de connexion, la gestion de la déconnexion et le traitement des erreurs d'accès.
     *
     * @param http L'objet {@link HttpSecurity} utilisé pour configurer la sécurité HTTP.
     * @return Un {@link SecurityFilterChain} configuré avec les règles de sécurité définies.
     * @throws Exception Si une erreur survient lors de la configuration de la sécurité.
     */

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("Chargement de SecurityConfig...");
        System.out.println("Chargement de SecurityConfig...");
        return http
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/login", "/css/**", "/js/**", "/images/**")
                            .permitAll();
                    auth.requestMatchers("/admin/home","/user/**")
                            .hasRole("ADMIN");
                    auth.anyRequest()
                            .hasAnyRole("USER", "ADMIN");
                })

                .formLogin(form -> {
                    form.loginPage("/login")
                            .usernameParameter("username")
                            .passwordParameter("password")
                            .successHandler(new CustomAuthentificationSuccessHandler())
                            .failureUrl("/login?error")
                            .permitAll();
                })

                .logout(logout -> {
                    logout.logoutUrl("/logout");
                    logout.logoutSuccessUrl("/login?logout");
                    logout.invalidateHttpSession(true);
                    logout.deleteCookies("JSESSIONID");
                })
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> {
                    httpSecurityExceptionHandlingConfigurer.accessDeniedPage("/accessDenied");

                })
                .build();
    }


}
