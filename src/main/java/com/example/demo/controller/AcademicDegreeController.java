package com.example.demo.controller;

import com.example.demo.entity.AcademicDegree;
import com.example.demo.service.AcademicDegreeService;
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
public class AcademicDegreeController {

    private final AcademicDegreeService academicDegreeService;

    @GetMapping("/academic-degrees")
    public String getTablePage() {
        return "academic-degrees";
    }

    @GetMapping("/academic-degrees-data")
    public ResponseEntity<Map<String, Object>> getEntityData(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        List<AcademicDegree> academicDegrees = academicDegreeService.getAll();
        response.put("data", academicDegrees);

        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        response.put("role", role);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/academic-degree/get-active/{entityId}")
    public ResponseEntity<Map<String, Object>> getActiveEntity(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        AcademicDegree academicDegree = academicDegreeService.getById(entityId);
        response.put("data", academicDegree);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("@userDetailsServiceImpl.isAdmin(principal)")
    @PostMapping("/api/academic-degree/update")
    public ResponseEntity<Map<String, Object>> updateRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();

        String name = payload.get("0");
        String shortName = payload.get("1");
        Integer dataId = Integer.parseInt(payload.get("dataId"));

        AcademicDegree academicDegree = academicDegreeService.getById(dataId);
        if (academicDegree == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if (academicDegreeService.existsByNameOrShortName(academicDegree.getId(), name, shortName)) {
            response.put("error", "Запись уже существует.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        academicDegree.setName(name);
        academicDegree.setShortName(shortName);
        academicDegree.setDisabled(false);
        academicDegreeService.save(academicDegree);

        response.put("updatedData", academicDegree);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("@userDetailsServiceImpl.isAdmin(principal)")
    @PutMapping("/api/academic-degree/save-new-record")
    public ResponseEntity<Map<String, Object>> createRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();

        String name = payload.get("0");
        String shortName = payload.get("1");

        if (academicDegreeService.existsByNameOrShortName(null, name, shortName)) {
            response.put("error", "Запись уже существует.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        AcademicDegree academicDegree = new AcademicDegree();
        academicDegree.setName(name);
        academicDegree.setShortName(shortName);
        academicDegree.setDisabled(false);
        academicDegreeService.save(academicDegree);

        response.put("createdData", academicDegree);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("@userDetailsServiceImpl.isAdmin(principal)")
    @DeleteMapping("/api/academic-degree/delete-record/{entityId}")
    public ResponseEntity<Map<String, Object>> deleteRecord(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        AcademicDegree academicDegree = academicDegreeService.getById(entityId);
        if (academicDegree == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        academicDegree.setDisabled(true);
        academicDegreeService.save(academicDegree);

        response.put("deletedData", academicDegree.getId());
        return ResponseEntity.ok(response);
    }
}
