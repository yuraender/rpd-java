package com.example.demo.controller;

import com.example.demo.entity.BasicEducationalProgramDiscipline;
import com.example.demo.service.BasicEducationalProgramDisciplineService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    private BasicEducationalProgramDisciplineService basicEducationalProgramDisciplineService;

    @GetMapping("/")
    public String home() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        String role = (String) session.getAttribute("role");
        if (role.equals("administrator")) {
            model.addAttribute("isAdmin", true);
        }
        return "home";
    }

    @GetMapping("/home-data")
    public ResponseEntity<Map<String, Object>> getEntityData(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        Integer bepId;
        try {
            bepId = (Integer) request.getSession().getAttribute("bepId");
        } catch (ClassCastException ex) {
            response.put("error", "Неверный формат данных.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        response.put("bepId", bepId);

        List<BasicEducationalProgramDiscipline> bepDisciplines = basicEducationalProgramDisciplineService.getAll()
                .stream()
                .filter(bepd -> bepId != null && bepd.getBasicEducationalProgram().getId() == bepId)
                .toList();
        response.put("bepDisciplines", bepDisciplines);

        return ResponseEntity.ok(response);
    }
}
