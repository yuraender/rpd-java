package com.example.demo.controller;

import com.example.demo.entity.Department;
import com.example.demo.entity.Direction;
import com.example.demo.service.DepartmentService;
import com.example.demo.service.DirectionService;
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
public class DirectionController {

    @Autowired
    private DirectionService directionService;

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/directions")
    public String getDepartmentPage(Model model) {
        return "directions";
    }

    @GetMapping("/directions-data-set-active/{entityId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> setActive(@PathVariable Integer entityId, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        Direction direction = directionService.getById(entityId);
        if (direction == null) {
            response.put("error", "Запись не найдена");
            return ResponseEntity.ok(response);
        }
        response.put("dataName", direction.getName());

        HttpSession session = request.getSession();
        session.setAttribute("directionId", entityId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/directions-data")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getEntityData(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        HttpSession session = request.getSession();
        List<Direction> directions = directionService.findAllByDisabledFalse();
        response.put("data", directions);
        List<Department> departments = departmentService.getAll();
        response.put("departments", departments);
        String role = (String) session.getAttribute("role");
        response.put("role", role);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/direction/get-active/{entityId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getActiveEntity(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();
        Direction direction = directionService.getById(entityId);
        response.put("data", direction);

        List<Department> departmentList = departmentService.getAll();
        response.put("departmentList", departmentList);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/direction/update")
    public ResponseEntity<Map<String, Object>> updateRecord(@RequestBody Map<String, String> payload, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        String param1 = payload.get("0");
        String param2 = payload.get("1");
        Integer param3 = Integer.parseInt(payload.get("2"));
        Integer dataId = Integer.parseInt(payload.get("dataId"));

        Direction entity = directionService.getById(dataId);
        Department department = departmentService.getById(param3);

        if (entity == null || department == null) {
            response.put("error", "Запись не найдена. Запись не обновлена.");
            return ResponseEntity.ok(response);
        }
        // Обновляем поле audience у Department
        entity.setEncryption(param1);
        entity.setName(param2);
        entity.setDepartment(department);
        entity.setDisabled(false);
        // Сохраняем обновленную запись
        directionService.save(entity);
        // Добавляем обновленную запись в ответ
        response.put("updatedData", entity);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/direction/save-new-record")
    public ResponseEntity<Map<String, Object>> createRecord(@RequestBody Map<String, String> payload, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        String param1 = payload.get("0");
        String param2 = payload.get("1");
        Integer param3 = Integer.parseInt(payload.get("2"));
        Department department = departmentService.getById(param3);

        if (department == null) {
            response.put("error", "Запись не найдена. Запись не обновлена.");
            return ResponseEntity.ok(response);
        }

        Direction entity = new Direction();
        entity.setName(param2);
        entity.setDepartment(department);
        entity.setEncryption(param1);
        entity.setDisabled(false);
        // Сохраняем обновленную запись
        directionService.save(entity);
        // Добавляем обновленную запись в ответ
        response.put("createdData", entity);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/api/direction/delete-record/{entityId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteRecord(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        // Получаем запись TechSupport по techSupportId
        Direction entity = directionService.getById(entityId);
        if (entity == null) {
            response.put("error", "Запись не найдена");
            return ResponseEntity.ok(response);
        }
        entity.setDisabled(true);
        directionService.save(entity);
        response.put("deletedData", entity.getId());
        return ResponseEntity.ok(response);
    }
}
