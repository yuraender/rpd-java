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

    @Autowired
    private InstituteService instituteService;

    @GetMapping("/departments")
    public String getDepartmentPage(Model model) {
        return "departments";
    }

    @GetMapping("/departments-data-set-active/{departmentId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> setActive(@PathVariable Long departmentId, HttpServletRequest request) {
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
        Long instituteIdFromSession = (Long) session.getAttribute("instituteId");
        List<Department> departments = departmentService.getAll(instituteIdFromSession);
        response.put("data", departments);
        List<Institute> institutes = instituteService.getAllInstitutes();
        response.put("institutes", institutes);
        List<Employee> employees = employeeService.getAllEmployees();
        response.put("employees", employees);

        String role = (String) session.getAttribute("role");
        response.put("role", role);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/departments/get-active/{departmentsId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getActiveDepartment(@PathVariable Long departmentsId) {
        Map<String, Object> response = new HashMap<>();
        Department department = departmentService.getById(departmentsId);
        response.put("data", department);
        List<Department> departmentList = departmentService.getAll();
        response.put("departmentList", departmentList);
        List<Institute> instituteList = instituteService.getAllInstitutes();
        response.put("instituteList", instituteList);
        List<Employee> teacherList = employeeService.getAllEmployees();
        response.put("teacherList", teacherList);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/department-support/update/{departmentId}/{code}/{departmentName}/{abbreviationName}/{instituteId}/{teacherId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateRecord(
            @PathVariable Long departmentId,
            @PathVariable String code,
            @PathVariable String departmentName,
            @PathVariable String abbreviationName,
            @PathVariable Long instituteId,
            @PathVariable Long teacherId) {
        Map<String, Object> response = new HashMap<>();

        if(code.isEmpty() || departmentName.isEmpty() || abbreviationName.isEmpty()){
            response.put("error", "Заполните все поля. Запись не обновлена.");
            return ResponseEntity.ok(response);
        }

        Department department = departmentService.getById(departmentId);
        Institute institute = instituteService.findById(instituteId);
        Employee manager = employeeService.getById(teacherId);
        if (institute == null || department == null || manager == null) {
            response.put("error", "Запись не найдена. Запись не обновлена.");
            return ResponseEntity.ok(response);
        }
        Optional<Department> existingDepartment = departmentService.findByCode(code);
        if (existingDepartment.isPresent()) {
            response.put("error", "Запись с таким кодом уже есть.");
            return ResponseEntity.ok(response);
        }

        // Обновляем поле audience у Department
        department.setCode(code);
        department.setName(departmentName);
        department.setAbbreviation(abbreviationName);
        department.setInstitute(institute);
        department.setManager(manager);
        department.setDisabled(false);
        // Сохраняем обновленную запись
        departmentService.save(department);
        // Добавляем обновленную запись в ответ
        response.put("updatedData", department);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/department-support/save-new-record/{code}/{departmentName}/{abbreviationName}/{instituteId}/{teacherId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createRecord(
            @PathVariable String code,
            @PathVariable String departmentName,
            @PathVariable String abbreviationName,
            @PathVariable Long instituteId,
            @PathVariable Long teacherId)
    {
        Map<String, Object> response = new HashMap<>();
        Institute institute = instituteService.findById(instituteId);
        Employee manager = employeeService.getById(teacherId);
        if (institute == null || manager == null) {
            response.put("error", "Запись не найдена. Запись не обновлена.");
            return ResponseEntity.ok(response);
        }
        Optional<Department> existingDepartment = departmentService.findByCode(code);
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

    @GetMapping("/api/department-support/delete-record/{departmentId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteRecord(@PathVariable Long departmentId) {
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
