package com.example.demo.controller;

import com.example.demo.entity.Auditorium;
import com.example.demo.service.AuditoriumService;
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
public class AuditoriumController {

    private final AuditoriumService auditoriumService;

    @GetMapping("/auditoriums")
    public String getTablePage() {
        return "auditoriums";
    }

    @GetMapping("/auditoriums-data")
    public ResponseEntity<Map<String, Object>> getEntityData(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        List<Auditorium> auditoriums = auditoriumService.getAll();
        response.put("data", auditoriums);

        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        response.put("role", role);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/auditorium/get-active/{entityId}")
    public ResponseEntity<Map<String, Object>> getActiveEntity(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        Auditorium auditorium = auditoriumService.getById(entityId);
        response.put("data", auditorium);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("@userDetailsServiceImpl.isAdmin(principal)")
    @PostMapping("/api/auditorium/update")
    public ResponseEntity<Map<String, Object>> updateRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();

        String auditoriumNumber = payload.get("0");
        String equipment = payload.get("1");
        String software = payload.get("2");
        Integer dataId = Integer.parseInt(payload.get("dataId"));

        Auditorium auditorium = auditoriumService.getById(dataId);
        if (auditorium == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if (auditoriumService.existsByAuditoriumNumber(auditorium.getId(), auditoriumNumber)) {
            response.put("error", "Запись уже существует.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        auditorium.setAuditoriumNumber(auditoriumNumber);
        auditorium.setEquipment(equipment);
        auditorium.setSoftware(software);
        auditorium.setDisabled(false);
        auditoriumService.save(auditorium);

        response.put("updatedData", auditorium);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("@userDetailsServiceImpl.isAdmin(principal)")
    @PutMapping("/api/auditorium/save-new-record")
    public ResponseEntity<Map<String, Object>> createRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();

        String auditoriumNumber = payload.get("0");
        String equipment = payload.get("1");
        String software = payload.get("2");

        if (auditoriumService.existsByAuditoriumNumber(null, auditoriumNumber)) {
            response.put("error", "Запись уже существует.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        Auditorium auditorium = new Auditorium();
        auditorium.setAuditoriumNumber(auditoriumNumber);
        auditorium.setEquipment(equipment);
        auditorium.setSoftware(software);
        auditorium.setDisabled(false);
        auditoriumService.save(auditorium);

        response.put("createdData", auditorium);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("@userDetailsServiceImpl.isAdmin(principal)")
    @DeleteMapping("/api/auditorium/delete-record/{entityId}")
    public ResponseEntity<Map<String, Object>> deleteRecord(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        Auditorium auditorium = auditoriumService.getById(entityId);
        if (auditorium == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        auditorium.setDisabled(true);
        auditoriumService.save(auditorium);

        response.put("deletedData", auditorium.getId());
        return ResponseEntity.ok(response);
    }
}
