package com.example.demo.controller;

import com.example.demo.entity.AcademicRank;
import com.example.demo.service.AcademicRankService;
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
public class AcademicRankController {

    private final AcademicRankService academicRankService;

    @GetMapping("/academic-ranks")
    public String getTablePage() {
        return "academic-ranks";
    }

    @GetMapping("/academic-ranks-data")
    public ResponseEntity<Map<String, Object>> getEntityData(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        List<AcademicRank> academicRanks = academicRankService.getAll();
        response.put("data", academicRanks);

        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        response.put("role", role);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/academic-rank/get-active/{entityId}")
    public ResponseEntity<Map<String, Object>> getActiveEntity(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        AcademicRank academicRank = academicRankService.getById(entityId);
        response.put("data", academicRank);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/academic-rank/update")
    public ResponseEntity<Map<String, Object>> updateRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();

        String name = payload.get("0");
        Integer dataId = Integer.parseInt(payload.get("dataId"));

        AcademicRank academicRank = academicRankService.getById(dataId);
        if (academicRank == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if (academicRankService.existsByName(academicRank.getId(), name)) {
            response.put("error", "Запись уже существует.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        academicRank.setName(name);
        academicRank.setDisabled(false);
        academicRankService.save(academicRank);

        response.put("updatedData", academicRank);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/api/academic-rank/save-new-record")
    public ResponseEntity<Map<String, Object>> createRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();

        String name = payload.get("0");

        if (academicRankService.existsByName(null, name)) {
            response.put("error", "Запись уже существует.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        AcademicRank academicRank = new AcademicRank();
        academicRank.setName(name);
        academicRank.setDisabled(false);
        academicRankService.save(academicRank);

        response.put("createdData", academicRank);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/api/academic-rank/delete-record/{entityId}")
    public ResponseEntity<Map<String, Object>> deleteRecord(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        AcademicRank academicRank = academicRankService.getById(entityId);
        if (academicRank == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        academicRank.setDisabled(true);
        academicRankService.save(academicRank);

        response.put("deletedData", academicRank.getId());
        return ResponseEntity.ok(response);
    }
}
