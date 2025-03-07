package nnk.springboot.controllers;

import nnk.springboot.domain.BidList;
import nnk.springboot.domain.User;
import nnk.springboot.repositories.UserRepository;
import nnk.springboot.service.BidListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BidListControllerTests {

    @Mock
    private BidListService bidListService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Model model;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private BidListController bidListController;


    @Test
    void testHomeMethod() {

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        when(authentication.getName()).thenReturn("testUser");
        User user = new User();
        user.setUsername("testUser");
        user.setRole("ADMIN");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        List<BidList> bidLists = List.of(new BidList());
        when(bidListService.getAllBidList()).thenReturn(bidLists);

        String viewName = bidListController.home(model);

        verify(model).addAttribute(eq("isAdmin"), eq(true));
        verify(model).addAttribute(eq("username"), eq("testUser"));
        verify(model).addAttribute(eq("bidLists"), eq(bidLists));
        verifyNoMoreInteractions(model);
    }

    @Test
    void testAddBidForm() {
        BidList bid = new BidList();
        String viewName = bidListController.addBidForm(bid, model);
        verify(model).addAttribute("bidList", bid);
    }

    @Test
    void testValidate_Success() {
        BidList bid = new BidList();
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = bidListController.validate(bid, bindingResult, model, redirectAttributes);

        verify(bidListService).addBid(bid);
        verify(redirectAttributes).addFlashAttribute("successMessage", "Bid ajouté avec succès !");
    }

    @Test
    void testValidate_Failure() {
        BidList bid = new BidList();
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = bidListController.validate(bid, bindingResult, model, redirectAttributes);
    }

    @Test
    void testShowUpdateForm() {
        int id = 1;
        BidList bid = new BidList();
        when(bidListService.getBidById(id)).thenReturn(bid);

        String viewName = bidListController.showUpdateForm(id, model);
        verify(model).addAttribute("bidList", bid);
    }

    @Test
    void testUpdateBid_Success() {
        int id = 1;
        BidList bidList = new BidList();
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = bidListController.updateBid(id, bidList, bindingResult, model);
        verify(bidListService).updateBid(id, bidList);
    }

    @Test
    void testUpdateBid_Failure() {
        int id = 1;
        BidList bidList = new BidList();
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = bidListController.updateBid(id, bidList, bindingResult, model);
    }

    @Test
    void testDeleteBid_Success() {
        int id = 1;
        String viewName = bidListController.deleteBid(id, redirectAttributes);
        verify(bidListService).deleteById(id);
        verify(redirectAttributes).addFlashAttribute("successMessage", "Bid supprimé avec succès !");
    }

    @Test
    void testDeleteBid_Failure() {
        int id = 1;
        doThrow(new NoSuchElementException()).when(bidListService).deleteById(id);

        String viewName = bidListController.deleteBid(id, redirectAttributes);
        verify(redirectAttributes).addFlashAttribute("errorMessage", "Erreur : Bid introuvable !");
    }


}
