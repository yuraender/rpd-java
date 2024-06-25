package com.example.demo.controller;
import com.example.demo.entity.*;
import com.example.demo.repository.FileRPDRepository;
import com.example.demo.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private TechSupportService techSupportService;

    @Autowired
    private FileRPDRepository fileRPDRepository;

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
    private InstituteService instituteService;
    @Autowired
    private BasicEducationalProgramService basicEducationalProgramService;

    @Autowired
    private DocumentService documentService;
    @Autowired
    private EducationTypeService educationTypeService;

    @Autowired
    private DisciplineEducationalProgramService disciplineEducationalProgramService;
    @Autowired
    private EmployeePositionService employeePositionService;
    @Autowired
    private CompetenciesDisciplinesEducationalProgramService competenciesDisciplinesEducationalProgramService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateAndSaveDocuments(@RequestBody Map<String, String> payload, HttpServletRequest request) throws IOException {
        Long oopId = Long.valueOf(payload.get("oopId"));
        String dateProtocol = String.valueOf(payload.get("dateProtocol"));
        HttpSession session = request.getSession();

        Long instituteId = (Long) session.getAttribute("instituteId");

        List<DisciplineEducationalProgram> allDisciplineEducationalPrograms = disciplineEducationalProgramService.getAll();

        List<DisciplineEducationalProgram> disciplinesEducationalPrograms = allDisciplineEducationalPrograms.stream()
                .filter(el -> Long.valueOf(el.getBasicEducationalProgram().getId()).equals(oopId))
                .filter(el -> el.getDisabled().equals(false)).collect(Collectors.toList());

        if (!disciplinesEducationalPrograms.isEmpty()) {
            for (DisciplineEducationalProgram disciplineEducationalProgram : disciplinesEducationalPrograms) {

//                Long instituteId = Long.valueOf(payload.get("instituteId"));
//                Long directionId = payload.get("directionId");
//                Long profileId = payload.get("profileId");
//                Long departmentId = payload.get("departmentId");

                Institute institute = instituteService.findById(instituteId);
                // Институт
                String instituteName = institute.getName();
                String instituteCity = institute.getCity();
                String instituteApprovalText = institute.getApprovalText();
                // ===========================================================
                // Сотрудник
                Employee employee = institute.getDirector();
                String directorName = employee.getNameTypeTwo();
                String employeePosition = disciplineEducationalProgram.getDiscipline().getDeveloper().getEmployeePosition().getPositionName();

                // ===========================================================
                // Направление
                Direction direction = disciplineEducationalProgram.getBasicEducationalProgram().getProfile().getDirection();
                String directionCode = direction.getEncryption();
                String directionName = direction.getName();
                // ===========================================================
                // Тип обучения
                EducationType educationType = disciplineEducationalProgram.getBasicEducationalProgram().getEducationType();
                String educationTypeText = educationType.getText();
                String educationTypeName = educationType.getText();
                String educationTypeLearningPeriod = String.valueOf(educationType.getLearningPeriod());
                // ===========================================================
                // Профиль
                Profile profile = disciplineEducationalProgram.getBasicEducationalProgram().getProfile();
                String profileName = profile.getName();
                // ===========================================================
                Discipline discipline = disciplineEducationalProgram.getDiscipline();
                String disciplineName = discipline.getName();
                //============================================================
                List<CompetenciesDisciplinesEducationalProgram> allCompetenciesOP = competenciesDisciplinesEducationalProgramService.getAll();
                Integer competenciesOPFirst = allCompetenciesOP.getFirst().getDisciplineEducationalProgram().getId();
                Integer disciplineOPActual = disciplineEducationalProgram.getId();
                List<CompetenciesDisciplinesEducationalProgram> competenciesOPFilter = allCompetenciesOP.stream()
                        .filter(el -> el.getDisciplineEducationalProgram().getId().equals(disciplineOPActual))
                        .filter(el -> el.getDisabled().equals(false)).toList();
                Competencie competencie = competenciesOPFilter.getFirst().getCompetencie();
                String competencyBeAble = competencie.getBeAble();

                // Собираем данные в карту
                Map<String, String> dataMap = new HashMap<>();
                dataMap.put("instituteName", instituteName);
                dataMap.put("instituteCity", instituteCity);
                dataMap.put("employeePosition", employeePosition);
                dataMap.put("instituteApprovalText", instituteApprovalText);
                dataMap.put("directorName", directorName);
                dataMap.put("directionCode", directionCode);
                dataMap.put("directionName", directionName);
                dataMap.put("educationTypeText", educationTypeText);
                dataMap.put("educationTypeLearningPeriod", educationTypeLearningPeriod);
                dataMap.put("profileName", profileName);
                dataMap.put("disciplineName", disciplineName);
                dataMap.put("dateProtocol", dateProtocol);

                //====================================list2
                dataMap.put("competencyBeAble", competencyBeAble);

                // Передаем карту в метод генерации и сохранения документов
                documentService.generateAndSaveDocuments(dataMap, disciplineEducationalProgram);
            }
        }
        return ResponseEntity.ok("Documents generated and saved successfully.");
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Integer id) {
//        FileRPD fileRPD = fileRPDRepository.findById(4L).orElseThrow(() -> new RuntimeException("File not found"));
        FileRPD fileRPD = fileRPDRepository.findAll().getLast();

        byte[] fileContent = fileRPD.getSection1();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "title.docx");
//        headers.setContentDispositionFormData("attachment", "missions.docx");
        headers.setContentLength(fileContent.length);

        return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
    }

}