package com.example.demo.controller;

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
}