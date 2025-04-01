package com.example.demo.controller;

import com.example.demo.entity.Competence;
import com.example.demo.service.CompetenceService;
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
public class CompetenceController {

    private final CompetenceService competenceService;

    @GetMapping("/competences")
    public String getTablePage() {
        return "competences";
    }

    @GetMapping("/competences-data")
    public ResponseEntity<Map<String, Object>> getEntityData(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        List<Competence> competences = competenceService.getAll();
        response.put("data", competences);

        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        response.put("role", role);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/competence/get-active/{entityId}")
    public ResponseEntity<Map<String, Object>> getActiveEntity(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        Competence competence = competenceService.getById(entityId);
        response.put("data", competence);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/competence/update")
    public ResponseEntity<Map<String, Object>> updateRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();

        String index = payload.get("0");
        String essence = payload.get("1");
        Integer dataId = Integer.parseInt(payload.get("dataId"));

        Competence competence = competenceService.getById(dataId);
        if (competence == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        competence.setIndex(index);
        competence.setEssence(essence);
        competence.setDisabled(false);
        competenceService.save(competence);

        response.put("updatedData", competence);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/competence/save-new-record")
    public ResponseEntity<Map<String, Object>> createRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();

        String index = payload.get("0");
        String essence = payload.get("1");

        Competence competence = new Competence();
        competence.setIndex(index);
        competence.setEssence(essence);
        competence.setDisabled(false);
        competenceService.save(competence);

        response.put("createdData", competence);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/competence/delete-record/{entityId}")
    public ResponseEntity<Map<String, Object>> deleteRecord(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        Competence competence = competenceService.getById(entityId);
        if (competence == null) {
            response.put("error", "Запись не найдена");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        competence.setDisabled(true);
        competenceService.save(competence);

        response.put("deletedData", competence.getId());
        return ResponseEntity.ok(response);
    }
}
