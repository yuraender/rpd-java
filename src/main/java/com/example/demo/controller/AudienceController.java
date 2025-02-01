package com.example.demo.controller;

import com.example.demo.entity.Audience;
import com.example.demo.entity.Institute;
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
    @Autowired
    private InstituteService instituteService;

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
        Integer instituteIdFromSession = (Integer) session.getAttribute("instituteId");
        List<Audience> audiences = audienceService.findAllByDisabledFalseAndInstituteId(instituteIdFromSession);
        response.put("data", audiences);
        List<Institute> institutes = instituteService.getAllInstitutes();
        response.put("institutes", institutes);
        String role = (String) session.getAttribute("role");
        response.put("role", role);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/audiences/get-active/{audiencesId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getActiveDepartment(@PathVariable Integer audiencesId) {
        Map<String, Object> response = new HashMap<>();
        Audience audience = audienceService.getById(audiencesId);
        response.put("data", audience);

        List<Institute> instituteList = instituteService.getAllInstitutes();
        response.put("instituteList", instituteList);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/audience-support/update/{entityId}/{instituteId}/{numberAudience}/{tech}/{softwareLicense}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateRecord(
            @PathVariable Integer instituteId,
            @PathVariable Integer entityId,
            @PathVariable String numberAudience,
            @PathVariable String tech,
            @PathVariable String softwareLicense) {
        Map<String, Object> response = new HashMap<>();

        if (numberAudience.isEmpty() || tech.isEmpty() || softwareLicense.isEmpty()) {
            response.put("error", "Заполните все поля. Запись не обновлена.");
            return ResponseEntity.ok(response);
        }

        Audience entity = audienceService.getById(entityId);
        Institute institute = instituteService.findById(instituteId);

        if (entity == null || institute == null) {
            response.put("error", "Запись не найдена. Запись не обновлена.");
            return ResponseEntity.ok(response);
        }
        // Обновляем поле audience у Department
        entity.setNumberAudience(numberAudience);
        entity.setInstitute(institute);
        entity.setTech(tech);
        entity.setSoftwareLicense(softwareLicense);
        entity.setDisabled(false);
        // Сохраняем обновленную запись
        audienceService.save(entity);
        // Добавляем обновленную запись в ответ
        response.put("updatedData", entity);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/audience-support/save-new-record")
    public ResponseEntity<Map<String, Object>> createRecord(@RequestBody Map<String, String> payload, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        Integer instituteId = Integer.parseInt(payload.get("0"));
        String numberAudience = payload.get("1");
        String tech = payload.get("2");
        String softwareLicense = payload.get("3");
        Institute institute = instituteService.findById(instituteId);

        if (institute == null) {
            response.put("error", "Запись не найдена. Запись не обновлена.");
            return ResponseEntity.ok(response);
        }
        Audience entity = new Audience();
        entity.setInstitute(institute);
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


    @GetMapping("/api/audience-support/delete-record/{entityId}")
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
