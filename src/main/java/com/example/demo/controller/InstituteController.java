package com.example.demo.controller;

import com.example.demo.entity.Employee;
import com.example.demo.entity.EmployeePosition;
import com.example.demo.entity.Institute;
import com.example.demo.service.EmployeeService;
import com.example.demo.service.InstituteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

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

    @GetMapping("/choose-institute")
    public String chooseInstitute(Model model, @RequestParam(value = "keyword", required = false) String keyword) {
        List<Institute> institutes = instituteService.filterInstitutes(keyword);
        model.addAttribute("institutes", institutes);
        model.addAttribute("keyword", keyword);
        if (institutes.isEmpty()) {
            model.addAttribute("message", "В БД пока нет записей");
        }
        return "choose-institute";
    }

    @GetMapping("/select-institute")
    public String selectInstitute(@RequestParam("id") Long id, HttpSession session) {
        session.setAttribute("instituteId", id);
        return "redirect:/home";
    }


    @PostMapping("/api/institute/update")
    public ResponseEntity<Map<String, Object>> updateRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();
        String param0 = payload.get("0");
        Long param1 = Long.valueOf(payload.get("1"));
        String param2 = payload.get("2");
        String param3 = payload.get("3");
        String param4 = payload.get("4");
        Long dataId = Long.valueOf(payload.get("dataId"));

        Institute entity = instituteService.findById(dataId);

        Employee employee = employeeService.getById(param1);

        if (entity == null) {
            response.put("error", "Запись не найдена. Запись не обновлена.");
            return ResponseEntity.ok(response);
        }

        entity.setName(param0);
        entity.setDirector(employee);
        entity.setCity(param2);
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
        Long param1 = Long.valueOf(payload.get("1"));
        String param2 = payload.get("2");
        String param3 = payload.get("3");
        String param4 = payload.get("4");

        Employee employee = employeeService.getById(param1);

        Institute entity = new Institute();
        entity.setName(param0);
        entity.setDirector(employee);
        entity.setCity(param2);
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