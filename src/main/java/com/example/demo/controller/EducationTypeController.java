package com.example.demo.controller;

import com.example.demo.entity.EducationType;
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
public class EducationTypeController {

    @Autowired
    private TechSupportService techSupportService;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DirectionService directionService;
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
    @Autowired
    private BasicEducationalProgramService basicEducationalProgramService;

    @Autowired
    private EducationTypeService educationTypeService;
    @Autowired
    private DisciplineEducationalProgramService disciplineEducationalProgramService;

    @GetMapping("/education-type")
    public String getTablePage(Model model) {
        return "education-types";
    }

    @GetMapping("/education-type-data")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getEntityData(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        HttpSession session = request.getSession();

        List<EducationType> entity = educationTypeService.getAllEducationTypes();
        response.put("data", entity);

        String role = (String) session.getAttribute("role");
        response.put("role", role);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/education-type/get-active/{entityId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getActiveEntity(@PathVariable Long entityId) {
        Map<String, Object> response = new HashMap<>();
        EducationType entity = educationTypeService.getById(entityId);
        response.put("data", entity);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/education-type/update")
    public ResponseEntity<Map<String, Object>> updateRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();
        String param0 = payload.get("0");
        String param1 = payload.get("1");
        String param2 = payload.get("2");
        Long dataId = Long.valueOf(payload.get("dataId"));

        EducationType entity = educationTypeService.getById(dataId);

        if (entity == null) {
            response.put("error", "Запись не найдена. Запись не обновлена.");
            return ResponseEntity.ok(response);
        }
        // Обновляем поле audience у Department
        entity.setName(param0);
        entity.setLearningPeriod(Integer.valueOf(param1));
        entity.setText(param2);
        entity.setDisabled(false);
        // Сохраняем обновленную запись
        educationTypeService.save(entity);
        // Добавляем обновленную запись в ответ
        response.put("updatedData", entity);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/education-type/save-new-record")
    public ResponseEntity<Map<String, Object>> createRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();
        String param0 = payload.get("0");
        String param1 = payload.get("1");
        String param2 = payload.get("2");

        EducationType entity = new EducationType();
        entity.setName(param0);
        entity.setLearningPeriod(Integer.valueOf(param1));
        entity.setText(param2);
        entity.setDisabled(false);
        // Сохраняем обновленную запись
        educationTypeService.save(entity);
        // Добавляем обновленную запись в ответ
        response.put("createdData", entity);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/education-type/delete-record/{entityId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteRecord(@PathVariable Long entityId) {
        Map<String, Object> response = new HashMap<>();

        // Получаем запись TechSupport по techSupportId
        EducationType entity = educationTypeService.getById(entityId);
        if (entity == null) {
            response.put("error", "Запись не найдена");
            return ResponseEntity.ok(response);
        }
        entity.setDisabled(true);
        educationTypeService.save(entity);
        response.put("deletedData", entity.getId());
        return ResponseEntity.ok(response);
    }
}
