package com.example.demo.controller;

import com.example.demo.entity.BasicEducationalProgram;
import com.example.demo.entity.Competence;
import com.example.demo.entity.Indicator;
import com.example.demo.entity.Protocol;
import com.example.demo.service.BasicEducationalProgramService;
import com.example.demo.service.CompetenceService;
import com.example.demo.service.IndicatorService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class IndicatorController {

    private final IndicatorService indicatorService;
    private final BasicEducationalProgramService basicEducationalProgramService;
    private final CompetenceService competenceService;

    @GetMapping("/indicators")
    public String getTablePage() {
        return "indicators";
    }

    @GetMapping("/indicators-data")
    public ResponseEntity<Map<String, Object>> getEntityData(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        List<Indicator> indicators = indicatorService.getAll();
        response.put("data", indicators);

        List<Indicator.Type> types = Arrays.stream(Indicator.Type.values()).toList();
        response.put("types", types);

        List<BasicEducationalProgram> beps = basicEducationalProgramService.getAll();
        response.put("beps", beps);

        List<Competence> competences = competenceService.getAll();
        response.put("competences", competences);

        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        response.put("role", role);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/indicator/get-active/{entityId}")
    public ResponseEntity<Map<String, Object>> getActiveEntity(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        Indicator indicator = indicatorService.getById(entityId);
        response.put("data", indicator);

        List<Indicator.Type> types = Arrays.stream(Indicator.Type.values()).toList();
        response.put("types", types);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/indicator/update")
    public ResponseEntity<Map<String, Object>> updateRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();

        String text = payload.get("0");
        Integer dataId = Integer.parseInt(payload.get("dataId"));

        Indicator indicator = indicatorService.getById(dataId);
        if (indicator == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if (indicatorService.existsByText(indicator.getId(), text)) {
            response.put("error", "Запись уже существует.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        indicator.setText(text);
        indicator.setDisabled(false);
        indicatorService.save(indicator);

        response.put("updatedData", indicator);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/api/indicator/save-new-record")
    public ResponseEntity<Map<String, Object>> createRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();

        String text = payload.get("0");
        int param1, param3;
        try {
            param1 = Integer.parseInt(payload.get("1"));
            param3 = Integer.parseInt(payload.get("3"));
        } catch (IllegalArgumentException ex) {
            response.put("error", "Неверный формат данных.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if (indicatorService.existsByText(null, text)) {
            response.put("error", "Запись уже существует.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        Competence competence = competenceService.getById(param3);
        if (param1 < 0 || param1 >= Protocol.Type.values().length || competence == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Indicator indicator = new Indicator();
        indicator.setText(text);
        indicator.setType(Indicator.Type.values()[param1]);
        indicator.setCompetence(competence);
        indicator.setDisabled(false);
        indicatorService.save(indicator);

        response.put("createdData", indicator);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/api/indicator/delete-record/{entityId}")
    public ResponseEntity<Map<String, Object>> deleteRecord(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        Indicator indicator = indicatorService.getById(entityId);
        if (indicator == null) {
            response.put("error", "Запись не найдена");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        indicator.setDisabled(true);
        indicatorService.save(indicator);

        response.put("deletedData", indicator.getId());
        return ResponseEntity.ok(response);
    }
}
