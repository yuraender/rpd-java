package com.example.demo.controller;

import com.example.demo.entity.Department;
import com.example.demo.entity.Employee;
import com.example.demo.entity.EmployeePosition;
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
import java.util.stream.Collectors;

@Controller
public class TeacherController {

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
    @Autowired
    private EmployeePositionService employeePositionService;

    @GetMapping("/teachers")
    public String getTablePage(Model model) {
        return "teachers";
    }

    @GetMapping("/teacher-data")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getEntityData(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        HttpSession session = request.getSession();

        List<Teacher> entity = teacherService.getAll();
        response.put("data", entity);

        List<Department> entity1 = departmentService.getAll();
        response.put("entity1", entity1);

        List<Employee> entity2 = employeeService.getAllEmployees();
        response.put("entity2", entity2);

        List<EmployeePosition> entity3 = employeePositionService.getAll();
        response.put("entity3", entity3);

        String role = (String) session.getAttribute("role");
        response.put("role", role);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/teacher/get-active/{entityId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getActiveEntity(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();
        Teacher entity = teacherService.getById(entityId);
        response.put("data", entity);

        List<Department> allDepartments = departmentService.getAll();
        List<Department> entityList = allDepartments.stream()
                .filter(el -> el.getId() == entity.getDepartment().getId() && !el.isDisabled())
                .collect(Collectors.toList());
        response.put("entity1", entityList);
        List<EmployeePosition> allPositions = employeePositionService.getAll();
        List<EmployeePosition> entityList2 = allPositions.stream()
                .filter(el -> el.getId() == entity.getEmployeePosition().getId() && !el.isDisabled()).toList();
        response.put("entity2", entityList2);
        List<Employee> entity3 = employeeService.getAllEmployees();
        response.put("entity3", entity3);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/teacher/update")
    public ResponseEntity<Map<String, Object>> updateRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();
        Integer param0 = Integer.parseInt(payload.get("0"));
        Integer param1 = Integer.parseInt(payload.get("1"));
        Integer param2 = Integer.parseInt(payload.get("2"));
        Integer dataId = Integer.parseInt(payload.get("dataId"));

        Department department = departmentService.getById(param0);
        EmployeePosition employeePosition = employeePositionService.getById(param1);
        Employee employee = employeeService.getById(param2);

        Teacher entity = teacherService.getById(dataId);

        if (entity == null) {
            response.put("error", "Запись не найдена. Запись не обновлена.");
            return ResponseEntity.ok(response);
        }
        // Обновляем поле audience у Department
        entity.setDepartment(department);
        entity.setEmployeePosition(employeePosition);
        entity.setEmployee(employee);
        entity.setDisabled(false);
        // Сохраняем обновленную запись
        teacherService.save(entity);
        // Добавляем обновленную запись в ответ
        response.put("updatedData", entity);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/teacher/save-new-record")
    public ResponseEntity<Map<String, Object>> createRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();
        Integer param0 = Integer.parseInt(payload.get("0"));
        Integer param1 = Integer.parseInt(payload.get("1"));
        Integer param2 = Integer.parseInt(payload.get("2"));

        Department department = departmentService.getById(param0);
        EmployeePosition employeePosition = employeePositionService.getById(param1);
        Employee employee = employeeService.getById(param2);

        Teacher entity = new Teacher();
        entity.setDepartment(department);
        entity.setEmployeePosition(employeePosition);
        entity.setEmployee(employee);

        entity.setDisabled(false);
        // Сохраняем обновленную запись
        teacherService.save(entity);
        // Добавляем обновленную запись в ответ
        response.put("createdData", entity);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/teacher/delete-record/{entityId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteRecord(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        // Получаем запись TechSupport по techSupportId
        Teacher entity = teacherService.getById(entityId);
        if (entity == null) {
            response.put("error", "Запись не найдена");
            return ResponseEntity.ok(response);
        }
        entity.setDisabled(true);
        teacherService.save(entity);
        response.put("deletedData", entity.getId());
        return ResponseEntity.ok(response);
    }
}
