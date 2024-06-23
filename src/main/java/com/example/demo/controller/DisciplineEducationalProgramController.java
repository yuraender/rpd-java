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
public class DisciplineEducationalProgramController {
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

    @GetMapping("/dep")
    public String getTablePage(Model model) {
        return "disciplines-educational-programs";
    }

    @GetMapping("/dep-data")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getEntityData(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        HttpSession session = request.getSession();

        List<DisciplineEducationalProgram> entity = disciplineEducationalProgramService.getAll();
        response.put("data", entity);

        List<Department> departments = departmentService.getAll();
        response.put("departments", departments);

        List<BasicEducationalProgram> entity1 = basicEducationalProgramService.getAll();
        response.put("entity1", entity1);

        List<EducationType> entity2 = educationTypeService.getAllEducationTypes();
        response.put("entity2", entity2);

        List<Profile> entity3 = profileService.getAllProfiles();
        response.put("entity3", entity3);

        List<Discipline> entity4 = disciplineService.getAll();
        response.put("entity4", entity4);

        String role = (String) session.getAttribute("role");
        response.put("role", role);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/dep-data-set-active/{entityId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> setActive(@PathVariable Long entityId, HttpServletRequest request) {
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

    @GetMapping("/api/dep/get-active/{entityId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getActiveEntity(@PathVariable Long entityId) {
        Map<String, Object> response = new HashMap<>();
        DisciplineEducationalProgram entity = disciplineEducationalProgramService.getById(entityId);
        response.put("data", entity);
        List<Discipline> entity2 = disciplineService.getAll();
        response.put("entity2", entity2);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/dep/update")
    public ResponseEntity<Map<String, Object>> updateRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();
        Long param0 = Long.valueOf(payload.get("0"));
        Long dataId = Long.valueOf(payload.get("dataId"));

        DisciplineEducationalProgram entity = disciplineEducationalProgramService.getById(dataId);
        Discipline discipline = disciplineService.getById(param0);

        if (entity == null || discipline == null) {
            response.put("error", "Запись не найдена. Запись не обновлена.");
            return ResponseEntity.ok(response);
        }
        // Обновляем поле audience у Department
        entity.setDiscipline(discipline);
        entity.setDisabled(false);
        // Сохраняем обновленную запись
        disciplineEducationalProgramService.save(entity);
        // Добавляем обновленную запись в ответ
        response.put("updatedData", entity);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/dep/save-new-record")
    public ResponseEntity<Map<String, Object>> createRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();
        Long param0 = Long.valueOf(payload.get("0"));
        Long param1 = Long.valueOf(payload.get("1"));

        BasicEducationalProgram basicEducationalProgram = basicEducationalProgramService.getById(param0);
        Discipline discipline = disciplineService.getById(param1);

        if (basicEducationalProgram == null || discipline == null) {
            response.put("error", "Запись не найдена. Запись не обновлена.");
            return ResponseEntity.ok(response);
        }

        DisciplineEducationalProgram entity = new DisciplineEducationalProgram();
        entity.setBasicEducationalProgram(basicEducationalProgram);
        entity.setDiscipline(discipline);
        entity.setDisabled(false);
        // Сохраняем обновленную запись
        disciplineEducationalProgramService.save(entity);
        // Добавляем обновленную запись в ответ
        response.put("createdData", entity);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/api/dep/delete-record/{entityId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteRecord(@PathVariable Long entityId) {
        Map<String, Object> response = new HashMap<>();

        // Получаем запись TechSupport по techSupportId
        DisciplineEducationalProgram entity = disciplineEducationalProgramService.getById(entityId);
        if (entity == null) {
            response.put("error", "Запись не найдена");
            return ResponseEntity.ok(response);
        }
        entity.setDisabled(true);
        disciplineEducationalProgramService.save(entity);
        response.put("deletedData", entity.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/dep/department-filter/{entityId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> filterByDepartment(@PathVariable Long entityId) {
        Map<String, Object> response = new HashMap<>();
        Department department = departmentService.getById(entityId);
        // Получаем запись entityId
        List<Direction> filterList = directionService.getByDepartment(department);
        if (filterList == null) {
            response.put("error", "Запись не найдена");
            return ResponseEntity.ok(response);
        }
        response.put("filterList", filterList);

        List<DisciplineEducationalProgram> allEntityForTable = disciplineEducationalProgramService.getAll();

        if (entityId == 0) {
            response.put("entityList", allEntityForTable);
        } else {
            List<DisciplineEducationalProgram> entityList = allEntityForTable.stream()
                    .filter(el -> Long.valueOf(el.getDiscipline().getDepartment().getId()).equals(entityId))
                    .filter(el -> el.getDisabled().equals(false)).toList();
            response.put("entityList", entityList);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/dep/direction-filter/{filter1}/{filter2}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> filterByDirection(@PathVariable Long filter1, @PathVariable Long filter2) {
        Map<String, Object> response = new HashMap<>();

        Direction direction = directionService.getById(filter2);
        // Получаем запись entityId
        List<Profile> allProfiles = profileService.getAllProfiles();
        List<Profile> filterList = allProfiles.stream()
                .filter(el -> Long.valueOf(el.getDirection().getId()).equals(filter2))
                .filter(el -> el.getDisabled().equals(false)).toList();

        if (filterList == null) {
            response.put("error", "Запись не найдена");
            return ResponseEntity.ok(response);
        }
        response.put("filterList", filterList);

        List<DisciplineEducationalProgram> allTableEntity = disciplineEducationalProgramService.getAll();

        if (filter2 == 0) {
            List<DisciplineEducationalProgram> entityList = allTableEntity.stream()
                    .filter(el -> Long.valueOf(el.getDiscipline().getDepartment().getId()).equals(filter1))
                    .filter(el -> el.getDisabled().equals(false)).toList();
            response.put("entityList", entityList);
        } else {
            List<DisciplineEducationalProgram> entityList = allTableEntity.stream()
                    .filter(el -> Long.valueOf(el.getDiscipline().getDepartment().getId()).equals(filter1))
                    .filter(el -> Long.valueOf(el.getBasicEducationalProgram().getProfile().getDirection().getId()).equals(filter2))
                    .filter(el -> el.getDisabled().equals(false)).toList();
            response.put("entityList", entityList);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/dep/profile-filter/{filter1}/{filter2}/{filter3}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> filterByProfile(@PathVariable Long filter1, @PathVariable Long filter2, @PathVariable Long filter3) {
        Map<String, Object> response = new HashMap<>();
        List<DisciplineEducationalProgram> allTableEntity = disciplineEducationalProgramService.getAll();

        if (filter3 == 0) {
            List<DisciplineEducationalProgram> entityList = allTableEntity.stream()
                    .filter(el -> Long.valueOf(el.getDiscipline().getDepartment().getId()).equals(filter1))
                    .filter(el -> el.getDisabled().equals(false)).toList();
            response.put("entityList", entityList);
        } else {
            List<DisciplineEducationalProgram> entityList = allTableEntity.stream()
                    .filter(el -> Long.valueOf(el.getDiscipline().getDepartment().getId()).equals(filter1))
                    .filter(el -> Long.valueOf(el.getBasicEducationalProgram().getProfile().getDirection().getId()).equals(filter2))
                    .filter(el -> Long.valueOf(el.getBasicEducationalProgram().getProfile().getId()).equals(filter3))
                    .filter(el -> el.getDisabled().equals(false)).toList();
            response.put("entityList", entityList);
        }
        return ResponseEntity.ok(response);
    }
}
