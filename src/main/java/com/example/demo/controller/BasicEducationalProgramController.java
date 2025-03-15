package com.example.demo.controller;

import com.example.demo.entity.*;
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
public class BasicEducationalProgramController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private DirectionService directionService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private BasicEducationalProgramService basicEducationalProgramService;
    @Autowired
    private EducationTypeService educationTypeService;

    @GetMapping("/bep")
    public String getTablePage(Model model) {
        return "basic-educational-programs";
    }

    @GetMapping("/bep-data-set-active/{entityId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> setActive(@PathVariable Integer entityId, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        BasicEducationalProgram entity = basicEducationalProgramService.getById(entityId);
        if (entity == null) {
            response.put("error", "Запись не найдена");
            return ResponseEntity.ok(response);
        }
        response.put("dataName", entity.getId());

        HttpSession session = request.getSession();
        session.setAttribute("oopId", entityId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/bep-data")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getEntityData(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        HttpSession session = request.getSession();

        List<BasicEducationalProgram> beps = basicEducationalProgramService.getAll();
        response.put("data", beps);

        List<Department> departments = departmentService.getAll();
        response.put("departments", departments);

        List<Profile> entity1 = profileService.getAllProfiles();
        response.put("entity1", entity1);

        List<EducationType> entity2 = educationTypeService.getAllEducationTypes();
        response.put("entity2", entity2);

        String role = (String) session.getAttribute("role");
        response.put("role", role);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/bep/get-active/{entityId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getActiveEntity(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();
        BasicEducationalProgram entity = basicEducationalProgramService.getById(entityId);
        response.put("data", entity);
        List<Profile> profiles = profileService.getAllProfiles();
        response.put("entity1", profiles);
        List<EducationType> educationTypes = educationTypeService.getAllEducationTypes();
        response.put("entity2", educationTypes);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/bep/update")
    public ResponseEntity<Map<String, Object>> updateRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();
        Integer param0 = Integer.parseInt(payload.get("0"));
        Integer param1 = Integer.parseInt(payload.get("1"));
        Integer param2 = Integer.parseInt(payload.get("2"));
        Integer dataId = Integer.parseInt(payload.get("dataId"));

        BasicEducationalProgram entity = basicEducationalProgramService.getById(dataId);
        Profile profile = profileService.getById(param1);
        EducationType educationType = educationTypeService.getById(param2);

        if (entity == null || educationType == null) {
            response.put("error", "Запись не найдена. Запись не обновлена.");
            return ResponseEntity.ok(response);
        }
        // Обновляем поле audience у Department
        entity.setAcademicYear(param0);
        entity.setProfile(profile);
        entity.setEducationType(educationType);
        entity.setDisabled(false);
        // Сохраняем обновленную запись
        basicEducationalProgramService.save(entity);
        // Добавляем обновленную запись в ответ
        response.put("updatedData", entity);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/bep/save-new-record")
    public ResponseEntity<Map<String, Object>> createRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();
        Integer param0 = Integer.parseInt(payload.get("0"));
        Integer param1 = Integer.parseInt(payload.get("1"));
        Integer param2 = Integer.parseInt(payload.get("2"));
        Profile profile = profileService.getById(param1);
        EducationType educationType = educationTypeService.getById(param2);

        if (educationType == null || profile == null) {
            response.put("error", "Запись не найдена. Запись не обновлена.");
            return ResponseEntity.ok(response);
        }

        BasicEducationalProgram entity = new BasicEducationalProgram();
        entity.setAcademicYear(param0);
        entity.setProfile(profile);
        entity.setEducationType(educationType);
        entity.setDisabled(false);
        // Сохраняем обновленную запись
        basicEducationalProgramService.save(entity);
        // Добавляем обновленную запись в ответ
        response.put("createdData", entity);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/api/bep/delete-record/{entityId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteRecord(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        // Получаем запись TechSupport по techSupportId
        BasicEducationalProgram entity = basicEducationalProgramService.getById(entityId);
        if (entity == null) {
            response.put("error", "Запись не найдена");
            return ResponseEntity.ok(response);
        }
        entity.setDisabled(true);
        basicEducationalProgramService.save(entity);
        response.put("deletedData", entity.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/bep/department-filter/{entityId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> filterByDepartment(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();
        Department department = departmentService.getById(entityId);
        // Получаем запись entityId
        List<Direction> filterList = directionService.getByDepartment(department);
        response.put("filterList", filterList);

        List<BasicEducationalProgram> allBasicEducationalPrograms = basicEducationalProgramService.getAll();

        if (entityId == 0) {
            response.put("entityList", allBasicEducationalPrograms);
        } else {
            List<BasicEducationalProgram> entityList = allBasicEducationalPrograms.stream()
                    .filter(el -> el.getProfile().getDirection().getDepartment().getId() == entityId)
                    .filter(el -> !el.isDisabled()).toList();
            response.put("entityList", entityList);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/bep/direction-filter/{filter1}/{filter2}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> filterByDirection(@PathVariable Integer filter1, @PathVariable Integer filter2) {
        Map<String, Object> response = new HashMap<>();

        List<BasicEducationalProgram> allBasicEducationalPrograms = basicEducationalProgramService.getAll();

        if (filter2 == 0) {
            List<BasicEducationalProgram> entityList = allBasicEducationalPrograms.stream()
                    .filter(el -> el.getProfile().getDirection().getDepartment().getId() == filter1)
                    .filter(el -> !el.isDisabled()).toList();
            response.put("entityList", entityList);
        } else {
            List<BasicEducationalProgram> entityList = allBasicEducationalPrograms.stream()
                    .filter(el -> el.getProfile().getDirection().getDepartment().getId() == filter1)
                    .filter(el -> el.getProfile().getDirection().getId() == filter2)
                    .filter(el -> !el.isDisabled()).toList();
            response.put("entityList", entityList);
        }
        return ResponseEntity.ok(response);
    }
}
