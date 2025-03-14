package com.example.demo.controller;

import com.example.demo.entity.EmployeePosition;
import com.example.demo.service.EducationTypeService;
import com.example.demo.service.EmployeePositionService;
import com.example.demo.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class EmployeePositionController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EducationTypeService educationTypeService;
    @Autowired
    private EmployeePositionService employeePositionService;

    @GetMapping("/employee-positions")
    public String getTablePage(Model model) {
        return "employee-positions";
    }

    @GetMapping("/employee-positions-data")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getEntityData(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        HttpSession session = request.getSession();

        List<EmployeePosition> entity = employeePositionService.getAll();
        response.put("data", entity);

        String role = (String) session.getAttribute("role");
        response.put("role", role);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/employee-position/get-active/{entityId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getActiveEntity(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();
        EmployeePosition entity = employeePositionService.getById(entityId);
        response.put("data", entity);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/employee-position/update")
    public ResponseEntity<Map<String, Object>> updateRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();
        String param0 = payload.get("0");
        Integer dataId = Integer.parseInt(payload.get("dataId"));

        EmployeePosition entity = employeePositionService.getById(dataId);

        if (entity == null) {
            response.put("error", "Запись не найдена. Запись не обновлена.");
            return ResponseEntity.ok(response);
        }
        // Обновляем поле audience у Department
        entity.setPositionName(param0);
        entity.setDisabled(false);
        // Сохраняем обновленную запись
        employeePositionService.save(entity);
        // Добавляем обновленную запись в ответ
        response.put("updatedData", entity);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/employee-position/save-new-record")
    public ResponseEntity<Map<String, Object>> createRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();
        String param0 = payload.get("0");

        EmployeePosition entity = new EmployeePosition();
        entity.setPositionName(param0);
        entity.setDisabled(false);
        // Сохраняем обновленную запись
        employeePositionService.save(entity);
        // Добавляем обновленную запись в ответ
        response.put("createdData", entity);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/employee-position/delete-record/{entityId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteRecord(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        // Получаем запись TechSupport по techSupportId
        EmployeePosition entity = employeePositionService.getById(entityId);
        if (entity == null) {
            response.put("error", "Запись не найдена");
            return ResponseEntity.ok(response);
        }
        entity.setDisabled(true);
        employeePositionService.save(entity);
        response.put("deletedData", entity.getId());
        return ResponseEntity.ok(response);
    }
}
