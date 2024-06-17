package com.example.demo.controller;

import com.example.demo.entity.Department;
import com.example.demo.entity.Institute;
import com.example.demo.service.DepartmentService;
import com.example.demo.service.InstituteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Controller
public class BackgroundInformationController {

    @Autowired
    private InstituteService instituteService;

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/background-information")
    public String getBackgroundInformation(
            @SessionAttribute(name = "instituteId", required = false) Long instituteId,
            @SessionAttribute(name = "departmentId", required = false) Long departmentId,
            Model model) {

        // Получаем все институты
        List<Institute> institutes = instituteService.getAllInstitutes();
        model.addAttribute("institutes", institutes);

        // Устанавливаем активный институт, если он есть
        if (instituteId != null) {
            Institute activeInstitute = instituteService.findById(instituteId);
            model.addAttribute("activeInstitute", activeInstitute);
        }

        // Получаем все кафедры
        List<Department> departments = departmentService.getAllDepartments();
        model.addAttribute("departments", departments);

        // Устанавливаем активную кафедру, если она есть
        if (departmentId != null) {
            Department activeDepartment = departmentService.getById(departmentId);
            model.addAttribute("activeDepartment", activeDepartment);
        }

        return "background-information";
    }
}