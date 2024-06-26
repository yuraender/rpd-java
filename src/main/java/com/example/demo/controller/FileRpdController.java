package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class FileRpdController {
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
    private InstituteService instituteService;
    @Autowired
    private BasicEducationalProgramService basicEducationalProgramService;

    @Autowired
    private EducationTypeService educationTypeService;

    @Autowired
    private DisciplineEducationalProgramService disciplineEducationalProgramService;
    @Autowired
    private CompetenciesDisciplinesEducationalProgramService competenciesDisciplinesEducationalProgramService;
    @Autowired
    private CompetencieService competencieService;
    @Autowired
    private FileRPDService fileRPDService;

    @GetMapping("/files-rpd")
    public String getTablePage(Model model) {
        return "files-rpd";
    }

    @GetMapping("/files-rpd-data")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getEntityData(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        HttpSession session = request.getSession();

        List<FileRPD> entity = fileRPDService.getAll();
        response.put("data", entity);

        List<Department> departments = departmentService.getAll();
        response.put("departments", departments);

        List<BasicEducationalProgram> entity1 = basicEducationalProgramService.getAll();
        response.put("entity1", entity1);

        List<Discipline> entity2 = disciplineService.getAll();
        response.put("entity2", entity2);

        List<Competencie> entity3 = competencieService.getAll();
        response.put("entity3", entity3);

        List<DisciplineEducationalProgram> entity4 = disciplineEducationalProgramService.getAll();
        response.put("entity4", entity4);


        String role = (String) session.getAttribute("role");
        response.put("role", role);

        return ResponseEntity.ok(response);
    }
//    @GetMapping("/files-rpd-data-set-active/{entityId}")
//    @ResponseBody
//    public ResponseEntity<Map<String, Object>> setActive(@PathVariable Long entityId, HttpServletRequest request) {
//        Map<String, Object> response = new HashMap<>();
//
//        BasicEducationalProgram entity = basicEducationalProgramService.getById(entityId);
//        if (entity == null) {
//            response.put("error", "Запись не найдена");
//            return ResponseEntity.ok(response);
//        }
//        response.put("dataName", entity.getId());
//
//        HttpSession session = request.getSession();
//        session.setAttribute("oopId", entityId);
//        return ResponseEntity.ok(response);
//    }
//
    @GetMapping("/api/files-rpd/get-active/{entityId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getActiveEntity(@PathVariable Long entityId) {
        Map<String, Object> response = new HashMap<>();
        FileRPD entity = fileRPDService.getById(entityId);
        response.put("data", entity);
//        List<Competencie> entity2 = competencieService.getAll();
//        response.put("entity2", entity2);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/api/files-rpd/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("sectionNumber") String sectionNumber, Long rpdId) {
        try {
            // Обработка файла и сохранение в БД
            // Например, можно получить байты файла и сохранить их в БД
            byte[] fileBytes = file.getBytes();

            // Пример сохранения в базу данных
            FileRPD fileRPD = fileRPDService.getById(rpdId);

            if(sectionNumber.equals("section3")){
                fileRPD.setSection1(fileBytes);
                fileRPD.setSection1IsLoad(true);
            }

            fileRPD.setSection4(fileBytes);
            fileRPD.setSection4IsLoad(true);
            fileRPDService.save(fileRPD);

            return ResponseEntity.ok("File uploaded successfully");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file");
        }
    }

//    @GetMapping("/api/files-rpd/download")
//    public ResponseEntity<byte[]> downloadFile(@PathVariable Integer id) {
//        FileRPD fileRPD = fileRPDService.getById(id);
//
//
//        byte[] fileContent = fileRPD.getSection1();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//        headers.setContentDispositionFormData("attachment", "title.docx");
////        headers.setContentDispositionFormData("attachment", "missions.docx");
//        headers.setContentLength(fileContent.length);
//
//        return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
//    }


//
//    @PostMapping("/api/files-rpd/update")
//    public ResponseEntity<Map<String, Object>> updateRecord(@RequestBody Map<String, String> payload) {
//        Map<String, Object> response = new HashMap<>();
//        Long param0 = Long.valueOf(payload.get("0"));
//        Long dataId = Long.valueOf(payload.get("dataId"));
//
//        DisciplineEducationalProgram entity = disciplineEducationalProgramService.getById(dataId);
//        Discipline discipline = disciplineService.getById(param0);
//
//        if (entity == null || discipline == null) {
//            response.put("error", "Запись не найдена. Запись не обновлена.");
//            return ResponseEntity.ok(response);
//        }
//        // Обновляем поле audience у Department
//        entity.setDiscipline(discipline);
//        entity.setDisabled(false);
//        // Сохраняем обновленную запись
//        disciplineEducationalProgramService.save(entity);
//        // Добавляем обновленную запись в ответ
//        response.put("updatedData", entity);
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping("/api/files-rpd/save-new-record")
//    public ResponseEntity<Map<String, Object>> createRecord(@RequestBody Map<String, String> payload) {
//        Map<String, Object> response = new HashMap<>();
//        Long param0 = Long.valueOf(payload.get("0"));
//        Long param1 = Long.valueOf(payload.get("1"));
//
//        DisciplineEducationalProgram disciplineEducationalProgram = disciplineEducationalProgramService.getById(param0);
//        Competencie competencie = competencieService.getById(param1);
//
//        if (disciplineEducationalProgram == null || competencie == null) {
//            response.put("error", "Запись не найдена. Запись не обновлена.");
//            return ResponseEntity.ok(response);
//        }
//
//        CompetenciesDisciplinesEducationalProgram entity = new CompetenciesDisciplinesEducationalProgram();
//        entity.setDisciplineEducationalProgram(disciplineEducationalProgram);
//        entity.setCompetencie(competencie);
//        entity.setDisabled(false);
//        // Сохраняем обновленную запись
//        competenciesDisciplinesEducationalProgramService.save(entity);
//        // Добавляем обновленную запись в ответ
//        response.put("createdData", entity);
//        return ResponseEntity.ok(response);
//    }
//
//
//    @GetMapping("/api/files-rpd/delete-record/{entityId}")
//    @ResponseBody
//    public ResponseEntity<Map<String, Object>> deleteRecord(@PathVariable Long entityId) {
//        Map<String, Object> response = new HashMap<>();
//
//        // Получаем запись TechSupport по techSupportId
//        CompetenciesDisciplinesEducationalProgram entity = competenciesDisciplinesEducationalProgramService.getById(entityId);
//        if (entity == null) {
//            response.put("error", "Запись не найдена");
//            return ResponseEntity.ok(response);
//        }
//        entity.setDisabled(true);
//        competenciesDisciplinesEducationalProgramService.save(entity);
//        response.put("deletedData", entity.getId());
//        return ResponseEntity.ok(response);
//    }
//
    @GetMapping("/api/files-rpd/department-filter/{entityId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> filterByDepartment(@PathVariable Long entityId) {
        Map<String, Object> response = new HashMap<>();
        // Получаем запись entityId
        List<Teacher> filterListAll = teacherService.getAll();

        List<Teacher> filterList = filterListAll.stream()
                .filter(el -> Long.valueOf(el.getDepartment().getId()).equals(entityId))
                .filter(el -> el.getDisabled().equals(false)).toList();

        response.put("filterList", filterList);

        List<FileRPD> allEntityForTable = fileRPDService.getAll();

        if (entityId == 0) {
            response.put("entityList", allEntityForTable);
        } else {
            List<FileRPD> entityList = allEntityForTable.stream()
                    .filter(el -> Long.valueOf(el.getDisciplineEducationalProgram().getDiscipline().getDepartment().getId()).equals(entityId))
                    .filter(el -> el.getDisabled().equals(false)).toList();
            response.put("entityList", entityList);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/files-rpd/direction-filter/{filter1}/{filter2}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> filterByDirection(@PathVariable Long filter1, @PathVariable Long filter2) {
        Map<String, Object> response = new HashMap<>();

        //Получаем запись entityId
        List<Discipline> allDisciplines = disciplineService.getAll();
        List<Discipline> filterList = allDisciplines.stream()
                .filter(el -> Long.valueOf(el.getDepartment().getId()).equals(filter1))
                .filter(el -> el.getDeveloper().getId().equals(filter2))
                .filter(el -> el.getDisabled().equals(false)).toList();

        if (filterList == null) {
            response.put("error", "Запись не найдена");
            return ResponseEntity.ok(response);
        }
        response.put("filterList", filterList);

        List<FileRPD> allTableEntity = fileRPDService.getAll();

        if (filter2 == 0) {
            List<FileRPD> entityList = allTableEntity.stream()
                    .filter(el -> Long.valueOf(el.getDisciplineEducationalProgram().getDiscipline().getDepartment().getId()).equals(filter1))
                    .filter(el -> el.getDisabled().equals(false)).toList();
            response.put("entityList", entityList);
        } else {
            List<FileRPD> entityList = allTableEntity.stream()
                    .filter(el -> Long.valueOf(el.getDisciplineEducationalProgram().getDiscipline().getDepartment().getId()).equals(filter1))
                    .filter(el -> el.getDisciplineEducationalProgram().getDiscipline().getDeveloper().getId().equals(filter2))
                    .filter(el -> el.getDisabled().equals(false)).toList();
            response.put("entityList", entityList);
        }
        return ResponseEntity.ok(response);
    }


    @GetMapping("/api/files-rpd/discipline-filter/{filter1}/{filter2}/{filter3}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> filterByDiscipline(@PathVariable Long filter1, @PathVariable Long filter2, @PathVariable Long filter3) {
        Map<String, Object> response = new HashMap<>();

        //Получаем запись entityId
        List<DisciplineEducationalProgram> allBasicEducationalProgram = disciplineEducationalProgramService.getAll();
        List<DisciplineEducationalProgram> filterList = allBasicEducationalProgram.stream()
                .filter(el -> Long.valueOf(el.getDiscipline().getId()).equals(filter3))
                .filter(el -> el.getDisabled().equals(false)).toList();

        if (filterList == null) {
            response.put("error", "Запись не найдена");
            return ResponseEntity.ok(response);
        }
        response.put("filterList", filterList);

        List<FileRPD> allTableEntity = fileRPDService.getAll();

        if (filter2 == 0) {
            List<FileRPD> entityList = allTableEntity.stream()
                    .filter(el -> Long.valueOf(el.getDisciplineEducationalProgram().getDiscipline().getDepartment().getId()).equals(filter1))
                    .filter(el -> Long.valueOf(el.getDisciplineEducationalProgram().getDiscipline().getDeveloper().getId()).equals(filter2))
                    .filter(el -> el.getDisabled().equals(false)).toList();
            response.put("entityList", entityList);
        } else {
            List<FileRPD> entityList = allTableEntity.stream()
                    .filter(el -> Long.valueOf(el.getDisciplineEducationalProgram().getDiscipline().getDepartment().getId()).equals(filter1))
                    .filter(el -> Long.valueOf(el.getDisciplineEducationalProgram().getDiscipline().getDeveloper().getId()).equals(filter2))
                    .filter(el -> Long.valueOf(el.getDisciplineEducationalProgram().getDiscipline().getId()).equals(filter3))
                    .filter(el -> el.getDisabled().equals(false)).toList();
            response.put("entityList", entityList);
        }
        return ResponseEntity.ok(response);
    }


    @GetMapping("/api/files-rpd/oop-filter/{filter1}/{filter2}/{filter3}/{filter4}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> filterByOOP(@PathVariable Long filter1, @PathVariable Long filter2, @PathVariable Long filter3, @PathVariable Long filter4) {
        Map<String, Object> response = new HashMap<>();
        List<FileRPD> allTableEntity = fileRPDService.getAll();

        if (filter3 == 0) {
            List<FileRPD> entityList = allTableEntity.stream()
                    .filter(el -> Long.valueOf(el.getDisciplineEducationalProgram().getDiscipline().getDepartment().getId()).equals(filter1))
                    .filter(el -> Long.valueOf(el.getDisciplineEducationalProgram().getDiscipline().getDeveloper().getId()).equals(filter2))
                    .filter(el -> Long.valueOf(el.getDisciplineEducationalProgram().getDiscipline().getId()).equals(filter3))
                    .filter(el -> el.getDisabled().equals(false)).toList();
            response.put("entityList", entityList);
        } else {
            List<FileRPD> entityList = allTableEntity.stream()
                    .filter(el -> Long.valueOf(el.getDisciplineEducationalProgram().getDiscipline().getDepartment().getId()).equals(filter1))
                    .filter(el -> Long.valueOf(el.getDisciplineEducationalProgram().getDiscipline().getDeveloper().getId()).equals(filter2))
                    .filter(el -> Long.valueOf(el.getDisciplineEducationalProgram().getDiscipline().getId()).equals(filter3))
                    .filter(el -> Long.valueOf(el.getDisciplineEducationalProgram().getId()).equals(filter4))
                    .filter(el -> el.getDisabled().equals(false)).toList();
            response.put("entityList", entityList);
        }
        return ResponseEntity.ok(response);
    }
}
