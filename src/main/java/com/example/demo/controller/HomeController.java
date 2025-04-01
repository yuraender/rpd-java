package com.example.demo.controller;

import com.example.demo.entity.BasicEducationalProgramDiscipline;
import com.example.demo.entity.Teacher;
import com.example.demo.service.BasicEducationalProgramDisciplineService;
import com.example.demo.service.TeacherService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    private TeacherService teacherService;
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
        HttpSession session = request.getSession();

        Object oopIdObj = session.getAttribute("oopId");

        Integer oopId = null;
        if (oopIdObj != null) {
            try {
                oopId = Integer.parseInt(oopIdObj.toString());
                response.put("oopId", oopId);
            } catch (NumberFormatException e) {
                response.put("error", "Неверный формат OOP ID");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        }

        String role = (String) session.getAttribute("role");
        response.put("role", role);

        if (oopId != null) {
            //Список дисциплин ОП, для которых будет созданы пакеты РПД
            List<BasicEducationalProgramDiscipline> allBasicEducationalProgramDisciplines = basicEducationalProgramDisciplineService.getAll();
            Integer finalOopId = oopId;
            List<BasicEducationalProgramDiscipline> bepDisciplines = allBasicEducationalProgramDisciplines.stream()
                    .filter(el -> el.getBasicEducationalProgram().getId() == finalOopId)
                    .filter(el -> !el.isDisabled()).toList();
            response.put("disciplinesOP", bepDisciplines);
        } else {
            response.put("disciplinesOP", "");
        }
        response.put("oopId", oopId);

        List<Teacher> teachers = teacherService.getAll();
        response.put("teachers", teachers);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/home/teacher-filter/{entityId}")
    public ResponseEntity<Map<String, Object>> filterByTeacher(@PathVariable Integer entityId, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        HttpSession session = request.getSession();

        Object oopIdObj = session.getAttribute("oopId");
        Integer oopId = null;
        if (oopIdObj != null) {
            try {
                oopId = Integer.parseInt(oopIdObj.toString());
                response.put("oopId", oopId);
            } catch (NumberFormatException e) {
                response.put("error", "Неверный формат OOP ID");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        }

        if (oopId != null) {
            List<BasicEducationalProgramDiscipline> allBasicEducationalProgramDisciplines = basicEducationalProgramDisciplineService.getAll();
            Integer finalOopId = oopId;
            if (entityId == 0) {
                List<BasicEducationalProgramDiscipline> bepDisciplines = allBasicEducationalProgramDisciplines.stream()
                        .filter(el -> el.getBasicEducationalProgram().getId() == finalOopId)
                        .filter(el -> !el.isDisabled()).toList();
                response.put("disciplinesOP", bepDisciplines);
            } else {
                List<BasicEducationalProgramDiscipline> bepDisciplines = allBasicEducationalProgramDisciplines.stream()
                        .filter(el -> el.getBasicEducationalProgram().getId() == finalOopId)
                        .filter(el -> el.getDiscipline().getDeveloper().getId() == entityId)
                        .filter(el -> !el.isDisabled()).toList();
                response.put("disciplinesOP", bepDisciplines);
            }
        } else {
            response.put("disciplinesOP", "");
        }

        return ResponseEntity.ok(response);
    }
}
