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
    @Autowired
    private AudienceService audienceService;

    @Autowired
    private TechSupportService techSupportService;
    @Autowired
    private BasicEducationalProgramService basicEducationalProgramService;

    @Autowired
    private DisciplineEducationalProgramService disciplineEducationalProgramService;
    @Autowired
    private CompetenciesDisciplinesEducationalProgramService competenciesDisciplinesEducationalProgramService;
    @Autowired
    private FileRPDService fileRPDService;

    @GetMapping("/background-information")
    public String getBackgroundInformation(
            @SessionAttribute(name = "instituteId", required = false) Integer instituteId,
            @SessionAttribute(name = "departmentId", required = false) Integer departmentId,
            @SessionAttribute(name = "directionId", required = false) Integer directionId,
            @SessionAttribute(name = "profileId", required = false) Integer profileId,
            @SessionAttribute(name = "educationTypeId", required = false) Integer educationTypeId,
            @SessionAttribute(name = "employeeId", required = false) Integer employeeId,
            @SessionAttribute(name = "employeePositionId", required = false) Integer employeePositionId,
            @SessionAttribute(name = "teacherId", required = false) Integer teacherId,
            @SessionAttribute(name = "disciplineId", required = false) Integer disciplineId,
            @SessionAttribute(name = "competencieId", required = false) Integer competencieId,
            @SessionAttribute(name = "audienceId", required = false) Integer audienceId,
            @SessionAttribute(name = "techSupportId", required = false) Integer techSupportId,
            @SessionAttribute(name = "basicEducationalProgramId", required = false) Integer basicEducationalProgramId,
            @SessionAttribute(name = "disciplinesEducationalProgramId", required = false) Integer disciplinesEducationalProgramId,
            @SessionAttribute(name = "competenciesDisciplinesEducationalProgramId", required = false) Integer competenciesDisciplinesEducationalProgramId,
            @SessionAttribute(name = "fileRPDId", required = false) Integer fileRPDId,
            @SessionAttribute(name = "role", required = false) String role,
            Model model
    ) {
        // Получаем все институты
        List<Institute> institutes = instituteService.getAllInstitutes();
        model.addAttribute("institutes", institutes);

        // Устанавливаем активный институт, если он есть
        if (instituteId != null) {
            Institute activeInstitute = instituteService.findById(instituteId);
            model.addAttribute("activeInstitute", activeInstitute);
        }

        // Получаем все кафедры
        List<Department> departments = departmentService.getAll();
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

        // Получаем все аудитории
        List<Audience> audiences = audienceService.getAll();
        model.addAttribute("audiences", audiences);

        // Устанавливаем аудиторию
        if (audienceId != null) {
            Audience activeAudience = audienceService.getById(audienceId);
            model.addAttribute("activeAudience", activeAudience);
        }

        // Получаем все тех.обеспечения
        List<TechSupport> techSupports = techSupportService.getAll();
        model.addAttribute("techSupports", techSupports);

        // Устанавливаем тех.обеспечение
        if (techSupportId != null) {
            TechSupport activeTechSupport = techSupportService.getById(techSupportId);
            model.addAttribute("activeTechSupport", activeTechSupport);
        }

        // Получаем все ООП
        List<BasicEducationalProgram> basicEducationalPrograms = basicEducationalProgramService.getAll();
        model.addAttribute("basicEducationalPrograms", basicEducationalPrograms);

        // Устанавливаем ООП
        if (basicEducationalProgramId != null) {
            BasicEducationalProgram activeBasicEducationalProgram = basicEducationalProgramService.getById(basicEducationalProgramId);
            model.addAttribute("activeBasicEducationalProgram", activeBasicEducationalProgram);
        }

        // Получаем все дисциплиныОП
        List<DisciplineEducationalProgram> disciplineEducationalPrograms = disciplineEducationalProgramService.getAll();
        model.addAttribute("disciplineEducationalPrograms", disciplineEducationalPrograms);

        // Устанавливаем дисциплиныОП
        if (disciplinesEducationalProgramId != null) {
            DisciplineEducationalProgram activeDisciplineEducationalProgram = disciplineEducationalProgramService.getById(disciplinesEducationalProgramId);
            model.addAttribute("activeDisciplinesEducationalProgram", activeDisciplineEducationalProgram);
        }

        // Получаем все компетенции дисциплин ОП
        List<CompetenciesDisciplinesEducationalProgram> competenciesDisciplinesEducationalPrograms = competenciesDisciplinesEducationalProgramService.getAll();
        model.addAttribute("competenciesDisciplinesEducationalPrograms", competenciesDisciplinesEducationalPrograms);

        // Устанавливаем компетенцию дисциплин ОП
        if (competenciesDisciplinesEducationalProgramId != null) {
            CompetenciesDisciplinesEducationalProgram activeCompetenciesDisciplinesEducationalProgram = competenciesDisciplinesEducationalProgramService.getById(competenciesDisciplinesEducationalProgramId);
            model.addAttribute("activeCompetenciesDisciplinesEducationalProgram", activeCompetenciesDisciplinesEducationalProgram);
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
