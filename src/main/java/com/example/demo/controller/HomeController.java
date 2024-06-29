package com.example.demo.controller;

import com.example.demo.entity.DisciplineEducationalProgram;
import com.example.demo.entity.Institute;
import com.example.demo.service.DisciplineEducationalProgramService;
import com.example.demo.service.EmployeePositionService;
import com.example.demo.service.InstituteService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@SessionAttributes("instituteId")
public class HomeController {
    @Autowired
    private InstituteService instituteService;
    @Autowired
    private DisciplineEducationalProgramService disciplineEducationalProgramService;
    @Autowired
    private EmployeePositionService employeePositionService;
    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        Long instituteId = (Long) session.getAttribute("instituteId");
        boolean isInstituteSelected = instituteId != null;

        model.addAttribute("isInstituteSelected", isInstituteSelected);
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

    @GetMapping("/home-data")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getEntityData(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        HttpSession session = request.getSession();

        Object oopIdObj = session.getAttribute("oopId");

        Long oopId = null;
        if (oopIdObj != null) {
            try {
                oopId = Long.parseLong(oopIdObj.toString());
                response.put("oopId", oopId);
            } catch (NumberFormatException e) {
                response.put("error", "Неверный формат OOP ID");
                return ResponseEntity.badRequest().body(response);
            }
        }

        String role = (String) session.getAttribute("role");
        response.put("role", role);

        Long instituteId = (Long) session.getAttribute("instituteId");
        response.put("instituteId", instituteId);

        if(oopId != null){
            //Список дисциплин ОП, для которых будет созданы пакеты РПД
            List<DisciplineEducationalProgram> allDisciplineEducationalPrograms = disciplineEducationalProgramService.getAll();
            Long finalOopId = oopId;
            List<DisciplineEducationalProgram> disciplineEducationalPrograms = allDisciplineEducationalPrograms.stream()
                    .filter(el -> Long.valueOf(el.getBasicEducationalProgram().getId()).equals(finalOopId))
                    .filter(el -> el.getDisabled().equals(false)).toList();
            response.put("disciplinesOP", disciplineEducationalPrograms);

        }else{
            response.put("disciplinesOP", "");
        }
        response.put("oopId", oopId);

        return ResponseEntity.ok(response);
    }
}
