package com.example.demo.controller;

import com.example.demo.entity.Competencie;
import com.example.demo.entity.Indicator;
import com.example.demo.entity.Protocol;
import com.example.demo.service.CompetencieService;
import com.example.demo.service.IndicatorService;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class IndicatorController {

    private final IndicatorService indicatorService;
    private final CompetencieService competencieService;

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

        List<Competencie> competencies = competencieService.getAll();
        response.put("competencies", competencies);

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
        indicator.setText(text);
        indicator.setDisabled(false);
        indicatorService.save(indicator);

        response.put("updatedData", indicator);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/indicator/save-new-record")
    public ResponseEntity<Map<String, Object>> createRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();

        String text = payload.get("0");
        int param1, param2;
        try {
            param1 = Integer.parseInt(payload.get("1"));
            param2 = Integer.parseInt(payload.get("2"));
        } catch (IllegalArgumentException ex) {
            response.put("error", "Неверный формат данных.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Competencie competencie = competencieService.getById(param2);
        if (param1 < 0 || param1 >= Protocol.Type.values().length || competencie == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Indicator indicator = new Indicator();
        indicator.setText(text);
        indicator.setType(Indicator.Type.values()[param1]);
        indicator.setCompetencie(competencie);
        indicator.setDisabled(false);
        indicatorService.save(indicator);

        response.put("createdData", indicator);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/indicator/delete-record/{entityId}")
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
