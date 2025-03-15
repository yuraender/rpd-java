package com.example.demo.controller;

import com.example.demo.entity.Audience;
import com.example.demo.service.*;
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
public class AudienceController {

    @Autowired
    private TechSupportService techSupportService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private AudienceService audienceService;

    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private DisciplineService disciplineService;

    @GetMapping("/audiences")
    public String getDepartmentPage(Model model) {
        return "audiences";
    }

    @GetMapping("/audiences-data-set-active/{entityId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> setActive(@PathVariable Integer entityId, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        Audience audience = audienceService.getById(entityId);
        if (audience == null) {
            response.put("error", "Запись не найдена");
            return ResponseEntity.ok(response);
        }
        response.put("dataName", audience.getNumberAudience());

        HttpSession session = request.getSession();
        session.setAttribute("audienceId", entityId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/audiences-data")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getEntityData(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        HttpSession session = request.getSession();
        List<Audience> audiences = audienceService.findAllByDisabledFalse();
        response.put("data", audiences);
        String role = (String) session.getAttribute("role");
        response.put("role", role);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/audience/get-active/{entityId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getActiveDepartment(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();
        Audience audience = audienceService.getById(entityId);
        response.put("data", audience);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/audience/update")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();
        String param0 = payload.get("0");
        String param1 = payload.get("1");
        String param2 = payload.get("2");
        Integer dataId = Integer.parseInt(payload.get("dataId"));

        if (param0.isEmpty() || param1.isEmpty() || param2.isEmpty()) {
            response.put("error", "Заполните все поля. Запись не обновлена.");
            return ResponseEntity.ok(response);
        }

        Audience entity = audienceService.getById(dataId);

        if (entity == null) {
            response.put("error", "Запись не найдена. Запись не обновлена.");
            return ResponseEntity.ok(response);
        }
        // Обновляем поле audience у Department
        entity.setNumberAudience(param0);
        entity.setTech(param1);
        entity.setSoftwareLicense(param2);
        entity.setDisabled(false);
        // Сохраняем обновленную запись
        audienceService.save(entity);
        // Добавляем обновленную запись в ответ
        response.put("updatedData", entity);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/audience/save-new-record")
    public ResponseEntity<Map<String, Object>> createRecord(@RequestBody Map<String, String> payload, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        String numberAudience = payload.get("0");
        String tech = payload.get("1");
        String softwareLicense = payload.get("2");

        Audience entity = new Audience();
        entity.setNumberAudience(numberAudience);
        entity.setTech(tech);
        entity.setSoftwareLicense(softwareLicense);
        entity.setDisabled(false);
        // Сохраняем обновленную запись
        audienceService.save(entity);
        // Добавляем обновленную запись в ответ
        response.put("createdData", entity);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/api/audience/delete-record/{entityId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteRecord(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        // Получаем запись TechSupport по techSupportId
        Audience audience = audienceService.getById(entityId);
        if (audience == null) {
            response.put("error", "Запись не найдена");
            return ResponseEntity.ok(response);
        }
        audience.setDisabled(true);
        audienceService.save(audience);
        response.put("deletedData", audience.getId());
        return ResponseEntity.ok(response);
    }
}
