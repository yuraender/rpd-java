package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final EmployeePositionService employeePositionService;
    private final AcademicDegreeService academicDegreeService;
    private final AcademicRankService academicRankService;

    @GetMapping("/teachers")
    public String getTablePage() {
        return "teachers";
    }

    @GetMapping("/teachers-data")
    public ResponseEntity<Map<String, Object>> getEntityData(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        List<Teacher> teachers = teacherService.getAll();
        response.put("data", teachers);

        List<Employee> employees = employeeService.getAll();
        response.put("employees", employees);

        List<Department> departments = departmentService.getAll();
        response.put("departments", departments);

        List<EmployeePosition> employeePositions
                = employeePositionService.getAllByType(EmployeePosition.Type.TEACHING);
        response.put("employeePositions", employeePositions);

        List<AcademicDegree> academicDegrees = academicDegreeService.getAll();
        response.put("academicDegrees", academicDegrees);

        List<AcademicRank> academicRanks = academicRankService.getAll();
        response.put("academicRanks", academicRanks);

        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        response.put("role", role);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/teacher/get-active/{entityId}")
    public ResponseEntity<Map<String, Object>> getActiveEntity(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        Teacher teacher = teacherService.getById(entityId);
        response.put("data", teacher);

        List<Department> departments = departmentService.getAll();
        response.put("departments", departments);

        List<EmployeePosition> employeePositions
                = employeePositionService.getAllByType(EmployeePosition.Type.TEACHING);
        response.put("employeePositions", employeePositions);

        List<AcademicDegree> academicDegrees = academicDegreeService.getAll();
        response.put("academicDegrees", academicDegrees);

        List<AcademicRank> academicRanks = academicRankService.getAll();
        response.put("academicRanks", academicRanks);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/teacher/update")
    public ResponseEntity<Map<String, Object>> updateRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();

        int param0, param1, param2, param3;
        try {
            param0 = Integer.parseInt(payload.get("0"));
            param1 = Integer.parseInt(payload.get("1"));
            param2 = Integer.parseInt(payload.get("2"));
            param3 = Integer.parseInt(payload.get("3"));
        } catch (NumberFormatException ex) {
            response.put("error", "Неверный формат данных.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        Integer dataId = Integer.parseInt(payload.get("dataId"));

        Teacher teacher = teacherService.getById(dataId);
        Department department = departmentService.getById(param0);
        EmployeePosition employeePosition = employeePositionService
                .getByIdAndType(param1, EmployeePosition.Type.TEACHING);
        AcademicDegree academicDegree = academicDegreeService.getById(param2);
        AcademicRank academicRank = academicRankService.getById(param3);
        if (teacher == null || department == null || employeePosition == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        teacher.setDepartment(department);
        teacher.setEmployeePosition(employeePosition);
        teacher.setAcademicDegree(academicDegree);
        teacher.setAcademicRank(academicRank);
        teacher.setDisabled(false);
        teacherService.save(teacher);

        response.put("updatedData", teacher);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/teacher/save-new-record")
    public ResponseEntity<Map<String, Object>> createRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();

        int param0, param1, param2, param3, param4;
        try {
            param0 = Integer.parseInt(payload.get("0"));
            param1 = Integer.parseInt(payload.get("1"));
            param2 = Integer.parseInt(payload.get("2"));
            param3 = Integer.parseInt(payload.get("3"));
            param4 = Integer.parseInt(payload.get("4"));
        } catch (NumberFormatException ex) {
            response.put("error", "Неверный формат данных.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Employee employee = employeeService.getById(param0);
        Department department = departmentService.getById(param1);
        EmployeePosition employeePosition = employeePositionService
                .getByIdAndType(param2, EmployeePosition.Type.TEACHING);
        AcademicDegree academicDegree = academicDegreeService.getById(param3);
        AcademicRank academicRank = academicRankService.getById(param4);
        if (employee == null || department == null || employeePosition == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if (teacherService.existsByEmployee(null, employee)) {
            response.put("error", "Запись уже существует.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        Teacher teacher = new Teacher();
        teacher.setEmployee(employee);
        teacher.setDepartment(department);
        teacher.setEmployeePosition(employeePosition);
        teacher.setAcademicDegree(academicDegree);
        teacher.setAcademicRank(academicRank);
        teacher.setDisabled(false);
        teacherService.save(teacher);

        response.put("createdData", teacher);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/teacher/delete-record/{entityId}")
    public ResponseEntity<Map<String, Object>> deleteRecord(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        Teacher teacher = teacherService.getById(entityId);
        if (teacher == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        teacher.setDisabled(true);
        teacherService.save(teacher);

        response.put("deletedData", teacher.getId());
        return ResponseEntity.ok(response);
    }
}
