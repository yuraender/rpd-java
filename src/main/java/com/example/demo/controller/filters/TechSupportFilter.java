package com.example.demo.controller.filters;

import com.example.demo.entity.Department;
import com.example.demo.entity.Teacher;
import com.example.demo.entity.TechSupport;
import com.example.demo.service.DepartmentService;
import com.example.demo.service.TeacherService;
import com.example.demo.service.TechSupportService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/filter")
public class TechSupportFilter {
    @Autowired
    private TechSupportService techSupportService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private DepartmentService departmentService;
    @PostMapping("/department-filter")
    public void getTeachersByDepartment(@RequestBody Map<String, String> payload, HttpSession session, Model model) {
        String id = payload.get("departmentId");
        List<Teacher> teachersFilter = teacherService.findByDepartmentId(Long.valueOf(id));
        model.addAttribute("teachersFilter", teachersFilter);
    }
}
