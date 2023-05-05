package com.mafv.academy.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.mafv.academy.models.Usuario;
import com.mafv.academy.repository.UsuarioRepository;

@Controller
public class LoginController {

    @Autowired
    private UsuarioRepository userRepository;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username, @RequestParam String password, HttpSession session) {

        Usuario user = userRepository.findByUsername(username);
        if (user == null) {
            // Usuario no encontrado
            return "redirect:/login?error";
        }

        if (!user.getPassword().equals(password)) {
            // Contraseña incorrecta
            return "redirect:/login?error";
        }
        if (user.isFirstLogin()) {
            session.setAttribute("userId", user.getCodigo());
            return "redirect:/change-password";
        }

        session.setAttribute("user", user);
        return "redirect:/welcome";
    }

    @GetMapping(value = {"/welcome"})
    public String welcome(){

        return "welcome";
    }

    // Logout
    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){    
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        return "redirect:/login?logout="+true; 
    }

}
