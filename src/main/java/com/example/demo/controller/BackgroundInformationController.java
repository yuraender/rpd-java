package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.service.*;
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

    @Autowired
    private EducationTypeService educationTypeService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeePositionService employeePositionService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private DisciplineService disciplineService;
    @Autowired
    private CompetencieService competencieService;

    @GetMapping("/background-information")
    public String getBackgroundInformation(
            @SessionAttribute(name = "instituteId", required = false) Long instituteId,
            @SessionAttribute(name = "departmentId", required = false) Long departmentId,
            @SessionAttribute(name = "directionId", required = false) Long directionId,
            @SessionAttribute(name = "profileId", required = false) Long profileId,
            @SessionAttribute(name = "educationTypeId", required = false) Long educationTypeId,
            @SessionAttribute(name = "employeeId", required = false) Long employeeId,
            @SessionAttribute(name = "employeePositionId", required = false) Long employeePositionId,
            @SessionAttribute(name = "teacherId", required = false) Long teacherId,
            @SessionAttribute(name = "disciplineId", required = false) Long disciplineId,
            @SessionAttribute(name = "competencieId", required = false) Long competencieId,
            @SessionAttribute(name = "role", required = false) String role,
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

        // Получаем все типы обучения
        List<EducationType> educationTypes = educationTypeService.getAllEducationTypes();
        model.addAttribute("educationTypes", educationTypes);

        // Устанавливаем тип обучения
        if (educationTypeId != null) {
            EducationType activeEducationTypes = educationTypeService.getById(profileId);
            model.addAttribute("activeEducationTypes", activeEducationTypes);
        }

        // Получаем всех сотрудников
        List<Employee> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);

        // Устанавливаем тип обучения, если она есть
        if (employeeId != null) {
            Employee activeEmployee = employeeService.getById(employeeId);
            model.addAttribute("activeEmployee", activeEmployee);
        }

        // Получаем всех сотрудников
        List<EmployeePosition> employeePositions = employeePositionService.getAll();
        model.addAttribute("employeePositions", employeePositions);

        // Устанавливаем тип обучения, если она есть
        if (employeePositionId != null) {
            EmployeePosition activeEmployeePosition = employeePositionService.getById(employeePositionId);
            model.addAttribute("activeEmployeePosition", activeEmployeePosition);
        }

        // Получаем всех преподавателей
        List<Teacher> teachers = teacherService.getAll();
        model.addAttribute("teachers", teachers);

        // Устанавливаем преподавателя
        if (teacherId != null) {
            Teacher activeTeacher = teacherService.getById(teacherId);
            model.addAttribute("activeEmployeePosition", activeTeacher);
        }

        // Получаем все дисциплины
        List<Discipline> disciplines = disciplineService.getAll();
        model.addAttribute("disciplines", disciplines);

        // Устанавливаем дисциплину
        if (disciplineId != null) {
            Discipline activeDiscipline = disciplineService.getById(disciplineId);
            model.addAttribute("activeDiscipline", activeDiscipline);
        }

        // Получаем все компетенции
        List<Competencie> competencies = competencieService.getAll();
        model.addAttribute("competencies", competencies);

        // Устанавливаем компетенцию
        if (competencieId != null) {
            Competencie activeCompetencie = competencieService.getById(competencieId);
            model.addAttribute("activeCompetencie", activeCompetencie);
        }




        return "background-information";
    }
}