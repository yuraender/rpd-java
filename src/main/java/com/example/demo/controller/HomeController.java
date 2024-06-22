package com.example.demo.controller;

import com.example.demo.entity.Institute;
import com.example.demo.service.InstituteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("instituteId")
public class HomeController {
    @Autowired
    private InstituteService instituteService;
    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        Long instituteId = (Long) session.getAttribute("instituteId");
        boolean isInstituteSelected = instituteId != null;
        model.addAttribute("isInstituteSelected", isInstituteSelected);
        System.out.println("institute controller");
        String role = (String) session.getAttribute("role");

        if (role.equals("administrator")) {
            model.addAttribute("isAdmin", true);
        }
        if (isInstituteSelected) {
            Institute institute = instituteService.findById(instituteId);
            model.addAttribute("institute", institute);
        }
        return "home";
    }
}
