package com.example.demo.controller;

import javax.servlet.http.HttpSession;

import com.example.demo.entity.Institute;
import com.example.demo.service.InstituteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import java.util.List;

@Controller
@SessionAttributes("instituteId")
public class InstituteController {
    @Autowired
    private InstituteService instituteService;

    @GetMapping("/choose-institute")
    public String chooseInstitute(Model model, @RequestParam(value = "keyword", required = false) String keyword) {
        List<Institute> institutes = instituteService.filterInstitutes(keyword);
        model.addAttribute("institutes", institutes);
        model.addAttribute("keyword", keyword);
        if (institutes.isEmpty()) {
            model.addAttribute("message", "В БД пока нет записей");
        }
        return "choose-institute";
    }

    @GetMapping("/select-institute")
    public String selectInstitute(@RequestParam("id") Long id, HttpSession session) {
        System.out.println(id);
        session.setAttribute("instituteId", id);
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        Long instituteId = (Long) session.getAttribute("instituteId");
        if (instituteId != null) {
            Institute institute = instituteService.findById(instituteId);
            model.addAttribute("institute", institute);
        }
        return "home";
    }
}