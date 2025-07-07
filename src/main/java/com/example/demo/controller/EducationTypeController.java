package com.example.demo.controller;

import com.example.demo.entity.EducationType;
import com.example.demo.service.EducationTypeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class EducationTypeController {

    private final EducationTypeService educationTypeService;

    @GetMapping("/education-types")
    public String getTablePage() {
        return "education-types";
    }

    @GetMapping("/education-types-data")
    public ResponseEntity<Map<String, Object>> getEntityData(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        List<EducationType> educationTypes = educationTypeService.getAll();
        response.put("data", educationTypes);

        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        response.put("role", role);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/education-type/get-active/{entityId}")
    public ResponseEntity<Map<String, Object>> getActiveEntity(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        EducationType educationType = educationTypeService.getById(entityId);
        response.put("data", educationType);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("@userDetailsServiceImpl.isAdmin(principal)")
    @PostMapping("/api/education-type/update")
    public ResponseEntity<Map<String, Object>> updateRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();

        String name = payload.get("0");
        int learningPeriod;
        try {
            learningPeriod = Integer.parseInt(payload.get("1"));
        } catch (NumberFormatException ex) {
            response.put("error", "Неверный формат данных.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        String text = payload.get("2");
        Integer dataId = Integer.parseInt(payload.get("dataId"));

        EducationType educationType = educationTypeService.getById(dataId);
        if (educationType == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if (educationTypeService.existsByName(educationType.getId(), name)) {
            response.put("error", "Запись уже существует.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        educationType.setName(name);
        educationType.setLearningPeriod(learningPeriod);
        educationType.setText(text);
        educationType.setDisabled(false);
        educationTypeService.save(educationType);

        response.put("updatedData", educationType);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("@userDetailsServiceImpl.isAdmin(principal)")
    @PutMapping("/api/education-type/save-new-record")
    public ResponseEntity<Map<String, Object>> createRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();

        String name = payload.get("0");
        int learningPeriod;
        try {
            learningPeriod = Integer.parseInt(payload.get("1"));
        } catch (NumberFormatException ex) {
            response.put("error", "Неверный формат данных.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        String text = payload.get("2");

        if (educationTypeService.existsByName(null, name)) {
            response.put("error", "Запись уже существует.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        EducationType educationType = new EducationType();
        educationType.setName(name);
        educationType.setLearningPeriod(learningPeriod);
        educationType.setText(text);
        educationType.setDisabled(false);
        educationTypeService.save(educationType);

        response.put("createdData", educationType);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("@userDetailsServiceImpl.isAdmin(principal)")
    @DeleteMapping("/api/education-type/delete-record/{entityId}")
    public ResponseEntity<Map<String, Object>> deleteRecord(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        EducationType educationType = educationTypeService.getById(entityId);
        if (educationType == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        educationType.setDisabled(true);
        educationTypeService.save(educationType);

        response.put("deletedData", educationType.getId());
        return ResponseEntity.ok(response);
    }
}
