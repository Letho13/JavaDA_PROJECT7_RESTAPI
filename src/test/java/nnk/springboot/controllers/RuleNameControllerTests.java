package nnk.springboot.controllers;

import nnk.springboot.domain.RuleName;
import nnk.springboot.domain.User;
import nnk.springboot.repositories.UserRepository;
import nnk.springboot.service.RuleNameService;
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
class RuleNameControllerTests {

    @Mock
    private RuleNameService ruleNameService;

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
    private RuleNameController ruleNameController;

    @Test
    void testRuleNameHomeMethod() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");

        User user = new User();
        user.setUsername("testUser");
        user.setRole("ADMIN");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        List<RuleName> ruleNames = List.of(new RuleName());
        when(ruleNameService.getAllRuleName()).thenReturn(ruleNames);

        String viewName = ruleNameController.home(model);

        verify(model).addAttribute("isAdmin", true);
        verify(model).addAttribute("username", "testUser");
        verify(model).addAttribute("ruleNames", ruleNames);
    }

    @Test
    void testAddRuleNameForm() {
        RuleName ruleName = new RuleName();
        String viewName = ruleNameController.addRuleForm(ruleName, model);
        verify(model).addAttribute("ruleName", ruleName);
    }

    @Test
    void testValidateRuleName_Success() {
        RuleName ruleName = new RuleName();
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = ruleNameController.validate(ruleName, bindingResult, model, redirectAttributes);

        verify(ruleNameService).addRuleName(ruleName);
        verify(redirectAttributes).addFlashAttribute("successMessage", "RuleName ajouté avec succès !");
    }

    @Test
    void testValidateRuleName_Failure() {
        RuleName ruleName = new RuleName();
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = ruleNameController.validate(ruleName, bindingResult, model, redirectAttributes);
        verifyNoInteractions(ruleNameService);
    }

    @Test
    void testShowUpdateForm() {
        int id = 1;
        RuleName ruleName = new RuleName();
        when(ruleNameService.getByIdRuleName(id)).thenReturn(ruleName);

        String viewName = ruleNameController.showUpdateForm(id, model);
        verify(model).addAttribute("ruleName", ruleName);
    }

    @Test
    void testUpdateRuleName_Success() {
        int id = 1;
        RuleName ruleName = new RuleName();
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = ruleNameController.updateRuleName(id, ruleName, bindingResult, model);
        verify(ruleNameService).updateRuleName(id, ruleName);
    }

    @Test
    void testUpdateRuleName_Failure() {
        int id = 1;
        RuleName ruleName = new RuleName();
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = ruleNameController.updateRuleName(id, ruleName, bindingResult, model);
        verifyNoInteractions(ruleNameService);
    }

    @Test
    void testDeleteRuleName_Success() {
        int id = 1;
        String viewName = ruleNameController.deleteRuleName(id, redirectAttributes);
        verify(ruleNameService).deleteById(id);
        verify(redirectAttributes).addFlashAttribute("successMessage", "RuleName supprimé avec succès !");
    }

    @Test
    void testDeleteRuleName_Failure() {
        int id = 1;
        doThrow(new NoSuchElementException()).when(ruleNameService).deleteById(id);

        String viewName = ruleNameController.deleteRuleName(id, redirectAttributes);
        verify(redirectAttributes).addFlashAttribute("errorMessage", "Erreur : RuleName introuvable !");
    }
}
