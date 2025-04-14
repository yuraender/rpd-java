package com.example.demo.controller;

import com.example.demo.entity.Department;
import com.example.demo.entity.Direction;
import com.example.demo.service.DepartmentService;
import com.example.demo.service.DirectionService;
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
public class DirectionController {

    private final DirectionService directionService;
    private final DepartmentService departmentService;

    @GetMapping("/directions")
    public String getTablePage() {
        return "directions";
    }

    @GetMapping("/api/direction/set-active/{entityId}")
    public ResponseEntity<Map<String, Object>> setActive(@PathVariable Integer entityId, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        Direction direction = directionService.getById(entityId);
        if (direction == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        response.put("dataName", direction.getName());

        HttpSession session = request.getSession();
        session.setAttribute("directionId", entityId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/directions-data")
    public ResponseEntity<Map<String, Object>> getEntityData(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        List<Direction> directions = directionService.getAll();
        response.put("data", directions);

        List<Department> departments = departmentService.getAll();
        response.put("departments", departments);

        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        response.put("role", role);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/direction/get-active/{entityId}")
    public ResponseEntity<Map<String, Object>> getActiveEntity(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        Direction direction = directionService.getById(entityId);
        response.put("data", direction);

        List<Department> departments = departmentService.getAll();
        response.put("departments", departments);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/direction/update")
    public ResponseEntity<Map<String, Object>> updateRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();

        String code = payload.get("0");
        String name = payload.get("1");
        int param2;
        try {
            param2 = Integer.parseInt(payload.get("2"));
        } catch (NumberFormatException ex) {
            response.put("error", "Неверный формат данных.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        Integer dataId = Integer.parseInt(payload.get("dataId"));

        Direction direction = directionService.getById(dataId);
        Department department = departmentService.getById(param2);
        if (direction == null || department == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if (directionService.existsByCodeOrName(direction.getId(), code, name)) {
            response.put("error", "Запись уже существует.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        direction.setCode(code);
        direction.setName(name);
        direction.setDepartment(department);
        direction.setDisabled(false);
        directionService.save(direction);

        response.put("updatedData", direction);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/direction/save-new-record")
    public ResponseEntity<Map<String, Object>> createRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();

        String code = payload.get("0");
        String name = payload.get("1");
        int param2;
        try {
            param2 = Integer.parseInt(payload.get("2"));
        } catch (NumberFormatException ex) {
            response.put("error", "Неверный формат данных.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if (directionService.existsByCodeOrName(null, code, name)) {
            response.put("error", "Запись уже существует.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        Department department = departmentService.getById(param2);
        if (department == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Direction direction = new Direction();
        direction.setCode(code);
        direction.setName(name);
        direction.setDepartment(department);
        direction.setDisabled(false);
        directionService.save(direction);

        response.put("createdData", direction);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/direction/delete-record/{entityId}")
    public ResponseEntity<Map<String, Object>> deleteRecord(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        Direction direction = directionService.getById(entityId);
        if (direction == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        direction.setDisabled(true);
        directionService.save(direction);

        response.put("deletedData", direction.getId());
        return ResponseEntity.ok(response);
    }
}
