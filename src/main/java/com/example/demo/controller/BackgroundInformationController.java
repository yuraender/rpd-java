package com.example.demo.controller;

import com.example.demo.entity.Department;
import com.example.demo.entity.Direction;
import com.example.demo.entity.Institute;
import com.example.demo.entity.Profile;
import com.example.demo.service.DepartmentService;
import com.example.demo.service.DirectionService;
import com.example.demo.service.InstituteService;
import com.example.demo.service.ProfileService;
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

    @Autowired
    private DirectionService directionService;

    @Autowired
    private ProfileService profileService;

    @GetMapping("/background-information")
    public String getBackgroundInformation(
            @SessionAttribute(name = "instituteId", required = false) Long instituteId,
            @SessionAttribute(name = "departmentId", required = false) Long departmentId,
            @SessionAttribute(name = "directionId", required = false) Long directionId,
            @SessionAttribute(name = "profileId", required = false) Long profileId,
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

        // Получаем все направления
        List<Direction> directions = directionService.getAllDirections();
        model.addAttribute("directions", directions);

        // Устанавливаем активную кафедру, если она есть
        if (directionId != null) {
            Direction activeDirection = directionService.getById(directionId);
            model.addAttribute("activeDirection", activeDirection);
        }

        // Получаем все профили
        List<Profile> profiles = profileService.getAllProfiles();
        model.addAttribute("profiles", profiles);

        // Устанавливаем активную кафедру, если она есть
        if (profileId != null) {
            Profile activeProfile = profileService.getById(profileId);
            model.addAttribute("activeProfile", activeProfile);
        }

        return "background-information";
    }
}