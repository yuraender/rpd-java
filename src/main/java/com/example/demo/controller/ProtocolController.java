package com.example.demo.controller;

import com.example.demo.entity.Protocol;
import com.example.demo.service.ProtocolService;
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

import java.sql.Date;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ProtocolController {

    private final ProtocolService protocolService;

    @GetMapping("/protocols")
    public String getTablePage() {
        return "protocols";
    }

    @GetMapping("/protocols-data")
    public ResponseEntity<Map<String, Object>> getEntityData(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        List<Protocol> protocols = protocolService.getAll();
        response.put("data", protocols);

        List<Protocol.Type> types = Arrays.stream(Protocol.Type.values()).toList();
        response.put("types", types);

        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        response.put("role", role);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/protocol/get-active/{entityId}")
    public ResponseEntity<Map<String, Object>> getActiveEntity(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        Protocol protocol = protocolService.getById(entityId);
        response.put("data", protocol);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/protocol/update")
    public ResponseEntity<Map<String, Object>> updateRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();

        int number;
        Date date;
        try {
            number = Integer.parseInt(payload.get("0"));
            date = Date.valueOf(payload.get("1"));
        } catch (IllegalArgumentException ex) {
            response.put("error", "Неверный формат данных.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        Integer dataId = Integer.parseInt(payload.get("dataId"));

        if (date.before(Date.valueOf("2000-01-01")) || date.after(Date.from(Instant.now()))) {
            response.put("error", "Протокол не должен быть датирован ранее 01.01.2000 и позже текущего дня.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Protocol protocol = protocolService.getById(dataId);
        if (protocol == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        protocol.setNumberProtocol(number);
        protocol.setDate(date);
        protocol.setDisabled(false);
        protocolService.save(protocol);

        response.put("updatedData", protocol);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/protocol/save-new-record")
    public ResponseEntity<Map<String, Object>> createRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();

        int number, param2;
        Date date;
        try {
            number = Integer.parseInt(payload.get("0"));
            date = Date.valueOf(payload.get("1"));
            param2 = Integer.parseInt(payload.get("2"));
        } catch (IllegalArgumentException ex) {
            response.put("error", "Неверный формат данных.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if (date.before(Date.valueOf("2000-01-01")) || date.after(Date.from(Instant.now()))) {
            response.put("error", "Протокол не должен быть датирован ранее 01.01.2000 и позже текущего дня.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        if (param2 < 0 || param2 >= Protocol.Type.values().length) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Protocol protocol = new Protocol();
        protocol.setNumberProtocol(number);
        protocol.setDate(date);
        protocol.setType(Protocol.Type.values()[param2]);
        protocol.setDisabled(false);
        protocolService.save(protocol);

        response.put("createdData", protocol);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/protocol/delete-record/{entityId}")
    public ResponseEntity<Map<String, Object>> deleteRecord(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        Protocol protocol = protocolService.getById(entityId);
        if (protocol == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        protocol.setDisabled(true);
        protocolService.save(protocol);

        response.put("deletedData", protocol.getId());
        return ResponseEntity.ok(response);
    }
}
