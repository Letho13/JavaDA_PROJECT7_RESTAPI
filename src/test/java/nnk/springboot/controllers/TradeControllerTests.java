package nnk.springboot.controllers;

import nnk.springboot.domain.Trade;
import nnk.springboot.domain.User;
import nnk.springboot.repositories.UserRepository;
import nnk.springboot.service.TradeService;
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
class TradeControllerTests {

    @Mock
    private TradeService tradeService;

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
    private TradeController tradeController;

    @Test
    void testTradeHomeMethod() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");

        User user = new User();
        user.setUsername("testUser");
        user.setRole("ADMIN");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        List<Trade> trades = List.of(new Trade());
        when(tradeService.getAllTrade()).thenReturn(trades);

        String viewName = tradeController.home(model);

        verify(model).addAttribute("isAdmin", true);
        verify(model).addAttribute("username", "testUser");
        verify(model).addAttribute("trades", trades);
    }

    @Test
    void testAddTradeForm() {
        Trade trade = new Trade();
        String viewName = tradeController.addTrade(trade, model);
        verify(model).addAttribute("trade", trade);
    }

    @Test
    void testValidateTrade_Success() {
        Trade trade = new Trade();
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = tradeController.validate(trade, bindingResult, model, redirectAttributes);

        verify(tradeService).addTrade(trade);
        verify(redirectAttributes).addFlashAttribute("successMessage", "Trade ajouté avec succès !");
    }

    @Test
    void testValidateTrade_Failure() {
        Trade trade = new Trade();
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = tradeController.validate(trade, bindingResult, model, redirectAttributes);
        verifyNoInteractions(tradeService);
    }

    @Test
    void testShowUpdateForm() {
        int id = 1;
        Trade trade = new Trade();
        when(tradeService.getTradeById(id)).thenReturn(trade);

        String viewName = tradeController.showUpdateForm(id, model);
        verify(model).addAttribute("trade", trade);
    }

    @Test
    void testUpdateTrade_Success() {
        int id = 1;
        Trade trade = new Trade();
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = tradeController.updateTrade(id, trade, bindingResult, model);
        verify(tradeService).updateTrade(id, trade);
    }

    @Test
    void testUpdateTrade_Failure() {
        int id = 1;
        Trade trade = new Trade();
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = tradeController.updateTrade(id, trade, bindingResult, model);
        verifyNoInteractions(tradeService);
    }

    @Test
    void testDeleteTrade_Success() {
        int id = 1;
        String viewName = tradeController.deleteTrade(id, redirectAttributes);
        verify(tradeService).deleteById(id);
        verify(redirectAttributes).addFlashAttribute("successMessage", "Trade supprimé avec succès !");
    }

    @Test
    void testDeleteTrade_Failure() {
        int id = 1;
        doThrow(new NoSuchElementException()).when(tradeService).deleteById(id);

        String viewName = tradeController.deleteTrade(id, redirectAttributes);
        verify(redirectAttributes).addFlashAttribute("errorMessage", "Erreur : Trade introuvable !");
    }
}
