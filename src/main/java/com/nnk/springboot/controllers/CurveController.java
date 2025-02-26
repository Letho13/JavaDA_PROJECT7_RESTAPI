package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.service.CurvePointService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
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

    // TODO: Inject Curve Point service

    @RequestMapping("/curvePoint/list")
    public String home(Model model) {
        List<CurvePoint> allCurvePoint = curvePointService.getAllCurvePoint();
        model.addAttribute("curvePoint",allCurvePoint);
        // TODO: find all Curve Point, add to model
        return "curvePoint/list";
    }

    @GetMapping("/curvePoint/add")
    public String addCurveForm(CurvePoint curvePoint, Model model) {
        model.addAttribute("curvePoint",curvePoint);
        return "curvePoint/add";
    }

    @PostMapping("/curvePoint/validate")
    public String validate(@Valid CurvePoint curvePoint, BindingResult result, Model model ,RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "curvePoint/add";
        }
        curvePointService.addCurvePoint(curvePoint);
        redirectAttributes.addFlashAttribute("successMessage", "CurvePoint ajouté avec succès !");
        model.addAttribute("curvePoints",curvePoint);
        // TODO: check data valid and save to db, after saving return Curve list
        return "redirect:/curvePoint/add";
    }

    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {

        CurvePoint curvePoint = curvePointService.getCurvePointById(id);
        model.addAttribute("curvePoint", curvePoint);

        // TODO: get CurvePoint by Id and to model then show to the form
        return "curvePoint/update";
    }

    @PostMapping("/curvePoint/update/{id}")
    public String updateCurvePoint(@PathVariable("id") Integer id, @Valid CurvePoint curvePoint,
                             BindingResult result, Model model) {
        if(result.hasErrors()){
            return "curvePoint/update";
        }
        curvePointService.updateCurvePoint(id,curvePoint);
        // TODO: check required fields, if valid call service to update Curve and return Curve list
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
        // TODO: Find Curve by Id and delete the Curve, return to Curve list
        return "redirect:/curvePoint/list";
    }
}
