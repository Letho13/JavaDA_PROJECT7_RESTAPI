package nnk.springboot.controllers;

import nnk.springboot.domain.CurvePoint;
import nnk.springboot.domain.User;
import nnk.springboot.repositories.UserRepository;
import nnk.springboot.service.CurvePointService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurveControllerTests {

    @Mock
    private CurvePointService curvePointService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private CurveController curveController;

    @Test
    void testHomeMethod() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");

        User user = new User();
        user.setUsername("testUser");
        user.setRole("ADMIN");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        List<CurvePoint> curvePoints = List.of(new CurvePoint());
        when(curvePointService.getAllCurvePoint()).thenReturn(curvePoints);

        String viewName = curveController.home(model);

        verify(model).addAttribute("isAdmin", true);
        verify(model).addAttribute("username", "testUser");
        verify(model).addAttribute("curvePoints", curvePoints);
    }

    @Test
    void testAddCurveForm() {
        CurvePoint curvePoint = new CurvePoint();
        String viewName = curveController.addCurveForm(curvePoint, model);
        verify(model).addAttribute("curvePoint", curvePoint);
    }

    @Test
    void testValidate_Success() {
        CurvePoint curvePoint = new CurvePoint();
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = curveController.validate(curvePoint, bindingResult, model, redirectAttributes);

        verify(curvePointService).addCurvePoint(curvePoint);
        verify(redirectAttributes).addFlashAttribute("successMessage", "CurvePoint ajouté avec succès !");
    }

    @Test
    void testValidate_Failure() {
        CurvePoint curvePoint = new CurvePoint();
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = curveController.validate(curvePoint, bindingResult, model, redirectAttributes);
    }

    @Test
    void testShowUpdateForm() {
        int id = 1;
        CurvePoint curvePoint = new CurvePoint();
        when(curvePointService.getCurvePointById(id)).thenReturn(curvePoint);

        String viewName = curveController.showUpdateForm(id, model);
        verify(model).addAttribute("curvePoint", curvePoint);
    }

    @Test
    void testUpdateCurvePoint_Success() {
        int id = 1;
        CurvePoint curvePoint = new CurvePoint();
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = curveController.updateCurvePoint(id, curvePoint, bindingResult, model);
        verify(curvePointService).updateCurvePoint(id, curvePoint);
    }

    @Test
    void testUpdateCurvePoint_Failure() {
        int id = 1;
        CurvePoint curvePoint = new CurvePoint();
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = curveController.updateCurvePoint(id, curvePoint, bindingResult, model);
    }

    @Test
    void testDeleteCurve_Success() {
        int id = 1;
        String viewName = curveController.deleteCurve(id, redirectAttributes);
        verify(curvePointService).deleteById(id);
        verify(redirectAttributes).addFlashAttribute("successMessage", "CurvePoint supprimé avec succès !");
    }

    @Test
    void testDeleteCurve_Failure() {
        int id = 1;
        doThrow(new NoSuchElementException()).when(curvePointService).deleteById(id);

        String viewName = curveController.deleteCurve(id, redirectAttributes);
        verify(redirectAttributes).addFlashAttribute("errorMessage", "Erreur : CurvePoint introuvable !");
    }
}
