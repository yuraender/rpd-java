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
    private CompetenceService competenceService;
    @Autowired
    private AuditoriumService auditoriumService;

    @Autowired
    private BasicEducationalProgramService basicEducationalProgramService;

    @Autowired
    private BasicEducationalProgramDisciplineService basicEducationalProgramDisciplineService;
    @Autowired
    private FileRPDService fileRPDService;

    @GetMapping("/background-information")
    public String getBackgroundInformation(
            @SessionAttribute(name = "departmentId", required = false) Integer departmentId,
            @SessionAttribute(name = "directionId", required = false) Integer directionId,
            @SessionAttribute(name = "profileId", required = false) Integer profileId,
            @SessionAttribute(name = "educationTypeId", required = false) Integer educationTypeId,
            @SessionAttribute(name = "employeeId", required = false) Integer employeeId,
            @SessionAttribute(name = "employeePositionId", required = false) Integer employeePositionId,
            @SessionAttribute(name = "teacherId", required = false) Integer teacherId,
            @SessionAttribute(name = "disciplineId", required = false) Integer disciplineId,
            @SessionAttribute(name = "competenceId", required = false) Integer competenceId,
            @SessionAttribute(name = "auditoriumId", required = false) Integer auditoriumId,
            @SessionAttribute(name = "bepId", required = false) Integer bepId,
            @SessionAttribute(name = "bepDisciplineId", required = false) Integer bepDisciplineId,
            @SessionAttribute(name = "fileRPDId", required = false) Integer fileRPDId,
            @SessionAttribute(name = "role", required = false) String role,
            Model model
    ) {
        // Получаем все кафедры
        List<Department> departments = departmentService.getAll();
        model.addAttribute("departments", departments);

        // Устанавливаем активную кафедру, если она есть
        if (departmentId != null) {
            Department activeDepartment = departmentService.getById(departmentId);
            model.addAttribute("activeDepartment", activeDepartment);
        }

        // Получаем все направления
        List<Direction> directions = directionService.getAll();
        model.addAttribute("directions", directions);

        // Устанавливаем активную кафедру, если она есть
        if (directionId != null) {
            Direction activeDirection = directionService.getById(directionId);
            model.addAttribute("activeDirection", activeDirection);
        }

        // Получаем все профили
        List<Profile> profiles = profileService.getAll();
        model.addAttribute("profiles", profiles);

        // Устанавливаем активную кафедру, если она есть
        if (profileId != null) {
            Profile activeProfile = profileService.getById(profileId);
            model.addAttribute("activeProfile", activeProfile);
        }

        // Получаем все формы обучения
        List<EducationType> educationTypes = educationTypeService.getAll();
        model.addAttribute("educationTypes", educationTypes);

        // Устанавливаем тип обучения
        if (educationTypeId != null) {
            EducationType activeEducationTypes = educationTypeService.getById(profileId);
            model.addAttribute("activeEducationTypes", activeEducationTypes);
        }

        // Получаем всех сотрудников
        List<Employee> employees = employeeService.getAll();
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
        List<Competence> competences = competenceService.getAll();
        model.addAttribute("competences", competences);

        // Устанавливаем компетенцию
        if (competenceId != null) {
            Competence activeCompetence = competenceService.getById(competenceId);
            model.addAttribute("activeCompetence", activeCompetence);
        }

        // Получаем все аудитории
        List<Auditorium> auditoriums = auditoriumService.getAll();
        model.addAttribute("auditoriums", auditoriums);

        // Устанавливаем аудиторию
        if (auditoriumId != null) {
            Auditorium activeAuditorium = auditoriumService.getById(auditoriumId);
            model.addAttribute("activeAuditorium", activeAuditorium);
        }

        // Получаем все ООП
        List<BasicEducationalProgram> basicEducationalPrograms = basicEducationalProgramService.getAll();
        model.addAttribute("basicEducationalPrograms", basicEducationalPrograms);

        // Устанавливаем ООП
        if (bepId != null) {
            BasicEducationalProgram activeBasicEducationalProgram = basicEducationalProgramService.getById(bepId);
            model.addAttribute("activeBasicEducationalProgram", activeBasicEducationalProgram);
        }

        // Получаем все дисциплины ОП
        List<BasicEducationalProgramDiscipline> bepDisciplines = basicEducationalProgramDisciplineService.getAll();
        model.addAttribute("bepDisciplines", bepDisciplines);

        // Устанавливаем дисциплины ОП
        if (bepDisciplineId != null) {
            BasicEducationalProgramDiscipline activeBepDiscipline = basicEducationalProgramDisciplineService.getById(bepDisciplineId);
            model.addAttribute("activeBepDiscipline", activeBepDiscipline);
        }

        // Получаем все РПД
        List<FileRPD> fileRPDS = fileRPDService.getAll();
        model.addAttribute("fileRPDS", fileRPDS);

        // Устанавливаем РПД
        if (fileRPDId != null) {
            FileRPD activeFileRPD = fileRPDService.getById(fileRPDId);
            model.addAttribute("activeFileRPD", activeFileRPD);
        }

        return "background-information";
    }
}
