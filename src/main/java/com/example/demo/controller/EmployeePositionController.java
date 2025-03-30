package com.example.demo.controller;

import com.example.demo.entity.EmployeePosition;
import com.example.demo.service.EmployeePositionService;
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
public class EmployeePositionController {

    private final EmployeePositionService employeePositionService;

    @GetMapping("/employee-positions")
    public String getTablePage() {
        return "employee-positions";
    }

    @GetMapping("/employee-positions-data")
    public ResponseEntity<Map<String, Object>> getEntityData(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        List<EmployeePosition> employeePositions = employeePositionService.getAll();
        response.put("data", employeePositions);

        List<EmployeePosition.Type> types = Arrays.stream(EmployeePosition.Type.values()).toList();
        response.put("types", types);

        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        response.put("role", role);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/employee-position/get-active/{entityId}")
    public ResponseEntity<Map<String, Object>> getActiveEntity(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        EmployeePosition employeePosition = employeePositionService.getById(entityId);
        response.put("data", employeePosition);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/employee-position/update")
    public ResponseEntity<Map<String, Object>> updateRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();

        String name = payload.get("0");
        Integer dataId = Integer.parseInt(payload.get("dataId"));

        EmployeePosition employeePosition = employeePositionService.getById(dataId);
        if (employeePosition == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        employeePosition.setName(name);
        employeePosition.setDisabled(false);
        employeePositionService.save(employeePosition);

        response.put("updatedData", employeePosition);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/employee-position/save-new-record")
    public ResponseEntity<Map<String, Object>> createRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();

        String name = payload.get("0");
        int param1;
        try {
            param1 = Integer.parseInt(payload.get("1"));
        } catch (IllegalArgumentException ex) {
            response.put("error", "Неверный формат данных.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if (param1 < 0 || param1 >= EmployeePosition.Type.values().length) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        EmployeePosition employeePosition = new EmployeePosition();
        employeePosition.setName(name);
        employeePosition.setType(EmployeePosition.Type.values()[param1]);
        employeePosition.setDisabled(false);
        employeePositionService.save(employeePosition);

        response.put("createdData", employeePosition);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/employee-position/delete-record/{entityId}")
    public ResponseEntity<Map<String, Object>> deleteRecord(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        EmployeePosition employeePosition = employeePositionService.getById(entityId);
        if (employeePosition == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        employeePosition.setDisabled(true);
        employeePositionService.save(employeePosition);

        response.put("deletedData", employeePosition.getId());
        return ResponseEntity.ok(response);
    }
}
