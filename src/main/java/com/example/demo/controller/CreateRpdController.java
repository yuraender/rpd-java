package com.example.demo.controller;

import com.example.demo.entity.DisciplineEducationalProgram;
import com.example.demo.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CreateRpdController {

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

    @GetMapping("/create-rpd")
    public String getTablePage(Model model) {
        return "create-rpd";
    }

    @GetMapping("/page-rpd-data")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getEntityData(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        HttpSession session = request.getSession();

        Object oopIdObj = session.getAttribute("oopId");

        Integer oopId = null;
        if (oopIdObj != null) {
            try {
                oopId = Integer.parseInt(oopIdObj.toString());
                response.put("oopId", oopId);
            } catch (NumberFormatException e) {
                response.put("error", "Неверный формат OOP ID");
                return ResponseEntity.badRequest().body(response);
            }
        }

        String role = (String) session.getAttribute("role");
        response.put("role", role);

        if (oopId != null) {
            //Список дисциплин ОП, для которых будет созданы пакеты РПД
            List<DisciplineEducationalProgram> allDisciplineEducationalPrograms = disciplineEducationalProgramService.getAll();
            Integer finalOopId = oopId;
            List<DisciplineEducationalProgram> disciplineEducationalPrograms = allDisciplineEducationalPrograms.stream()
                    .filter(el -> el.getBasicEducationalProgram().getId() == finalOopId)
                    .filter(el -> !el.isDisabled()).toList();
            response.put("disciplinesOP", disciplineEducationalPrograms);
            response.put("oopId", oopId);
        } else {
            response.put("error", "Нужно выбрать ООП");
        }

        return ResponseEntity.ok(response);
    }

//    @GetMapping("/api/teacher/get-active/{entityId}")
//    @ResponseBody
//    public ResponseEntity<Map<String, Object>> getActiveEntity(@PathVariable Integer entityId) {
//        Map<String, Object> response = new HashMap<>();
//        Teacher entity = teacherService.getById(entityId);
//        response.put("data", entity);
//
//        List<Department> allDepartments = departmentService.getAll();
//        List<Department> entityList = allDepartments.stream()
//                .filter(el -> el.getId( == entity.getDepartment().getId()) && !el.getDisabled())
//                .collect(Collectors.toList());
//        response.put("entity1", entityList);
//        List<EmployeePosition> allPositions = employeePositionService.getAll();
//        List<EmployeePosition> entityList2 = allPositions.stream()
//                .filter(el -> el.getId( == entity.getEmployeePosition().getId()) && !el.getDisabled()).toList();
//        response.put("entity2", entityList2);
//        List<Employee> entity3 = employeeService.getAllEmployees();
//        response.put("entity3", entity3);
//
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping("/api/teacher/update")
//    public ResponseEntity<Map<String, Object>> updateRecord(@RequestBody Map<String, String> payload) {
//        Map<String, Object> response = new HashMap<>();
//        Integer param0 = Integer.parseInt(payload.get("0"));
//        Integer param1 = Integer.parseInt(payload.get("1"));
//        Integer param2 = Integer.parseInt(payload.get("2"));
//        Integer dataId = Integer.parseInt(payload.get("dataId"));
//
//        Department department = departmentService.getById(param0);
//        EmployeePosition employeePosition = employeePositionService.getById(param1);
//        Employee employee = employeeService.getById(param2);
//
//        Teacher entity = teacherService.getById(dataId);
//
//        if (entity == null) {
//            response.put("error", "Запись не найдена. Запись не обновлена.");
//            return ResponseEntity.ok(response);
//        }
//        // Обновляем поле audience у Department
//        entity.setDepartment(department);
//        entity.setEmployeePosition(employeePosition);
//        entity.setEmployee(employee);
//        entity.setDisabled(false);
//        // Сохраняем обновленную запись
//        teacherService.save(entity);
//        // Добавляем обновленную запись в ответ
//        response.put("updatedData", entity);
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping("/api/teacher/save-new-record")
//    public ResponseEntity<Map<String, Object>> createRecord(@RequestBody Map<String, String> payload) {
//        Map<String, Object> response = new HashMap<>();
//        Integer param0 = Integer.parseInt(payload.get("0"));
//        Integer param1 = Integer.parseInt(payload.get("1"));
//        Integer param2 = Integer.parseInt(payload.get("2"));
//
//        Department department = departmentService.getById(param0);
//        EmployeePosition employeePosition = employeePositionService.getById(param1);
//        Employee employee = employeeService.getById(param2);
//
//        Teacher entity = new Teacher();
//        entity.setDepartment(department);
//        entity.setEmployeePosition(employeePosition);
//        entity.setEmployee(employee);
//
//        entity.setDisabled(false);
//        // Сохраняем обновленную запись
//        teacherService.save(entity);
//        // Добавляем обновленную запись в ответ
//        response.put("createdData", entity);
//        return ResponseEntity.ok(response);
//    }
}
