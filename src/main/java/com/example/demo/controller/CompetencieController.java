package com.example.demo.controller;

import com.example.demo.entity.Competencie;
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
public class CompetencieController {

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
    private BasicEducationalProgramService basicEducationalProgramService;

    @Autowired
    private EducationTypeService educationTypeService;
    @Autowired
    private CompetencieService competencieService;
    @Autowired
    private DisciplineEducationalProgramService disciplineEducationalProgramService;

    @GetMapping("/competencies")
    public String getTablePage(Model model) {
        return "competencies";
    }

    @GetMapping("/competencies-data")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getEntityData(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        HttpSession session = request.getSession();

        List<Competencie> entity = competencieService.getAll();
        response.put("data", entity);

        String role = (String) session.getAttribute("role");
        response.put("role", role);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/competencie/get-active/{entityId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getActiveEntity(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();
        Competencie entity = competencieService.getById(entityId);
        response.put("data", entity);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/competencie/update")
    public ResponseEntity<Map<String, Object>> updateRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();
        String param0 = payload.get("0");
        String param1 = payload.get("1");
        String param2 = payload.get("2");
        String param3 = payload.get("3");
        String param4 = payload.get("4");
        Integer dataId = Integer.parseInt(payload.get("dataId"));

        Competencie entity = competencieService.getById(dataId);

        if (entity == null) {
            response.put("error", "Запись не найдена. Запись не обновлена.");
            return ResponseEntity.ok(response);
        }

        // Обновляем поле audience у Department
        entity.setCode(param0);
        entity.setEssence(param1);
        entity.setKnow(param2);
        entity.setBeAble(param3);
        entity.setOwn(param4);
        entity.setDisabled(false);
        // Сохраняем обновленную запись
        competencieService.save(entity);
        // Добавляем обновленную запись в ответ
        response.put("updatedData", entity);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/competencie/save-new-record")
    public ResponseEntity<Map<String, Object>> createRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();
        String param0 = payload.get("0");
        String param1 = payload.get("1");
        String param2 = payload.get("2");
        String param3 = payload.get("3");
        String param4 = payload.get("4");

        Competencie entity = new Competencie();
        entity.setCode(param0);
        entity.setEssence(param1);
        entity.setKnow(param2);
        entity.setBeAble(param3);
        entity.setOwn(param4);
        entity.setDisabled(false);
        // Сохраняем обновленную запись
        competencieService.save(entity);
        // Добавляем обновленную запись в ответ
        response.put("createdData", entity);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/competencie/delete-record/{entityId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteRecord(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        // Получаем запись TechSupport по techSupportId
        Competencie entity = competencieService.getById(entityId);
        if (entity == null) {
            response.put("error", "Запись не найдена");
            return ResponseEntity.ok(response);
        }
        entity.setDisabled(true);
        competencieService.save(entity);
        response.put("deletedData", entity.getId());
        return ResponseEntity.ok(response);
    }
}
