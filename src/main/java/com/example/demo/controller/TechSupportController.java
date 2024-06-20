package com.example.demo.controller;

import com.example.demo.entity.Department;
import com.example.demo.entity.Discipline;
import com.example.demo.entity.Teacher;
import com.example.demo.entity.TechSupport;
import com.example.demo.service.DepartmentService;
import com.example.demo.service.DisciplineService;
import com.example.demo.service.TeacherService;
import com.example.demo.service.TechSupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TechSupportController {

    @Autowired
    private TechSupportService techSupportService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private DisciplineService disciplineService;

    @GetMapping("/tech-support")
    public String getTechSupportPage(Model model) {
        List<TechSupport> techSupports = techSupportService.getAll();
        model.addAttribute("techSupports", techSupports);

        List<Department> departments = departmentService.getAll();
        model.addAttribute("departments", departments);

        return "techSupport";
    }

    @GetMapping("/department-filter/{departmentId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getTeachersAndTechSupportByDepartment(@PathVariable Long departmentId) {
        Map<String, Object> response = new HashMap<>();

        if(departmentId == 0 ){
            response.put("teachersFilter", new ArrayList<Teacher>());
            List<TechSupport> techSupports = techSupportService.getAll();
            response.put("techSupports", techSupports);
        }else{
            List<Teacher> teachersFilter = teacherService.findByDepartmentId(departmentId);
            response.put("teachersFilter", teachersFilter);
            List<TechSupport> techSupports = techSupportService.getByDepartmentId(departmentId);
            response.put("techSupports", techSupports);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/teacher-filter/{departmentId}/{teacherId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getTeachersAndTechSupportByGetTeachers(@PathVariable Long departmentId, @PathVariable Long teacherId) {
        Map<String, Object> response = new HashMap<>();
        if(teacherId == 0 ){
            List<TechSupport> techSupports = techSupportService.getByDepartmentId(departmentId);
            response.put("disciplines", new ArrayList<Teacher>());
            response.put("techSupports", techSupports);
            return ResponseEntity.ok(response);
        }else{
            List<TechSupport> techSupports = techSupportService.getByTeacherId(departmentId, teacherId);
            List<Discipline> disciplines = disciplineService.getByTeacherId(teacherId);
            response.put("disciplines", disciplines);
            response.put("techSupports", techSupports);
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/teacher-filter/{departmentId}/{teacherId}/{disciplineId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getTeachersAndTechSupportByGetDiscipline(@PathVariable Long departmentId, @PathVariable Long teacherId, @PathVariable Long disciplineId) {
        Map<String, Object> response = new HashMap<>();
        List<TechSupport> techSupports;
        if(disciplineId == 0 ){
            techSupports = techSupportService.getByTeacherId(departmentId, teacherId);
            List<Discipline> disciplines = disciplineService.getByTeacherId(teacherId);
            response.put("disciplines", disciplines);
        }else{
            techSupports = techSupportService.getByDisciplineId(departmentId, teacherId, disciplineId);
        }
        response.put("techSupports", techSupports);


        return ResponseEntity.ok(response);
    }


}