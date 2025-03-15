package com.example.demo.controller;

import com.example.demo.entity.Department;
import com.example.demo.entity.Employee;
import com.example.demo.service.DepartmentService;
import com.example.demo.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class DepartmentController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/departments")
    public String getDepartmentPage(Model model) {
        return "departments";
    }

    @GetMapping("/departments-data-set-active/{departmentId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> setActive(@PathVariable Integer departmentId, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        Department department = departmentService.getById(departmentId);
        if (department == null) {
            response.put("error", "Запись не найдена");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        response.put("dataName", department.getName());

        HttpSession session = request.getSession();
        session.setAttribute("departmentId", departmentId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/departments-data")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getDepartmentData(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        HttpSession session = request.getSession();
        List<Department> departments = departmentService.getAll();
        response.put("data", departments);
        List<Employee> employees = employeeService.getAllEmployees();
        response.put("employees", employees);

        String role = (String) session.getAttribute("role");
        response.put("role", role);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/department/get-active/{entityId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getActiveDepartment(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();
        Department department = departmentService.getById(entityId);
        response.put("data", department);
        List<Department> departmentList = departmentService.getAll();
        response.put("departmentList", departmentList);
        List<Employee> teacherList = employeeService.getAllEmployees();
        response.put("teacherList", teacherList);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/department/update")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();
        String param0 = payload.get("0");
        String param1 = payload.get("1");
        String param2 = payload.get("2");
        Integer param3 = Integer.parseInt(payload.get("3"));
        Integer dataId = Integer.parseInt(payload.get("dataId"));

        if (param0.isEmpty() || param1.isEmpty() || param2.isEmpty()) {
            response.put("error", "Заполните все поля. Запись не обновлена.");
            return ResponseEntity.ok(response);
        }

        Department entity = departmentService.getById(dataId);
        Employee manager = employeeService.getById(param3);
        if (entity == null || manager == null) {
            response.put("error", "Запись не найдена. Запись не обновлена.");
            return ResponseEntity.ok(response);
        }
        Optional<Department> existingDepartment = departmentService.findByCode(code);
        if (existingDepartment.isPresent()) {
            response.put("error", "Запись с таким кодом уже есть.");
            return ResponseEntity.ok(response);
        }

        // Обновляем поле audience у Department
        entity.setCode(param0);
        entity.setName(param1);
        entity.setAbbreviation(param2);
        entity.setManager(manager);
        entity.setDisabled(false);
        // Сохраняем обновленную запись
        departmentService.save(department);
        // Добавляем обновленную запись в ответ
        response.put("updatedData", department);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/department/save-new-record")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();
        String param0 = payload.get("0");
        String param1 = payload.get("1");
        String param2 = payload.get("2");
        Integer param3 = Integer.parseInt(payload.get("3"));

        Employee manager = employeeService.getById(param3);
        if (manager == null) {
            response.put("error", "Запись не найдена. Запись не обновлена.");
            return ResponseEntity.ok(response);
        }
        Optional<Department> existingDepartment = departmentService.findByCode(param0);
        if (existingDepartment.isPresent()) {
            response.put("error", "Запись с таким кодом уже есть.");
            return ResponseEntity.ok(response);
        }

        Department department = new Department();
        department.setCode(code);
        department.setName(departmentName);
        department.setAbbreviation(abbreviationName);
        department.setInstitute(institute);
        department.setManager(manager);
        department.setDisabled(false);
        // Сохраняем обновленную запись
        departmentService.save(department);
        // Добавляем обновленную запись в ответ
        response.put("createdData", department);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/department/delete-record/{departmentId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteRecord(@PathVariable Integer departmentId) {
        Map<String, Object> response = new HashMap<>();

        // Получаем запись TechSupport по techSupportId
        Department department = departmentService.getById(departmentId);
        if (department == null) {
            response.put("error", "Запись не найдена");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        department.setDisabled(true);
        departmentService.save(department);
        response.put("deletedData", department.getId());
        return ResponseEntity.ok(response);
    }
}
