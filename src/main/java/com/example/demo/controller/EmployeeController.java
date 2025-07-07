package com.example.demo.controller;

import com.example.demo.entity.Employee;
import com.example.demo.entity.EmployeePosition;
import com.example.demo.service.EmployeePositionService;
import com.example.demo.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeePositionService employeePositionService;

    @GetMapping("/employees")
    public String getTablePage() {
        return "employees";
    }

    @GetMapping("/employees-data")
    public ResponseEntity<Map<String, Object>> getEntityData(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        List<Employee> employees = employeeService.getAll();
        response.put("data", employees);

        List<EmployeePosition> employeePositions
                = employeePositionService.getAllByType(EmployeePosition.Type.ADMINISTRATIVE);
        response.put("employeePositions", employeePositions);

        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        response.put("role", role);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/employee/get-active/{entityId}")
    public ResponseEntity<Map<String, Object>> getActiveEntity(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        Employee employee = employeeService.getById(entityId);
        response.put("data", employee);

        List<EmployeePosition> employeePositions
                = employeePositionService.getAllByType(EmployeePosition.Type.ADMINISTRATIVE);
        response.put("employeePositions", employeePositions);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("@userDetailsServiceImpl.isAdmin(principal)")
    @PostMapping("/api/employee/update")
    public ResponseEntity<Map<String, Object>> updateRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();

        String lastName = payload.get("0");
        String firstName = payload.get("1");
        String middleName = payload.get("2");
        int param3;
        try {
            param3 = Integer.parseInt(payload.get("3"));
        } catch (NumberFormatException ex) {
            response.put("error", "Неверный формат данных.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        Integer dataId = Integer.parseInt(payload.get("dataId"));

        Employee employee = employeeService.getById(dataId);
        EmployeePosition employeePosition = employeePositionService
                .getByIdAndType(param3, EmployeePosition.Type.ADMINISTRATIVE);
        if (employee == null || employeePosition == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        employee.setLastName(lastName);
        employee.setFirstName(firstName);
        employee.setMiddleName(middleName);
        employee.setEmployeePosition(employeePosition);
        employee.setDisabled(false);
        employeeService.save(employee);

        response.put("updatedData", employee);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("@userDetailsServiceImpl.isAdmin(principal)")
    @PutMapping("/api/employee/save-new-record")
    public ResponseEntity<Map<String, Object>> createRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();

        String lastName = payload.get("0");
        String firstName = payload.get("1");
        String middleName = payload.get("2");
        int param3;
        try {
            param3 = Integer.parseInt(payload.get("3"));
        } catch (NumberFormatException ex) {
            response.put("error", "Неверный формат данных.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        EmployeePosition employeePosition = employeePositionService
                .getByIdAndType(param3, EmployeePosition.Type.ADMINISTRATIVE);
        if (employeePosition == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Employee employee = new Employee();
        employee.setLastName(lastName);
        employee.setFirstName(firstName);
        employee.setMiddleName(middleName);
        employee.setEmployeePosition(employeePosition);
        employee.setDisabled(false);
        employeeService.save(employee);

        response.put("createdData", employee);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("@userDetailsServiceImpl.isAdmin(principal)")
    @DeleteMapping("/api/employee/delete-record/{entityId}")
    public ResponseEntity<Map<String, Object>> deleteRecord(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        Employee employee = employeeService.getById(entityId);
        if (employee == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        employee.setDisabled(true);
        employeeService.save(employee);

        response.put("deletedData", employee.getId());
        return ResponseEntity.ok(response);
    }
}
