package nnk.springboot.controllers;

import nnk.springboot.domain.Trade;
import nnk.springboot.domain.User;
import nnk.springboot.repositories.UserRepository;
import nnk.springboot.service.TradeService;
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
public class TradeController {

    private final TradeService tradeService;
    private final UserRepository userRepository;


    @RequestMapping("/trade/list")
    public String home(Model model) {
        List<Trade> getAllTradeList = tradeService.getAllTrade();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User loggedInUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur  " + username + "introuvable avec l'username"));

        String role = loggedInUser.getRole();
        boolean isAdmin = role.equals("ADMIN");

        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("username", loggedInUser.getUsername());
        model.addAttribute("trades", getAllTradeList);
        return "trade/list";
    }

    @GetMapping("/trade/add")
    public String addTrade(Trade trade, Model model) {
        model.addAttribute("trade", trade);
        return "trade/add";
    }

    @PostMapping("/trade/validate")
    public String validate(@Valid Trade trade, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "trade/add";
        }
        tradeService.addTrade(trade);
        redirectAttributes.addFlashAttribute("successMessage", "Trade ajouté avec succès !");
        model.addAttribute("trades", trade);
        return "redirect:/trade/list";
    }

    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {

        Trade trade = tradeService.getTradeById(id);
        model.addAttribute("trade", trade);
        return "trade/update";
    }

    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id, @Valid Trade trade,
                              BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "trade/update";
        }

        tradeService.updateTrade(id, trade);
        return "redirect:/trade/list";
    }

    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            tradeService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Trade supprimé avec succès !");
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur : Trade introuvable !");
        }

        return "redirect:/trade/list";
    }
}
