package com.example.demo.controller;

import com.example.demo.entity.Institute;
import com.example.demo.service.InstituteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
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
}