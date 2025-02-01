package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String showLoginForm(Model model, String error) {
        if (error != null) {
            model.addAttribute("error", "Неправильные имя пользователя или пароль.");
        }
        return "login";
    }

    @GetMapping("/default")
    public String defaultAfterLogin(HttpSession session) {
        String role = (String) session.getAttribute("role");
        if (role.equals("administrator")) {
            session.setAttribute("isAdmin", true);
            return "redirect:/home";
        }

        return "redirect:/home";
    }
}
