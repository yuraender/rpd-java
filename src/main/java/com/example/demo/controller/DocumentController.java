package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.repository.FileRPDRepository;
import com.example.demo.service.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
    public ResponseEntity<byte[]> generateAndSaveDocuments(@RequestBody Map<String, Object> payload, HttpServletRequest request) throws IOException {
        Integer oopId = Integer.valueOf((String) payload.get("oopId"));
        String protocolDate = (String) payload.get("protocolDate");
        String protocolNumber = (String) payload.get("protocolNumber");
        ObjectMapper mapper = new ObjectMapper();
        List<Integer> disciplinesOpList = mapper.convertValue(payload.get("disciplinesOPList"), new TypeReference<>() {
        });

        List<File> folders = new ArrayList<>();
        byte[] zipContent = null;
        HttpHeaders headers = new HttpHeaders();

        List<DisciplineEducationalProgram> allDisciplineEducationalPrograms = disciplineEducationalProgramService.getAll();

        List<DisciplineEducationalProgram> disciplinesEducationalPrograms = allDisciplineEducationalPrograms.stream()
                .filter(el -> el.getBasicEducationalProgram().getId() == oopId)
                .filter(el -> !el.isDisabled())
                .filter(el -> disciplinesOpList.contains(el.getId()))
                .collect(Collectors.toList());

        if (!disciplinesEducationalPrograms.isEmpty()) {
            for (DisciplineEducationalProgram disciplineEducationalProgram : disciplinesEducationalPrograms) {
//                Institute institute = instituteService.findById(instituteId);
//                String instituteName = institute.getName();
//                String instituteCity = institute.getCity();
//                String instituteApprovalText = institute.getApprovalText();
//                String instituteFooterText = institute.getFooterText();
//                Employee employee = institute.getDirector();
//                String directorName = employee.getNameTypeTwo();
//                String employeePosition = employee.getEmployeePosition().getPositionName();
                Direction direction = disciplineEducationalProgram.getBasicEducationalProgram().getProfile().getDirection();
                String directionCode = direction.getEncryption();
                String directionName = direction.getName();
                EducationType educationType = disciplineEducationalProgram.getBasicEducationalProgram().getEducationType();
                String educationTypeText = educationType.getText();
                String educationTypeName = educationType.getText();
                String educationTypeLearningPeriod = String.valueOf(educationType.getLearningPeriod());
                Profile profile = disciplineEducationalProgram.getBasicEducationalProgram().getProfile();
                String profileName = profile.getName();
                Discipline discipline = disciplineEducationalProgram.getDiscipline();
                String disciplineName = discipline.getName();
                List<CompetenciesDisciplinesEducationalProgram> allCompetenciesOP = competenciesDisciplinesEducationalProgramService.getAll();
                Integer disciplineOPActual = disciplineEducationalProgram.getId();
                List<CompetenciesDisciplinesEducationalProgram> competenciesOPFilter = allCompetenciesOP.stream()
                        .filter(el -> el.getDisciplineEducationalProgram().getId() == disciplineOPActual)
                        .filter(el -> !el.isDisabled()).toList();
                List<Map<String, String>> competenciesData = new ArrayList<>();

                for (CompetenciesDisciplinesEducationalProgram cdep : competenciesOPFilter) {
                    Competencie competencie = cdep.getCompetencie();
                    Map<String, String> competencyData = new HashMap<>();
                    competencyData.put("competencyName", competencie.getEssence());
                    competencyData.put("competencyKnow", competencie.getKnow());
                    competencyData.put("competencyBeAble", competencie.getBeAble());
                    competencyData.put("competencyOwn", competencie.getOwn());
                    competencyData.put("competencyCode", competencie.getCode());
                    competenciesData.add(competencyData);
                }
                Department department = disciplineEducationalProgram.getDiscipline().getDeveloper().getDepartment();
                String developerPosition = disciplineEducationalProgram.getDiscipline().getDeveloper().getEmployeePosition().getName();
                String abbreviation = department.getAbbreviation();
                String developerName = disciplineEducationalProgram.getDiscipline().getDeveloper().getEmployee().getNameTypeTwo();
                String managerName = department.getManager().getNameTypeTwo();

                // Получение данных для тех обеспечения
                List<Audience> allAudiencesList = audienceService.getAll();
                List<Audience> audiencesFiltered = allAudiencesList.stream()
                        .filter(el -> el.getId() == discipline.getId())
                        .filter(el -> !el.isDisabled()).toList();
                List<Map<String, String>> audienciesData = new ArrayList<>();

                for (Audience audience : audiencesFiltered) {
                    Map<String, String> audiences = new HashMap<>();
                    audiences.put("roomNumber", audience.getAudienceNumber());
                    audiences.put("specialEquipment", audience.getTech());
                    audiences.put("softwareLicenses", audience.getSoftwareLicense());
                    audienciesData.add(audiences);
                }

                Map<String, Object> dataMap = new HashMap<>();
//                dataMap.put("instituteName", instituteName);
//                dataMap.put("instituteCity", instituteCity);
//                dataMap.put("employeePosition", employeePosition);
//                dataMap.put("instituteApprovalText", instituteApprovalText);
//                dataMap.put("directorName", directorName);
                dataMap.put("directionCode", directionCode);
                dataMap.put("directionName", directionName);
                dataMap.put("educationTypeText", educationTypeText);
                dataMap.put("educationTypeLearningPeriod", educationTypeLearningPeriod);
                dataMap.put("profileName", profileName);
                dataMap.put("disciplineName", disciplineName);
                dataMap.put("protocolDate", protocolDate);
                dataMap.put("educationTypeName", educationTypeName);
                dataMap.put("developerPosition", developerPosition);
                dataMap.put("abbreviation", abbreviation);
                dataMap.put("developerName", developerName);
                dataMap.put("managerName", managerName);
                dataMap.put("protocolNumber", protocolNumber);
//                dataMap.put("instituteFooterText", instituteFooterText);

                FileRPD fileRPD = documentService.generateAndSaveDocuments(dataMap, disciplineEducationalProgram, competenciesData, audienciesData);

                String sanitizedPath = disciplineName.replaceAll("[\\\\/:*?\"<>|\\-]", "_");

                File folder = new File("generated_documents/" + sanitizedPath);
                folder.mkdirs();

                saveDocumentPart(folder, "Титульный лист.docx", fileRPD.getSection0());
                saveDocumentPart(folder, "Цели освоения дисциплины.docx", fileRPD.getSection1());
                saveDocumentPart(folder, "Место дисциплины в составе ООП.docx", fileRPD.getSection2());
                saveDocumentPart(folder, "Компетенции обучающегося.docx", fileRPD.getSection3());
                saveDocumentPart(folder, "Структура и содержание дисциплины.docx", fileRPD.getSection4());
                saveDocumentPart(folder, "Образовательные технологии.docx", fileRPD.getSection5());
                saveDocumentPart(folder, "Оценочные средства.docx", fileRPD.getSection6());
                saveDocumentPart(folder, "Учебно-методическое и информационное обеспечение дисциплины.docx", fileRPD.getSection7());
                saveDocumentPart(folder, "Материально-техническое обеспечение дисциплины.docx", fileRPD.getSection8());
                saveDocumentPart(folder, "Подвал.docx", fileRPD.getSection9());
                folders.add(folder);
            }

            File zipFile = new File("generated_documents/all_documents.zip");
            try (FileOutputStream fos = new FileOutputStream(zipFile);
                 ZipOutputStream zipOut = new ZipOutputStream(fos)) {
                Set<String> entryNames = new HashSet<>();
                for (File folder : folders) {
                    zipFolder(folder, folder.getName(), zipOut, entryNames);
                }
            }
            zipContent = Files.readAllBytes(zipFile.toPath());
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=all_documents.zip");

            // Удаление папки generated_documents после создания архива
            File generatedDocumentsFolder = new File("generated_documents");
            deleteFolderRecursively(generatedDocumentsFolder);
        }
        return new ResponseEntity<>(zipContent, headers, HttpStatus.OK);
    }

    private void zipFolder(File folder, String parentFolder, ZipOutputStream zipOut, Set<String> entryNames) throws IOException {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    zipFolder(file, parentFolder + "/" + file.getName(), zipOut, entryNames);
                } else {
                    try (FileInputStream fis = new FileInputStream(file)) {
                        String entryName = parentFolder + "/" + file.getName();
                        if (entryNames.contains(entryName)) {
                            continue; // Пропустить дублирующиеся записи
                        }
                        entryNames.add(entryName);
                        zipOut.putNextEntry(new ZipEntry(entryName));
                        byte[] bytes = new byte[1024];
                        int length;
                        while ((length = fis.read(bytes)) >= 0) {
                            zipOut.write(bytes, 0, length);
                        }
                        zipOut.closeEntry();
                    }
                }
            }
        }
    }

    private void saveDocumentPart(File folder, String fileName, byte[] content) throws IOException {
        String sanitizedFileName = fileName.replaceAll("[\\\\/:*?\"<>|\\-]", "_");

        File file = new File(folder, sanitizedFileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(content);
        }
    }

    private void deleteFolderRecursively(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteFolderRecursively(file);
                } else {
                    file.delete();
                }
            }
        }
        folder.delete();
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Integer id) {
//        FileRPD fileRPD = fileRPDRepository.findById(4L).orElseThrow(() -> new RuntimeException("File not found"));
        FileRPD fileRPD = fileRPDRepository.findAll().getLast();

        byte[] fileContent = fileRPD.getSection9();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "title.docx");
//        headers.setContentDispositionFormData("attachment", "missions.docx");
        headers.setContentLength(fileContent.length);

        return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
    }
}
