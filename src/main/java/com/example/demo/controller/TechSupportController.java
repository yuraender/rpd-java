package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TechSupportController {

    @Autowired
    private TechSupportService techSupportService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private AudienceService audienceService;

    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private DisciplineService disciplineService;

    @GetMapping("/tech-support")
    public String getTechSupportPage(Model model) {
        return "techSupport";
    }

    @GetMapping("/tech-support-data")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getTechSupportData(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        HttpSession session = request.getSession();
        List<TechSupport> techSupports = techSupportService.getAll();
        response.put("techSupports", techSupports);

        List<Department> departments = departmentService.getAll();
        response.put("departments", departments);

        List<Discipline> disciplines = disciplineService.getAll();
        response.put("disciplines", disciplines);

        List<Audience> audiences = audienceService.getAll();
        response.put("audiences", audiences);

        String role = (String) session.getAttribute("role");
        response.put("role", role);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/department-filter/{departmentId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getTeachersAndTechSupportByDepartment(@PathVariable Integer departmentId) {
        Map<String, Object> response = new HashMap<>();

        if (departmentId == 0) {
            response.put("teachersFilter", new ArrayList<Teacher>());
            List<TechSupport> techSupports = techSupportService.getAll();
            response.put("techSupports", techSupports);
        } else {
            List<Teacher> teachersFilter = teacherService.findByDepartmentId(departmentId);
            response.put("teachersFilter", teachersFilter);
            List<TechSupport> techSupports = techSupportService.getByDepartmentId(departmentId);
            response.put("techSupports", techSupports);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/teacher-filter/{departmentId}/{teacherId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getTeachersAndTechSupportByGetTeachers(@PathVariable Integer departmentId, @PathVariable Integer teacherId) {
        Map<String, Object> response = new HashMap<>();
        if (teacherId == 0) {
            List<TechSupport> techSupports = techSupportService.getByDepartmentId(departmentId);
            response.put("disciplines", new ArrayList<Teacher>());
            response.put("techSupports", techSupports);
            return ResponseEntity.ok(response);
        } else {
            List<TechSupport> techSupports = techSupportService.getByTeacherId(departmentId, teacherId);
            List<Discipline> disciplines = disciplineService.getByTeacherId(teacherId);
            response.put("disciplines", disciplines);
            response.put("techSupports", techSupports);
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/teacher-filter/{departmentId}/{teacherId}/{disciplineId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getTeachersAndTechSupportByGetDiscipline(@PathVariable Integer departmentId, @PathVariable Integer teacherId, @PathVariable Integer disciplineId) {
        Map<String, Object> response = new HashMap<>();
        List<TechSupport> techSupports;
        if (disciplineId == 0) {
            techSupports = techSupportService.getByTeacherId(departmentId, teacherId);
            List<Discipline> disciplines = disciplineService.getByTeacherId(teacherId);
            response.put("disciplines", disciplines);
        } else {
            techSupports = techSupportService.getByDisciplineId(departmentId, teacherId, disciplineId);
        }
        response.put("techSupports", techSupports);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/tech-support/get-active/{techSupportId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getActiveTechSupport(@PathVariable Integer techSupportId) {
        Map<String, Object> response = new HashMap<>();
        TechSupport techSupport = techSupportService.getById(techSupportId);
        List<Audience> audiences = audienceService.getAll();
        response.put("techSupport", techSupport);
        response.put("audiences", audiences);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/tech-support/update/{techSupportId}/{newAudienceId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateRecord(@PathVariable Integer techSupportId, @PathVariable Integer newAudienceId) {
        Map<String, Object> response = new HashMap<>();

        // Получаем запись TechSupport по techSupportId
        TechSupport techSupport = techSupportService.getById(techSupportId);
        if (techSupport == null) {
            response.put("error", "TechSupport not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        // Получаем запись Audience по newAudienceId
        Audience newAudience = audienceService.getById(newAudienceId);
        if (newAudience == null) {
            response.put("error", "Audience not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Проверяем, существует ли уже запись с такими audience_id и discipline_id и disable=false
        List<TechSupport> existingTechSupports = techSupportService.findByAudienceAndDiscipline(newAudienceId, techSupport.getDiscipline().getId());
        if (!existingTechSupports.isEmpty()) {
            response.put("error", "Запись уже существует.");
            response.put("updatedTechSupport", techSupport);
            return ResponseEntity.ok(response);
        }
        // Обновляем поле audience у TechSupport
        techSupport.setAudience(newAudience);
        // Сохраняем обновленную запись
        techSupportService.save(techSupport);
        // Добавляем обновленную запись в ответ
        response.put("updatedTechSupport", techSupport);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/tech-support/save-new-record/{audienceSelectModalId}/{disciplineSelectModalId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createRecord(@PathVariable Integer audienceSelectModalId, @PathVariable Integer disciplineSelectModalId) {
        Map<String, Object> response = new HashMap<>();

        // Проверяем, существует ли уже запись с такими audience_id и discipline_id и disable=false
        List<TechSupport> existingTechSupports = techSupportService.findByAudienceAndDiscipline(audienceSelectModalId, disciplineSelectModalId);
        if (!existingTechSupports.isEmpty()) {
            response.put("error", "Запись уже существует.");
            return ResponseEntity.ok(response);
        }

        // Создаем новую запись TechSupport
        TechSupport techSupport = new TechSupport();

        // Получаем запись Audience по newAudienceId
        Audience newAudience = audienceService.getById(audienceSelectModalId);
        if (newAudience == null) {
            response.put("error", "Audience not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Получаем запись Discipline по newDisciplineId
        Discipline newDiscipline = disciplineService.getById(disciplineSelectModalId);
        if (newDiscipline == null) {
            response.put("error", "Discipline not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Обновляем поле audience и discipline у TechSupport
        techSupport.setAudience(newAudience);
        techSupport.setDiscipline(newDiscipline);
        techSupport.setDisabled(false); // Устанавливаем disabled в false при создании новой записи

        // Сохраняем новую запись
        techSupportService.save(techSupport);

        // Добавляем созданную запись в ответ
        response.put("createdTechSupport", techSupport);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/tech-support/delete-record/{techSupportId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteRecord(@PathVariable Integer techSupportId) {
        Map<String, Object> response = new HashMap<>();

        // Получаем запись TechSupport по techSupportId
        TechSupport techSupport = techSupportService.getById(techSupportId);
        if (techSupport == null) {
            response.put("error", "TechSupport not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        techSupport.setDisabled(true);
        techSupportService.save(techSupport);
        response.put("deletedTechSupport", techSupport.getId());
        return ResponseEntity.ok(response);
    }
}
