package com.example.demo.controller;

import com.example.demo.entity.Employee;
import com.example.demo.entity.Institute;
import com.example.demo.service.EmployeeService;
import com.example.demo.service.InstituteService;
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
@SessionAttributes("instituteId")
public class InstituteController {

    @Autowired
    private InstituteService instituteService;
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/institutes")
    public String getTablePage(Model model) {
        return "institutes";
    }

    @GetMapping("/institutes-data")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getEntityData(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        HttpSession session = request.getSession();

        List<Institute> entity = instituteService.getAllInstitutes();
        response.put("data", entity);

        List<Employee> entity1 = employeeService.getAllEmployees();
        response.put("entity1", entity1);

        String role = (String) session.getAttribute("role");
        response.put("role", role);

        Long instituteId = (Long) session.getAttribute("instituteId");
        response.put("instituteId", instituteId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/institute/get-active/{entityId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getActiveEntity(@PathVariable Long entityId) {
        Map<String, Object> response = new HashMap<>();
        Institute entity = instituteService.findById(entityId);
        List<Employee> entity1 = employeeService.getAllEmployees();
        response.put("entity1", entity1);
        response.put("data", entity);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/select-institute/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> setInstitute(@PathVariable Long id, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        session.setAttribute("instituteId", id);
        response.put("id", id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/institute/update")
    public ResponseEntity<Map<String, Object>> updateRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();
        String param0 = payload.get("0");
        String param1 = payload.get("1");
        Long param2 = Long.valueOf(payload.get("2"));
        String param3 = payload.get("3");
        String param4 = payload.get("4");
        Long dataId = Long.valueOf(payload.get("dataId"));

        Institute entity = instituteService.findById(dataId);

        Employee employee = employeeService.getById(param2);

        if (entity == null) {
            response.put("error", "Запись не найдена. Запись не обновлена.");
            return ResponseEntity.ok(response);
        }

        entity.setName(param0);
        entity.setDirector(employee);
        entity.setCity(param1);
        entity.setApprovalText(param3);
        entity.setFooterText(param4);
        entity.setDisabled(false);
        // Сохраняем обновленную запись
        instituteService.save(entity);
        // Добавляем обновленную запись в ответ
        response.put("updatedData", entity);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/institute/save-new-record")
    public ResponseEntity<Map<String, Object>> createRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();
        String param0 = payload.get("0");
        String param1 = payload.get("1");
        Long param2 = Long.valueOf(payload.get("2"));
        String param3 = payload.get("2");
        String param4 = payload.get("3");

        Employee employee = employeeService.getById(param2);

        Institute entity = new Institute();
        entity.setName(param0);
        entity.setCity(param1);
        entity.setDirector(employee);
        entity.setApprovalText(param3);
        entity.setFooterText(param4);
        entity.setDisabled(false);

        // Сохраняем обновленную запись
        instituteService.save(entity);
        // Добавляем обновленную запись в ответ
        response.put("createdData", entity);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/institute/delete-record/{entityId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteRecord(@PathVariable Long entityId) {
        Map<String, Object> response = new HashMap<>();

        // Получаем запись TechSupport по techSupportId
        Institute entity = instituteService.findById(entityId);
        if (entity == null) {
            response.put("error", "Запись не найдена");
            return ResponseEntity.ok(response);
        }
        entity.setDisabled(true);
        instituteService.save(entity);
        response.put("deletedData", entity.getId());
        return ResponseEntity.ok(response);
    }
}
