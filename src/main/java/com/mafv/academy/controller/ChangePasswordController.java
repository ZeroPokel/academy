package com.mafv.academy.controller;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.mafv.academy.models.Usuario;
import com.mafv.academy.repository.UsuarioRepository;

@Controller
public class ChangePasswordController {

  @Autowired
  private UsuarioRepository userRepository;

  @GetMapping("/change-password")
  public String showChangePasswordForm(HttpSession session, Model model) {
        int userId = (Integer) session.getAttribute("userId");
        Optional<Usuario> user = userRepository.findById(userId);
        
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        return "change-password";
  }

  @PostMapping("/change-password")
  public String changePassword(@ModelAttribute Usuario user, HttpSession session) {
        Usuario existingUser = userRepository.findById(user.getCodigo()).orElse(null);
        
        if (existingUser == null) {
            return "redirect:/login";
        }

        existingUser.setPassword(user.getPassword());
        existingUser.setFirstLogin(false);
        userRepository.save(existingUser);
        session.setAttribute("user", existingUser);

        return "redirect:/welcome";
  }

}
