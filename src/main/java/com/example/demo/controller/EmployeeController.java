package com.example.demo.controller;

import com.example.demo.entity.EducationType;
import com.example.demo.entity.Employee;
import com.example.demo.entity.EmployeePosition;
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
public class EmployeeController {
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
    private EmployeePositionService employeePositionService;

    @Autowired
    private DisciplineEducationalProgramService disciplineEducationalProgramService;

    @GetMapping("/employees")
    public String getTablePage(Model model) {
        return "employees";
    }

    @GetMapping("/employees-data")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getEntityData(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        HttpSession session = request.getSession();

        List<Employee> entity = employeeService.getAllEmployees();
        response.put("data", entity);

        List<EmployeePosition> entity1 = employeePositionService.getAll();
        response.put("entity1", entity1);

        String role = (String) session.getAttribute("role");
        response.put("role", role);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/employees/get-active/{entityId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getActiveEntity(@PathVariable Long entityId) {
        Map<String, Object> response = new HashMap<>();
        Employee entity = employeeService.getById(entityId);
        response.put("data", entity);
        return ResponseEntity.ok(response);
    }



    @PostMapping("/api/employees/update")
    public ResponseEntity<Map<String, Object>> updateRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();
        String param0 = payload.get("0");
        String param1 = payload.get("1");
        String param2 = payload.get("2");
        Long dataId = Long.valueOf(payload.get("dataId"));

        Employee entity = employeeService.getById(dataId);

        if (entity == null) {
            response.put("error", "Запись не найдена. Запись не обновлена.");
            return ResponseEntity.ok(response);
        }
        // Обновляем поле audience у Department
        entity.setFirstName(param1);
        entity.setLastName(param0);
        entity.setMiddleName(param2);
        entity.setDisabled(false);
        // Сохраняем обновленную запись
        employeeService.save(entity);
        // Добавляем обновленную запись в ответ
        response.put("updatedData", entity);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/employees/save-new-record")
    public ResponseEntity<Map<String, Object>> createRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();
        String param0 = payload.get("0");
        String param1 = payload.get("1");
        String param2 = payload.get("2");
        Long param3 = Long.valueOf(payload.get("3"));

        Employee entity = new Employee();
        entity.setLastName(param0);
        entity.setFirstName(param1);
        entity.setMiddleName(param2);

        // Создаем поле nameTypeOne
        String nameTypeOne = param0;
        String nameTypeTwo = "";

        if (param1 != null && param1.length() > 0) {
            nameTypeOne += " " + param1.charAt(0) + ". ";
            nameTypeTwo += param1.charAt(0) + ". ";
        }
        if (param2 != null && param2.length() > 1) {
            nameTypeOne += param2.charAt(0) + ". ";
            nameTypeTwo += param2.charAt(0) + ". ";
        }
        nameTypeTwo+=param0;

        EmployeePosition employeePosition = employeePositionService.getById(param3);
        entity.setEmployeePosition(employeePosition);
        entity.setNameTypeOne(nameTypeOne);
        entity.setNameTypeTwo(nameTypeTwo);
        entity.setDisabled(false);
        // Сохраняем обновленную запись
        employeeService.save(entity);
        // Добавляем обновленную запись в ответ
        response.put("createdData", entity);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/employees/delete-record/{entityId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteRecord(@PathVariable Long entityId) {
        Map<String, Object> response = new HashMap<>();

        // Получаем запись TechSupport по techSupportId
        Employee entity = employeeService.getById(entityId);
        if (entity == null) {
            response.put("error", "Запись не найдена");
            return ResponseEntity.ok(response);
        }
        entity.setDisabled(true);
        employeeService.save(entity);
        response.put("deletedData", entity.getId());
        return ResponseEntity.ok(response);
    }
}
