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
public class CompetenciesDisciplinesEducationalProgramController {

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
    private DisciplineEducationalProgramService disciplineEducationalProgramService;
    @Autowired
    private CompetenciesDisciplinesEducationalProgramService competenciesDisciplinesEducationalProgramService;
    @Autowired
    private CompetencieService competencieService;

    @GetMapping("/cdep")
    public String getTablePage(Model model) {
        return "competencies-disciplines-educational-programs";
    }

    @GetMapping("/cdep-data")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getEntityData(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        HttpSession session = request.getSession();

        List<CompetenciesDisciplinesEducationalProgram> entity = competenciesDisciplinesEducationalProgramService.getAll();
        response.put("data", entity);

        List<Department> departments = departmentService.getAll();
        response.put("departments", departments);

        List<BasicEducationalProgram> entity1 = basicEducationalProgramService.getAll();
        response.put("entity1", entity1);

        List<Discipline> entity2 = disciplineService.getAll();
        response.put("entity2", entity2);

        List<Competencie> entity3 = competencieService.getAll();
        response.put("entity3", entity3);

        List<DisciplineEducationalProgram> entity4 = disciplineEducationalProgramService.getAll();
        response.put("entity4", entity4);


        String role = (String) session.getAttribute("role");
        response.put("role", role);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/cdep-data-set-active/{entityId}")
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

    @GetMapping("/api/cdep/get-active/{entityId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getActiveEntity(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();
        CompetenciesDisciplinesEducationalProgram entity = competenciesDisciplinesEducationalProgramService.getById(entityId);
        response.put("data", entity);
        List<Competencie> entity2 = competencieService.getAll();
        response.put("entity2", entity2);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/cdep/update")
    public ResponseEntity<Map<String, Object>> updateRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();
        Integer param0 = Integer.parseInt(payload.get("0"));
        Integer dataId = Integer.parseInt(payload.get("dataId"));

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

    @PostMapping("/api/cdep/save-new-record")
    public ResponseEntity<Map<String, Object>> createRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();
        Integer param0 = Integer.parseInt(payload.get("0"));
        Integer param1 = Integer.parseInt(payload.get("1"));

        DisciplineEducationalProgram disciplineEducationalProgram = disciplineEducationalProgramService.getById(param0);
        Competencie competencie = competencieService.getById(param1);

        if (disciplineEducationalProgram == null || competencie == null) {
            response.put("error", "Запись не найдена. Запись не обновлена.");
            return ResponseEntity.ok(response);
        }

        CompetenciesDisciplinesEducationalProgram entity = new CompetenciesDisciplinesEducationalProgram();
        entity.setDisciplineEducationalProgram(disciplineEducationalProgram);
        entity.setCompetencie(competencie);
        entity.setDisabled(false);
        // Сохраняем обновленную запись
        competenciesDisciplinesEducationalProgramService.save(entity);
        // Добавляем обновленную запись в ответ
        response.put("createdData", entity);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/api/cdep/delete-record/{entityId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteRecord(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        // Получаем запись TechSupport по techSupportId
        CompetenciesDisciplinesEducationalProgram entity = competenciesDisciplinesEducationalProgramService.getById(entityId);
        if (entity == null) {
            response.put("error", "Запись не найдена");
            return ResponseEntity.ok(response);
        }
        entity.setDisabled(true);
        competenciesDisciplinesEducationalProgramService.save(entity);
        response.put("deletedData", entity.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/cdep/department-filter/{entityId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> filterByDepartment(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();
        // Получаем запись entityId
        List<Teacher> filterListAll = teacherService.getAll();

        List<Teacher> filterList = filterListAll.stream()
                .filter(el -> el.getDepartment().getId() == entityId)
                .filter(el -> !el.isDisabled()).toList();

        response.put("filterList", filterList);

        List<CompetenciesDisciplinesEducationalProgram> allEntityForTable = competenciesDisciplinesEducationalProgramService.getAll();

        if (entityId == 0) {
            response.put("entityList", allEntityForTable);
        } else {
            List<CompetenciesDisciplinesEducationalProgram> entityList = allEntityForTable.stream()
                    .filter(el -> el.getDisciplineEducationalProgram().getDiscipline().getDepartment().getId() == entityId)
                    .filter(el -> !el.isDisabled()).toList();
            response.put("entityList", entityList);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/cdep/direction-filter/{filter1}/{filter2}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> filterByDirection(@PathVariable Integer filter1, @PathVariable Integer filter2) {
        Map<String, Object> response = new HashMap<>();

        //Получаем запись entityId
        List<Discipline> allDisciplines = disciplineService.getAll();
        List<Discipline> filterList = allDisciplines.stream()
                .filter(el -> el.getDepartment().getId() == filter1)
                .filter(el -> el.getDeveloper().getId() == filter2)
                .filter(el -> !el.isDisabled()).toList();

        response.put("filterList", filterList);

        List<CompetenciesDisciplinesEducationalProgram> allTableEntity = competenciesDisciplinesEducationalProgramService.getAll();

        if (filter2 == 0) {
            List<CompetenciesDisciplinesEducationalProgram> entityList = allTableEntity.stream()
                    .filter(el -> el.getDisciplineEducationalProgram().getDiscipline().getDepartment().getId() == filter1)
                    .filter(el -> !el.isDisabled()).toList();
            response.put("entityList", entityList);
        } else {
            List<CompetenciesDisciplinesEducationalProgram> entityList = allTableEntity.stream()
                    .filter(el -> el.getDisciplineEducationalProgram().getDiscipline().getDepartment().getId() == filter1)
                    .filter(el -> el.getDisciplineEducationalProgram().getDiscipline().getDeveloper().getId() == filter2)
                    .filter(el -> !el.isDisabled()).toList();
            response.put("entityList", entityList);
        }
        return ResponseEntity.ok(response);
    }


    @GetMapping("/api/cdep/discipline-filter/{filter1}/{filter2}/{filter3}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> filterByDiscipline(@PathVariable Integer filter1, @PathVariable Integer filter2, @PathVariable Integer filter3) {
        Map<String, Object> response = new HashMap<>();

        //Получаем запись entityId
        List<DisciplineEducationalProgram> allBasicEducationalProgram = disciplineEducationalProgramService.getAll();
        List<DisciplineEducationalProgram> filterList = allBasicEducationalProgram.stream()
                .filter(el -> el.getDiscipline().getId() == filter3)
                .filter(el -> !el.isDisabled()).toList();

        response.put("filterList", filterList);

        List<CompetenciesDisciplinesEducationalProgram> allTableEntity = competenciesDisciplinesEducationalProgramService.getAll();

        if (filter2 == 0) {
            List<CompetenciesDisciplinesEducationalProgram> entityList = allTableEntity.stream()
                    .filter(el -> el.getDisciplineEducationalProgram().getDiscipline().getDepartment().getId() == filter1)
                    .filter(el -> el.getDisciplineEducationalProgram().getDiscipline().getDeveloper().getId() == filter2)
                    .filter(el -> !el.isDisabled()).toList();
            response.put("entityList", entityList);
        } else {
            List<CompetenciesDisciplinesEducationalProgram> entityList = allTableEntity.stream()
                    .filter(el -> el.getDisciplineEducationalProgram().getDiscipline().getDepartment().getId() == filter1)
                    .filter(el -> el.getDisciplineEducationalProgram().getDiscipline().getDeveloper().getId() == filter2)
                    .filter(el -> el.getDisciplineEducationalProgram().getDiscipline().getId() == filter3)
                    .filter(el -> !el.isDisabled()).toList();
            response.put("entityList", entityList);
        }
        return ResponseEntity.ok(response);
    }


    @GetMapping("/api/cdep/oop-filter/{filter1}/{filter2}/{filter3}/{filter4}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> filterByOOP(@PathVariable Integer filter1, @PathVariable Integer filter2, @PathVariable Integer filter3, @PathVariable Integer filter4) {
        Map<String, Object> response = new HashMap<>();
        List<CompetenciesDisciplinesEducationalProgram> allTableEntity = competenciesDisciplinesEducationalProgramService.getAll();

        if (filter3 == 0) {
            List<CompetenciesDisciplinesEducationalProgram> entityList = allTableEntity.stream()
                    .filter(el -> el.getDisciplineEducationalProgram().getDiscipline().getDepartment().getId() == filter1)
                    .filter(el -> el.getDisciplineEducationalProgram().getDiscipline().getDeveloper().getId() == filter2)
                    .filter(el -> el.getDisciplineEducationalProgram().getDiscipline().getId() == filter3)
                    .filter(el -> !el.isDisabled()).toList();
            response.put("entityList", entityList);
        } else {
            List<CompetenciesDisciplinesEducationalProgram> entityList = allTableEntity.stream()
                    .filter(el -> el.getDisciplineEducationalProgram().getDiscipline().getDepartment().getId() == filter1)
                    .filter(el -> el.getDisciplineEducationalProgram().getDiscipline().getDeveloper().getId() == filter2)
                    .filter(el -> el.getDisciplineEducationalProgram().getDiscipline().getId() == filter3)
                    .filter(el -> el.getDisciplineEducationalProgram().getId() == filter4)
                    .filter(el -> !el.isDisabled()).toList();
            response.put("entityList", entityList);
        }
        return ResponseEntity.ok(response);
    }
}
