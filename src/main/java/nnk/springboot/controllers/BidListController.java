package nnk.springboot.controllers;

import nnk.springboot.domain.BidList;
import nnk.springboot.domain.User;
import nnk.springboot.repositories.UserRepository;
import nnk.springboot.service.BidListService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.*;

@RequiredArgsConstructor
@Controller
public class BidListController {

    private final BidListService bidListService;
    private final UserRepository userRepository;

    // Affiche la liste des bid avec les informations de l'utilisateur connecté et du Role
    @RequestMapping("/bidList/list")
    public String home(Model model) {
        List<BidList> allBildList = bidListService.getAllBidList();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User loggedInUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur " + username + " introuvable avec l'username"));

        String role = loggedInUser.getRole();
        boolean isAdmin = role.equals("ADMIN");

        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("username", loggedInUser.getUsername());
        model.addAttribute("bidLists", allBildList);

        return "bidList/list";
    }

    // Affiche le formulaire d'ajout d'un bid
    @GetMapping("/bidList/add")
    public String addBidForm(BidList bid, Model model) {
        model.addAttribute("bidList", bid);
        return "bidList/add";
    }

    // Valide et enregistre un bid
    @PostMapping("/bidList/validate")
    public String validate(@Valid BidList bid, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "bidList/add";
        }
        bidListService.addBid(bid);

        model.addAttribute("bidLists", bid);
        redirectAttributes.addFlashAttribute("successMessage", "Bid ajouté avec succès !");
        return "redirect:/bidList/list";
    }

    // Affiche le formulaire de mise à jour d'un bid récupéré par son id
    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        BidList bid = bidListService.getBidById(id);
        model.addAttribute("bidList", bid);
        return "bidList/update";
    }

    // Met à jour un bid existant
    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid BidList bidList,
                            BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "bidList/update";
        }
        bidListService.updateBid(id, bidList);
        return "redirect:/bidList/list";
    }

    // Supprime une enchère par son id
    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            bidListService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Bid supprimé avec succès !");
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur : Bid introuvable !");
        }
        return "redirect:/bidList/list";
    }
}
