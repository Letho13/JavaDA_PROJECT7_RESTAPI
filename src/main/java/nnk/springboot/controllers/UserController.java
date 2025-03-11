package nnk.springboot.controllers;

import nnk.springboot.domain.User;
import nnk.springboot.repositories.UserRepository;
import nnk.springboot.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    // Affiche la liste des utilisateurs avec les informations sur l'utilisateur connecté
    @RequestMapping("/user/list")
    public String home(Model model) {
        List<User> getAllUser = userService.getAllUsers();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User loggedInUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur " + username + " introuvable avec l'username"));

        String role = loggedInUser.getRole();
        boolean isAdmin = role.equals("ADMIN");

        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("username", loggedInUser.getUsername());
        model.addAttribute("users", getAllUser);
        return "user/list";
    }

    // Affiche le formulaire d'ajout d'un utilisateur
    @GetMapping("/user/add")
    public String addUser(User user, Model model) {
        model.addAttribute("user", user);
        return "user/add";
    }

    // Valide et enregistre un nouvel utilisateur avec encodage du mot de passe
    @PostMapping("/user/validate")
    public String validate(@Valid User user, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "user/add";
        }

        // Encoder le mot de passe avant l'enregistrement
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.addUser(user);

        redirectAttributes.addFlashAttribute("successMessage", "Utilisateur ajouté avec succès !");
        return "redirect:/user/list";
    }

    // Affiche le formulaire de mise à jour d'un utilisateur par id
    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        User user = userService.getUserById(id);
        user.setPassword(""); // Vider le mot de passe pour ne pas l'afficher
        model.addAttribute("user", user);
        return "user/update";
    }

    // Met à jour un utilisateur existant, en encodant le mot de passe s'il est modifié
    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid User user,
                             BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "user/update";
        }

        User existingUser = userService.getUserById(id);

        // Mise à jour du mot de passe uniquement s'il a été renseigné
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        // Mise à jour des autres informations demandées
        existingUser.setFullname(user.getFullname());
        existingUser.setUsername(user.getUsername());
        existingUser.setRole(user.getRole());

        userService.updateUser(id, existingUser);

        redirectAttributes.addFlashAttribute("successMessage", "Utilisateur mis à jour avec succès !");
        return "redirect:/user/list";
    }

    // Supprime un utilisateur par id
    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Utilisateur supprimé avec succès !");
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur : Utilisateur introuvable !");
        }
        return "redirect:/user/list";
    }
}
