package nnk.springboot.controllers;

import nnk.springboot.domain.Rating;
import nnk.springboot.domain.User;
import nnk.springboot.repositories.UserRepository;
import nnk.springboot.service.RatingService;
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
public class RatingController {

    private final RatingService ratingService;
    private final UserRepository userRepository;

    // TODO: Inject Rating service

    @RequestMapping("/rating/list")
    public String home(Model model) {
        List<Rating> allRating = ratingService.getAllRating();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User loggedInUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur  " + username + "introuvable avec l'username"));

        model.addAttribute("username", loggedInUser.getUsername());

        model.addAttribute("ratings", allRating);
        // TODO: find all Rating, add to model
        return "rating/list";
    }

    @GetMapping("/rating/add")
    public String addRatingForm(Rating rating, Model model) {
        model.addAttribute("rating", rating);
        return "rating/add";
    }

    @PostMapping("/rating/validate")
    public String validate(@Valid Rating rating, BindingResult result, Model model, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "rating/add";
        }

        ratingService.addRating(rating);
        redirectAttributes.addFlashAttribute("successMessage", "Rating ajouté avec succès !");
        model.addAttribute("ratings", rating);
        // TODO: check data valid and save to db, after saving return Rating list
        return "redirect:/rating/list";
    }

    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {

        Rating rating = ratingService.getRatingById(id);
        model.addAttribute("rating", rating);

        // TODO: get Rating by Id and to model then show to the form
        return "rating/update";
    }

    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @Valid Rating rating,
                               BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "rating/update";
        }

        ratingService.updateRating(id, rating);

        // TODO: check required fields, if valid call service to update Rating and return Rating list
        return "redirect:/rating/list";
    }

    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {

        try {
            ratingService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Rating supprimé avec succès !");
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur : Rating introuvable !");
        }

        // TODO: Find Rating by Id and delete the Rating, return to Rating list
        return "redirect:/rating/list";
    }
}
