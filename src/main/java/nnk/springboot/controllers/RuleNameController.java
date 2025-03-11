package nnk.springboot.controllers;

import nnk.springboot.domain.RuleName;
import nnk.springboot.domain.User;
import nnk.springboot.repositories.UserRepository;
import nnk.springboot.service.RuleNameService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class RuleNameController {

    private final RuleNameService ruleNameService;
    private final UserRepository userRepository;

    // Affiche la liste des RuleName avec les informations de l'utilisateur connecté et du Role
    @RequestMapping("/ruleName/list")
    public String home(Model model) {
        List<RuleName> getAllRule = ruleNameService.getAllRuleName();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User loggedInUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur " + username + " introuvable avec l'username"));

        String role = loggedInUser.getRole();
        boolean isAdmin = role.equals("ADMIN");

        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("username", loggedInUser.getUsername());
        model.addAttribute("ruleNames", getAllRule);
        return "ruleName/list";
    }

    // Affiche le formulaire d'ajout d'un RuleName
    @GetMapping("/ruleName/add")
    public String addRuleForm(RuleName ruleName, Model model) {
        model.addAttribute("ruleName", ruleName);
        return "ruleName/add";
    }

    // Valide et enregistre un nouveau RuleName
    @PostMapping("/ruleName/validate")
    public String validate(@Valid RuleName ruleName, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "ruleName/add";
        }
        ruleNameService.addRuleName(ruleName);
        model.addAttribute("ruleNames", ruleName);
        redirectAttributes.addFlashAttribute("successMessage", "RuleName ajouté avec succès !");
        return "redirect:/ruleName/list";
    }

    // Affiche le formulaire de mise à jour d'un RuleName récupéré par son id
    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        RuleName ruleName = ruleNameService.getByIdRuleName(id);
        model.addAttribute("ruleName", ruleName);
        return "ruleName/update";
    }

    // Met à jour un RuleName existant
    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id, @Valid RuleName ruleName,
                                 BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "ruleName/update";
        }
        ruleNameService.updateRuleName(id, ruleName);
        return "redirect:/ruleName/list";
    }

    // Supprime un RuleName par son id
    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            ruleNameService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "RuleName supprimé avec succès !");
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur : RuleName introuvable !");
        }
        return "redirect:/ruleName/list";
    }
}
