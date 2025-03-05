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

    // TODO: Inject RuleName service

    @RequestMapping("/ruleName/list")
    public String home(Model model) {
        List<RuleName> getAllRule = ruleNameService.getAllRuleName();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User loggedInUser= userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur  " + username + "introuvable avec l'username"));

        model.addAttribute("username", loggedInUser.getUsername());
        model.addAttribute("ruleNames", getAllRule);
        // TODO: find all RuleName, add to model
        return "ruleName/list";
    }

    @GetMapping("/ruleName/add")
    public String addRuleForm(RuleName ruleName, Model model) {
        model.addAttribute("ruleName",ruleName);
        return "ruleName/add";
    }

    @PostMapping("/ruleName/validate")
    public String validate(@Valid RuleName ruleName, BindingResult result, Model model, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "ruleName/add";
        }

        ruleNameService.addRuleName(ruleName);
        model.addAttribute("ruleNames", ruleName);
        redirectAttributes.addFlashAttribute("successMessage", "RuleName ajouté avec succès !");
        // TODO: check data valid and save to db, after saving return RuleName list
        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {

        RuleName ruleName = ruleNameService.getByIdRuleName(id);
        model.addAttribute("ruleName", ruleName);
        // TODO: get RuleName by Id and to model then show to the form
        return "ruleName/update";
    }

    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id, @Valid RuleName ruleName,
                                 BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "ruleName/update";
        }

        ruleNameService.updateRuleName(id, ruleName);
        // TODO: check required fields, if valid call service to update RuleName and return RuleName list
        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {

        try {
            ruleNameService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "RuleName supprimé avec succès !");
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur : RuleName introuvable !");
        }

        // TODO: Find RuleName by Id and delete the RuleName, return to Rule list
        return "redirect:/ruleName/list";
    }
}
