package com.example.demo.controller;

import com.example.demo.entity.Protocol;
import com.example.demo.entity.Teacher;
import com.example.demo.service.ProtocolService;
import com.example.demo.service.TeacherService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.Instant;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class ProtocolController {

    private final ProtocolService protocolService;
    private final TeacherService teacherService;

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

        List<Teacher> teachers = teacherService.getAll();
        response.put("teachers", teachers);

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

        List<Teacher> teachers = teacherService.getAll();
        response.put("teachers", teachers);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/protocol/update")
    public ResponseEntity<Map<String, Object>> updateRecord(@RequestBody Map<String, Object> payload) {
        Map<String, Object> response = new HashMap<>();

        int number1, number2;
        Date date1, date2;
        int[] param4;
        try {
            number1 = Integer.parseInt((String) payload.get("0"));
            date1 = Date.valueOf((String) payload.get("1"));
            number2 = Integer.parseInt((String) payload.get("2"));
            date2 = Date.valueOf((String) payload.get("3"));
            param4 = ((List<String>) payload.get("4")).stream()
                    .mapToInt(Integer::parseInt)
                    .toArray();
        } catch (IllegalArgumentException ex) {
            response.put("error", "Неверный формат данных.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        Integer dataId = Integer.parseInt((String) payload.get("dataId"));

        java.util.Date before = Date.valueOf("2000-01-01"), after = Date.from(Instant.now());
        if (date1.before(before) || date1.after(after) || date2.before(before) || date2.after(after)) {
            response.put("error", "Протокол не должен быть датирован ранее 01.01.2000 и позднее текущего дня.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Protocol protocol = protocolService.getById(dataId);
        List<Teacher> developers = Arrays.stream(param4)
                .mapToObj(teacherService::getById)
                .distinct()
                .toList();
        if (protocol == null || developers.stream().anyMatch(Objects::isNull)) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        protocol.setNumber1(number1);
        protocol.setDate1(date1);
        protocol.setNumber2(number2);
        protocol.setDate2(date2);
        protocol.setDevelopers(new HashSet<>(developers));
        protocol.setDisabled(false);
        protocolService.save(protocol);

        response.put("updatedData", protocol);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/api/protocol/save-new-record")
    public ResponseEntity<Map<String, Object>> createRecord(@RequestBody Map<String, Object> payload) {
        Map<String, Object> response = new HashMap<>();

        int number1, number2, param4;
        Date date1, date2;
        int[] param5;
        try {
            number1 = Integer.parseInt((String) payload.get("0"));
            date1 = Date.valueOf((String) payload.get("1"));
            number2 = Integer.parseInt((String) payload.get("2"));
            date2 = Date.valueOf((String) payload.get("3"));
            param4 = Integer.parseInt((String) payload.get("4"));
            param5 = ((List<String>) payload.get("5")).stream()
                    .mapToInt(Integer::parseInt)
                    .toArray();
        } catch (IllegalArgumentException ex) {
            response.put("error", "Неверный формат данных.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        java.util.Date before = Date.valueOf("2000-01-01"), after = Date.from(Instant.now());
        if (date1.before(before) || date1.after(after) || date2.before(before) || date2.after(after)) {
            response.put("error", "Протокол не должен быть датирован ранее 01.01.2000 и позднее текущего дня.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        List<Teacher> developers = Arrays.stream(param5)
                .mapToObj(teacherService::getById)
                .distinct()
                .toList();
        if (param4 < 0 || param4 >= Protocol.Type.values().length || developers.stream().anyMatch(Objects::isNull)) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Protocol protocol = new Protocol();
        protocol.setNumber1(number1);
        protocol.setDate1(date1);
        protocol.setNumber2(number2);
        protocol.setDate2(date2);
        protocol.setType(Protocol.Type.values()[param4]);
        protocol.setDevelopers(new HashSet<>(developers));
        protocol.setDisabled(false);
        protocolService.save(protocol);

        response.put("createdData", protocol);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/api/protocol/delete-record/{entityId}")
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
