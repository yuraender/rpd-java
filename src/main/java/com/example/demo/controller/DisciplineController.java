package com.example.demo.controller;

import com.example.demo.entity.Department;
import com.example.demo.entity.Discipline;
import com.example.demo.entity.Teacher;
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
public class DisciplineController {

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
    private EmployeePositionService employeePositionService;

    @GetMapping("/disciplines")
    public String getTablePage(Model model) {
        return "disciplines";
    }

    @GetMapping("/disciplines-data")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getEntityData(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        HttpSession session = request.getSession();

        List<Discipline> entity = disciplineService.getAll();
        response.put("data", entity);

        List<Teacher> entity1 = teacherService.getAll();
        response.put("entity1", entity1);

        List<Department> entity2 = departmentService.getAll();
        response.put("entity2", entity2);

        String role = (String) session.getAttribute("role");
        response.put("role", role);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/discipline/get-active/{entityId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getActiveEntity(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();
        Discipline entity = disciplineService.getById(entityId);
        response.put("data", entity);

        List<Department> allDepartments = departmentService.getAll();
        response.put("entity1", allDepartments);

        List<Teacher> teachers = teacherService.getAll();
        response.put("entity2", teachers);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/discipline/get-department/{entityId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getDepartment(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        Department department = departmentService.getById(entityId);
        response.put("department", department);
        List<Teacher> teachers = teacherService.getAll();
        response.put("teachers", teachers);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/discipline/update")
    public ResponseEntity<Map<String, Object>> updateRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();
        String param0 = payload.get("0");
        String param1 = payload.get("1");
        Integer param2 = Integer.parseInt(payload.get("2"));
        Integer param3 = Integer.parseInt(payload.get("3"));
        Integer dataId = Integer.parseInt(payload.get("dataId"));

        Department department = departmentService.getById(param2);
        Teacher teacher = teacherService.getById(param3);

        Discipline entity = disciplineService.getById(dataId);

        if (entity == null) {
            response.put("error", "Запись не найдена. Запись не обновлена.");
            return ResponseEntity.ok(response);
        }
        // Обновляем поле audience у Department
        entity.setIndex(param0);
        entity.setName(param1);
        entity.setDepartment(department);
        entity.setDeveloper(teacher);
        entity.setDisabled(false);
        // Сохраняем обновленную запись
        disciplineService.save(entity);
        // Добавляем обновленную запись в ответ
        response.put("updatedData", entity);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/discipline/save-new-record")
    public ResponseEntity<Map<String, Object>> createRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();
        String param0 = payload.get("0");
        String param1 = payload.get("1");
        Integer param2 = Integer.parseInt(payload.get("2"));
        Integer param3 = Integer.parseInt(payload.get("3"));
        Department department = departmentService.getById(param2);
        Teacher teacher = teacherService.getById(param3);
        Discipline entity = new Discipline();
        // Обновляем поле audience у Department
        entity.setIndex(param0);
        entity.setName(param1);
        entity.setDepartment(department);
        entity.setDeveloper(teacher);
        entity.setDisabled(false);
        // Сохраняем обновленную запись
        disciplineService.save(entity);
        // Добавляем обновленную запись в ответ
        response.put("createdData", entity);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/discipline/delete-record/{entityId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteRecord(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        // Получаем запись TechSupport по techSupportId
        Discipline entity = disciplineService.getById(entityId);
        if (entity == null) {
            response.put("error", "Запись не найдена");
            return ResponseEntity.ok(response);
        }
        entity.setDisabled(true);
        disciplineService.save(entity);
        response.put("deletedData", entity.getId());
        return ResponseEntity.ok(response);
    }
}
