package com.example.demo.controller;

import com.example.demo.entity.Department;
import com.example.demo.entity.Employee;
import com.example.demo.service.DepartmentService;
import com.example.demo.service.EmployeeService;
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
public class DepartmentController {

    private final DepartmentService departmentService;
    private final EmployeeService employeeService;

    @GetMapping("/departments")
    public String getTablePage() {
        return "departments";
    }

    @PostMapping("/api/department/set-active/{entityId}")
    public ResponseEntity<Map<String, Object>> setActive(@PathVariable Integer entityId, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        Department department = departmentService.getById(entityId);
        if (department == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        response.put("dataName", department.getName());

        HttpSession session = request.getSession();
        session.setAttribute("departmentId", entityId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/departments-data")
    public ResponseEntity<Map<String, Object>> getEntityData(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        List<Department> departments = departmentService.getAll();
        response.put("data", departments);

        List<Employee> employees = employeeService.getAll();
        response.put("employees", employees);

        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        response.put("role", role);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/department/get-active/{entityId}")
    public ResponseEntity<Map<String, Object>> getActiveEntity(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        Department department = departmentService.getById(entityId);
        response.put("data", department);

        List<Employee> employees = employeeService.getAll();
        response.put("employees", employees);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/department/update")
    public ResponseEntity<Map<String, Object>> updateRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();

        String code = payload.get("0");
        String name = payload.get("1");
        String abbreviation = payload.get("2");
        int param3;
        try {
            param3 = Integer.parseInt(payload.get("3"));
        } catch (NumberFormatException ex) {
            response.put("error", "Неверный формат данных.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        Integer dataId = Integer.parseInt(payload.get("dataId"));

        Department department = departmentService.getById(dataId);
        Employee head = employeeService.getById(param3);
        if (department == null || head == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if (departmentService.existsByCodeOrName(department.getId(), code, name)) {
            response.put("error", "Запись уже существует.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        department.setCode(code);
        department.setName(name);
        department.setAbbreviation(abbreviation);
        department.setHead(head);
        department.setDisabled(false);
        departmentService.save(department);

        response.put("updatedData", department);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/api/department/save-new-record")
    public ResponseEntity<Map<String, Object>> createRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();

        String code = payload.get("0");
        String name = payload.get("1");
        String abbreviation = payload.get("2");
        int param3;
        try {
            param3 = Integer.parseInt(payload.get("3"));
        } catch (NumberFormatException ex) {
            response.put("error", "Неверный формат данных.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if (departmentService.existsByCodeOrName(null, code, name)) {
            response.put("error", "Запись уже существует.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        Employee head = employeeService.getById(param3);
        if (head == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Department department = new Department();
        department.setCode(code);
        department.setName(name);
        department.setAbbreviation(abbreviation);
        department.setHead(head);
        department.setDisabled(false);
        departmentService.save(department);

        response.put("createdData", department);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/api/department/delete-record/{entityId}")
    public ResponseEntity<Map<String, Object>> deleteRecord(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        Department department = departmentService.getById(entityId);
        if (department == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        department.setDisabled(true);
        departmentService.save(department);

        response.put("deletedData", department.getId());
        return ResponseEntity.ok(response);
    }
}
