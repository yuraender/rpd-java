package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class TechSupportController {

    private final TechSupportService techSupportService;
    private final DepartmentService departmentService;
    private final TeacherService teacherService;
    private final AudienceService audienceService;
    private final DisciplineService disciplineService;

    @GetMapping("/tech-support")
    public String getTablePage() {
        return "tech-support";
    }

    @GetMapping("/tech-support-data")
    public ResponseEntity<Map<String, Object>> getEntityData(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        List<TechSupport> techSupport = techSupportService.getAll();
        response.put("data", techSupport);

        List<Department> departments = departmentService.getAll();
        response.put("departments", departments);

        List<Discipline> disciplines = disciplineService.getAll();
        response.put("disciplines", disciplines);

        List<Audience> audiences = audienceService.getAll();
        response.put("audiences", audiences);

        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        response.put("role", role);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/tech-support/get-active/{entityId}")
    public ResponseEntity<Map<String, Object>> getActiveEntity(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        TechSupport techSupport = techSupportService.getById(entityId);
        response.put("data", techSupport);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/tech-support/save-new-record")
    public ResponseEntity<Map<String, Object>> createRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();

        int param0, param1;
        try {
            param0 = Integer.parseInt(payload.get("0"));
            param1 = Integer.parseInt(payload.get("1"));
        } catch (NumberFormatException ex) {
            response.put("error", "Неверный формат данных.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        TechSupport existingTechSupport = techSupportService.getByDisciplineIdAndAudienceId(param0, param1);
        if (existingTechSupport != null) {
            response.put("error", "Запись уже существует.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        Discipline discipline = disciplineService.getById(param0);
        Audience audience = audienceService.getById(param1);
        if (discipline == null || audience == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        TechSupport techSupport = new TechSupport();
        techSupport.setDiscipline(discipline);
        techSupport.setAudience(audience);
        techSupport.setDisabled(false);
        techSupportService.save(techSupport);

        response.put("createdData", techSupport);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/tech-support/delete-record/{entityId}")
    public ResponseEntity<Map<String, Object>> deleteRecord(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        TechSupport techSupport = techSupportService.getById(entityId);
        if (techSupport == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        techSupport.setDisabled(true);
        techSupportService.save(techSupport);

        response.put("deletedData", techSupport.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/tech-support/department-filter/{filter1}")
    public ResponseEntity<Map<String, Object>> filterByDepartment(@PathVariable Integer filter1) {
        Map<String, Object> response = new HashMap<>();

        List<Teacher> filterList = teacherService.getAll().stream()
                .filter(t -> t.getDepartment().getId() == filter1)
                .toList();
        response.put("filterList", filterList);

        List<TechSupport> techSupport = techSupportService.getAll();

        if (filter1 == 0) {
            response.put("entityList", techSupport);
        } else {
            List<TechSupport> entityList = techSupport.stream()
                    .filter(ts -> ts.getDiscipline().getDeveloper().getDepartment().getId() == filter1)
                    .toList();
            response.put("entityList", entityList);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/tech-support/developer-filter/{filter1}/{filter2}")
    public ResponseEntity<Map<String, Object>> filterByDeveloper(@PathVariable Integer filter1, @PathVariable Integer filter2) {
        Map<String, Object> response = new HashMap<>();

        List<Discipline> filterList = disciplineService.getAll().stream()
                .filter(d -> d.getDeveloper().getDepartment().getId() == filter1)
                .filter(d -> d.getDeveloper().getId() == filter2)
                .toList();
        response.put("filterList", filterList);

        List<TechSupport> techSupport = techSupportService.getAll();

        if (filter2 == 0) {
            response.put("entityList", techSupport);
        } else {
            List<TechSupport> entityList = techSupport.stream()
                    .filter(ts -> ts.getDiscipline().getDeveloper().getDepartment().getId() == filter1)
                    .filter(ts -> ts.getDiscipline().getDeveloper().getId() == filter2)
                    .toList();
            response.put("entityList", entityList);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/tech-support/discipline-filter/{filter1}/{filter2}/{filter3}")
    public ResponseEntity<Map<String, Object>> filterByDiscipline(@PathVariable Integer filter1, @PathVariable Integer filter2, @PathVariable Integer filter3) {
        Map<String, Object> response = new HashMap<>();

        List<TechSupport> techSupport = techSupportService.getAll();

        if (filter3 == 0) {
            response.put("entityList", techSupport);
        } else {
            List<TechSupport> entityList = techSupport.stream()
                    .filter(ts -> ts.getDiscipline().getDeveloper().getDepartment().getId() == filter1)
                    .filter(ts -> ts.getDiscipline().getDeveloper().getId() == filter2)
                    .filter(ts -> ts.getDiscipline().getId() == filter3)
                    .toList();
            response.put("entityList", entityList);
        }
        return ResponseEntity.ok(response);
    }
}
