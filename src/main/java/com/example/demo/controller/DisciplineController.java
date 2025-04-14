package com.example.demo.controller;

import com.example.demo.entity.Discipline;
import com.example.demo.service.DisciplineService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class DisciplineController {

    private final DisciplineService disciplineService;

    @GetMapping("/disciplines")
    public String getTablePage() {
        return "disciplines";
    }

    @GetMapping("/disciplines-data")
    public ResponseEntity<Map<String, Object>> getEntityData(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        List<Discipline> disciplines = disciplineService.getAll();
        response.put("data", disciplines);

        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        response.put("role", role);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/discipline/get-active/{entityId}")
    public ResponseEntity<Map<String, Object>> getActiveEntity(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        Discipline discipline = disciplineService.getById(entityId);
        response.put("data", discipline);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/discipline/update")
    public ResponseEntity<Map<String, Object>> updateRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();

        String name = payload.get("0");
        Integer dataId = Integer.parseInt(payload.get("dataId"));

        Discipline discipline = disciplineService.getById(dataId);
        if (discipline == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if (disciplineService.existsByName(discipline.getId(), name)) {
            response.put("error", "Запись уже существует.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        discipline.setName(name);
        discipline.setDisabled(false);
        disciplineService.save(discipline);

        response.put("updatedData", discipline);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/api/discipline/save-new-record")
    public ResponseEntity<Map<String, Object>> createRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();

        String name = payload.get("0");

        if (disciplineService.existsByName(null, name)) {
            response.put("error", "Запись уже существует.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        Discipline discipline = new Discipline();
        discipline.setName(name);
        discipline.setDisabled(false);
        disciplineService.save(discipline);

        response.put("createdData", discipline);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/api/discipline/delete-record/{entityId}")
    public ResponseEntity<Map<String, Object>> deleteRecord(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        Discipline discipline = disciplineService.getById(entityId);
        if (discipline == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        discipline.setDisabled(true);
        disciplineService.save(discipline);

        response.put("deletedData", discipline.getId());
        return ResponseEntity.ok(response);
    }
}
