package nnk.springboot.controllers;

import nnk.springboot.domain.CurvePoint;
import nnk.springboot.domain.User;
import nnk.springboot.repositories.UserRepository;
import nnk.springboot.service.CurvePointService;
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
public class CurveController {

    private final CurvePointService curvePointService;
    private final UserRepository userRepository;

    @RequestMapping("/curvePoint/list")
    public String home(Model model) {
        List<CurvePoint> allCurvePoint = curvePointService.getAllCurvePoint();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User loggedInUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur  " + username + "introuvable avec l'username"));

        String role = loggedInUser.getRole();
        boolean isAdmin = role.equals("ADMIN");

        model.addAttribute("isAdmin",isAdmin);

        model.addAttribute("username", loggedInUser.getUsername());
        model.addAttribute("curvePoints", allCurvePoint);
        return "curvePoint/list";
    }

    @GetMapping("/curvePoint/add")
    public String addCurveForm(CurvePoint curvePoint, Model model) {
        model.addAttribute("curvePoint", curvePoint);
        return "curvePoint/add";
    }

    @PostMapping("/curvePoint/validate")
    public String validate(@Valid CurvePoint curvePoint, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "curvePoint/add";
        }
        curvePointService.addCurvePoint(curvePoint);
        redirectAttributes.addFlashAttribute("successMessage", "CurvePoint ajouté avec succès !");
        model.addAttribute("curvePoints", curvePoint);
        return "redirect:/curvePoint/list";
    }

    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {

        CurvePoint curvePoint = curvePointService.getCurvePointById(id);
        model.addAttribute("curvePoint", curvePoint);

        return "curvePoint/update";
    }

    @PostMapping("/curvePoint/update/{id}")
    public String updateCurvePoint(@PathVariable("id") Integer id, @Valid CurvePoint curvePoint,
                                   BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "curvePoint/update";
        }
        curvePointService.updateCurvePoint(id, curvePoint);
        return "redirect:/curvePoint/list";
    }

    @GetMapping("/curvePoint/delete/{id}")
    public String deleteCurve(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {

        try {
            curvePointService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "CurvePoint supprimé avec succès !");
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur : CurvePoint introuvable !");
        }
        return "redirect:/curvePoint/list";
    }
}
