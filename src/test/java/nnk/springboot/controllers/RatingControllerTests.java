package nnk.springboot.controllers;

import nnk.springboot.domain.Rating;
import nnk.springboot.domain.User;
import nnk.springboot.repositories.UserRepository;
import nnk.springboot.service.RatingService;
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
class RatingControllerTests {

    @Mock
    private RatingService ratingService;

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
    private RatingController ratingController;

    @Test
    void testRatingHomeMethod() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");

        User user = new User();
        user.setUsername("testUser");
        user.setRole("ADMIN");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        List<Rating> ratings = List.of(new Rating());
        when(ratingService.getAllRating()).thenReturn(ratings);

        String viewName = ratingController.home(model);

        verify(model).addAttribute("isAdmin", true);
        verify(model).addAttribute("username", "testUser");
        verify(model).addAttribute("ratings", ratings);
    }

    @Test
    void testAddRatingForm() {
        Rating rating = new Rating();
        String viewName = ratingController.addRatingForm(rating, model);
        verify(model).addAttribute("rating", rating);
    }

    @Test
    void testValidateRating_Success() {
        Rating rating = new Rating();
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = ratingController.validate(rating, bindingResult, model, redirectAttributes);

        verify(ratingService).addRating(rating);
        verify(redirectAttributes).addFlashAttribute("successMessage", "Rating ajouté avec succès !");
    }

    @Test
    void testValidateRating_Failure() {
        Rating rating = new Rating();
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = ratingController.validate(rating, bindingResult, model, redirectAttributes);
        verifyNoInteractions(ratingService);
    }

    @Test
    void testShowUpdateForm() {
        int id = 1;
        Rating rating = new Rating();
        when(ratingService.getRatingById(id)).thenReturn(rating);

        String viewName = ratingController.showUpdateForm(id, model);
        verify(model).addAttribute("rating", rating);
    }

    @Test
    void testUpdateRating_Success() {
        int id = 1;
        Rating rating = new Rating();
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = ratingController.updateRating(id, rating, bindingResult, model);
        verify(ratingService).updateRating(id, rating);
    }

    @Test
    void testUpdateRating_Failure() {
        int id = 1;
        Rating rating = new Rating();
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = ratingController.updateRating(id, rating, bindingResult, model);
        verifyNoInteractions(ratingService);
    }

    @Test
    void testDeleteRating_Success() {
        int id = 1;
        String viewName = ratingController.deleteRating(id, redirectAttributes);
        verify(ratingService).deleteById(id);
        verify(redirectAttributes).addFlashAttribute("successMessage", "Rating supprimé avec succès !");
    }

    @Test
    void testDeleteRating_Failure() {
        int id = 1;
        doThrow(new NoSuchElementException()).when(ratingService).deleteById(id);

        String viewName = ratingController.deleteRating(id, redirectAttributes);
        verify(redirectAttributes).addFlashAttribute("errorMessage", "Erreur : Rating introuvable !");
    }
}
