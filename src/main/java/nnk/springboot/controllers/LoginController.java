package nnk.springboot.controllers;

import nnk.springboot.domain.User;
import nnk.springboot.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
//@RequiredArgsConstructor
@Controller
//@RequestMapping("")
public class LoginController {

    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    public LoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

//    @GetMapping("/login")
//    public ModelAndView login() {
//        ModelAndView mav = new ModelAndView();
//        logger.info("Affichage de la page de login");
//        mav.setViewName("login");
//        return mav;
//    }


    @GetMapping("secure/article-details")
    public ModelAndView getAllUserArticles() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("users", userRepository.findAll());
        mav.setViewName("user/list");
        return mav;
    }

    @GetMapping("error")
    public ModelAndView error(Model model) {
        ModelAndView mav = new ModelAndView();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User loggedInUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur  " + username + "introuvable avec l'username"));

        model.addAttribute("username", loggedInUser.getUsername());
        String errorMessage= "You are not authorized for the requested data.";
        mav.addObject("errorMsg", errorMessage);
        mav.setViewName("403");
        return mav;
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        return "redirect:/login";
    }
}
