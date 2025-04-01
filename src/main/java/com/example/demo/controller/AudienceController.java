package com.example.demo.controller;

import com.example.demo.entity.Audience;
import com.example.demo.service.AudienceService;
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
public class AudienceController {

    private final AudienceService audienceService;

    @GetMapping("/audiences")
    public String getTablePage() {
        return "audiences";
    }

    @GetMapping("/api/audience/set-active/{entityId}")
    public ResponseEntity<Map<String, Object>> setActive(@PathVariable Integer entityId, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        Audience audience = audienceService.getById(entityId);
        if (audience == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        response.put("dataName", audience.getAudienceNumber());

        HttpSession session = request.getSession();
        session.setAttribute("audienceId", entityId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/audiences-data")
    public ResponseEntity<Map<String, Object>> getEntityData(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        List<Audience> audiences = audienceService.getAll();
        response.put("data", audiences);

        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        response.put("role", role);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/audience/get-active/{entityId}")
    public ResponseEntity<Map<String, Object>> getActiveEntity(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        Audience audience = audienceService.getById(entityId);
        response.put("data", audience);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/audience/update")
    public ResponseEntity<Map<String, Object>> updateRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();

        String audienceNumber = payload.get("0");
        String equipment = payload.get("1");
        String software = payload.get("2");
        Integer dataId = Integer.parseInt(payload.get("dataId"));

        Audience audience = audienceService.getById(dataId);
        if (audience == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        audience.setAudienceNumber(audienceNumber);
        audience.setEquipment(equipment);
        audience.setSoftware(software);
        audience.setDisabled(false);
        audienceService.save(audience);

        response.put("updatedData", audience);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/audience/save-new-record")
    public ResponseEntity<Map<String, Object>> createRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();

        String audienceNumber = payload.get("0");
        String equipment = payload.get("1");
        String software = payload.get("2");

        Audience audience = new Audience();
        audience.setAudienceNumber(audienceNumber);
        audience.setEquipment(equipment);
        audience.setSoftware(software);
        audience.setDisabled(false);
        audienceService.save(audience);

        response.put("createdData", audience);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/audience/delete-record/{entityId}")
    public ResponseEntity<Map<String, Object>> deleteRecord(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        Audience audience = audienceService.getById(entityId);
        if (audience == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        audience.setDisabled(true);
        audienceService.save(audience);

        response.put("deletedData", audience.getId());
        return ResponseEntity.ok(response);
    }
}
