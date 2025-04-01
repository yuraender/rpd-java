package com.example.demo.controller;

import com.example.demo.entity.Competencie;
import com.example.demo.service.CompetencieService;
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
public class CompetencieController {

    private final CompetencieService competencieService;

    @GetMapping("/competencies")
    public String getTablePage() {
        return "competencies";
    }

    @GetMapping("/competencies-data")
    public ResponseEntity<Map<String, Object>> getEntityData(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        List<Competencie> competencies = competencieService.getAll();
        response.put("data", competencies);

        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        response.put("role", role);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/competencie/get-active/{entityId}")
    public ResponseEntity<Map<String, Object>> getActiveEntity(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        Competencie competencie = competencieService.getById(entityId);
        response.put("data", competencie);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/competencie/update")
    public ResponseEntity<Map<String, Object>> updateRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();

        String index = payload.get("0");
        String essence = payload.get("1");
        Integer dataId = Integer.parseInt(payload.get("dataId"));

        Competencie competencie = competencieService.getById(dataId);
        if (competencie == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        competencie.setIndex(index);
        competencie.setEssence(essence);
        competencie.setDisabled(false);
        competencieService.save(competencie);

        response.put("updatedData", competencie);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/competencie/save-new-record")
    public ResponseEntity<Map<String, Object>> createRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();

        String index = payload.get("0");
        String essence = payload.get("1");

        Competencie competencie = new Competencie();
        competencie.setIndex(index);
        competencie.setEssence(essence);
        competencie.setDisabled(false);
        competencieService.save(competencie);

        response.put("createdData", competencie);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/competencie/delete-record/{entityId}")
    public ResponseEntity<Map<String, Object>> deleteRecord(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        Competencie competencie = competencieService.getById(entityId);
        if (competencie == null) {
            response.put("error", "Запись не найдена");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        competencie.setDisabled(true);
        competencieService.save(competencie);

        response.put("deletedData", competencie.getId());
        return ResponseEntity.ok(response);
    }
}
