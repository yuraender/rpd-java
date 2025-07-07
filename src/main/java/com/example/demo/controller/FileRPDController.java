package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.service.BasicEducationalProgramDisciplineService;
import com.example.demo.service.DepartmentService;
import com.example.demo.service.FileRPDService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class FileRPDController {

    private final FileRPDService fileRPDService;
    private final DepartmentService departmentService;
    private final BasicEducationalProgramDisciplineService basicEducationalProgramDisciplineService;

    @GetMapping("/rpd")
    public String getTablePage() {
        return "rpd";
    }

    @GetMapping("/rpd-data")
    public ResponseEntity<Map<String, Object>> getEntityData(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        List<FileRPD> filesRPD = fileRPDService.getAll();
        response.put("data", filesRPD);

        List<Department> departments = departmentService.getAll();
        response.put("departments", departments);

        List<BasicEducationalProgramDiscipline> bepDisciplines = basicEducationalProgramDisciplineService.getAll();
        response.put("bepDisciplines", bepDisciplines);

        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        response.put("role", role);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/rpd/get-active/{entityId}")
    public ResponseEntity<Map<String, Object>> getActiveEntity(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        FileRPD fileRPD = fileRPDService.getById(entityId);
        response.put("data", fileRPD);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/rpd/uploadFile")
    public ResponseEntity<Map<String, Object>> uploadFile(
            @RequestParam Integer rpdId, @RequestParam int sectionNumber, @RequestParam MultipartFile file
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (sectionNumber < 1 || sectionNumber > 8) {
                response.put("error", "Раздел с таким номером не найден.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            if (sectionNumber == 3 || sectionNumber == 8) {
                response.put("error", "Раздел с таким номером нельзя загружать.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            if (!isDocxFile(file)) {
                response.put("error", "Вы пытаетесь загрузить не DOCX файл.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            FileRPD fileRPD = fileRPDService.getById(rpdId);
            if (fileRPD == null) {
                response.put("error", "Запись не найдена.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            String methodName = "setSection" + sectionNumber;
            FileRPD.class.getMethod(methodName, byte[].class).invoke(fileRPD, new Object[]{file.getBytes()});
            FileRPD.class.getMethod(methodName + "IsLoad", boolean.class).invoke(fileRPD, true);
            fileRPDService.save(fileRPD);

            response.put("data", sectionNumber);
            return ResponseEntity.ok(response);
        } catch (IOException | ReflectiveOperationException ex) {
            response.put("error", "Не удалось загрузить файл.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @SneakyThrows
    @PostMapping("/api/rpd/downloadFile")
    public ResponseEntity<byte[]> downloadFile(
            @RequestParam Integer rpdId, @RequestParam int sectionNumber
    ) {
        FileRPD fileRPD = fileRPDService.getById(rpdId);
        if (fileRPD == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        byte[] fileContent = (byte[]) FileRPD.class.getMethod("getSection" + sectionNumber).invoke(fileRPD);
        if (fileContent == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.attachment().filename(sectionNumber + ".docx").build());
        headers.setContentLength(fileContent.length);
        return ResponseEntity.ok().headers(headers).body(fileContent);
    }

    @PutMapping("/api/rpd/save-new-record")
    public ResponseEntity<Map<String, Object>> createRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();

        int academicYear, param1;
        try {
            academicYear = Integer.parseInt(payload.get("0"));
            param1 = Integer.parseInt(payload.get("1"));
        } catch (NumberFormatException ex) {
            response.put("error", "Неверный формат данных.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        BasicEducationalProgramDiscipline bepDiscipline = basicEducationalProgramDisciplineService.getById(param1);
        if (bepDiscipline == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if (fileRPDService.getByAcademicYearAndBasicEducationalProgramDiscipline(academicYear, bepDiscipline) != null) {
            response.put("error", "Запись уже существует.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        FileRPD fileRPD = new FileRPD();
        fileRPD.setAcademicYear(academicYear);
        fileRPD.setBasicEducationalProgramDiscipline(bepDiscipline);
        fileRPD.setDisabled(false);
        fileRPDService.save(fileRPD);

        response.put("createdData", fileRPD);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/api/rpd/delete-record/{entityId}")
    public ResponseEntity<Map<String, Object>> deleteRecord(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        FileRPD fileRPD = fileRPDService.getById(entityId);
        if (fileRPD == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        fileRPD.setDisabled(true);
        fileRPDService.save(fileRPD);

        response.put("deletedData", fileRPD.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/rpd/department-filter/{filter1}")
    public ResponseEntity<Map<String, Object>> filterByDepartment(@PathVariable Integer filter1) {
        Map<String, Object> response = new HashMap<>();

        List<BasicEducationalProgram> filterList = basicEducationalProgramDisciplineService.getAll().stream()
                .filter(bepd -> bepd.getBasicEducationalProgram().getProfile().getDirection().getDepartment().getId() == filter1)
                .map(BasicEducationalProgramDiscipline::getBasicEducationalProgram)
                .distinct()
                .toList();
        response.put("filterList", filterList);

        List<FileRPD> filesRPD = fileRPDService.getAll();

        if (filter1 == 0) {
            response.put("entityList", filesRPD);
        } else {
            List<FileRPD> entityList = filesRPD.stream()
                    .filter(f -> f.getBasicEducationalProgramDiscipline().getBasicEducationalProgram()
                            .getProfile().getDirection().getDepartment().getId() == filter1)
                    .toList();
            response.put("entityList", entityList);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/rpd/bep-filter/{filter1}/{filter2}")
    public ResponseEntity<Map<String, Object>> filterByBep(@PathVariable Integer filter1, @PathVariable Integer filter2) {
        Map<String, Object> response = new HashMap<>();

        List<Discipline> filterList = basicEducationalProgramDisciplineService.getAll().stream()
                .filter(d -> d.getBasicEducationalProgram().getProfile().getDirection().getDepartment().getId() == filter1)
                .filter(d -> d.getBasicEducationalProgram().getId() == filter2)
                .map(BasicEducationalProgramDiscipline::getDiscipline)
                .distinct()
                .toList();
        response.put("filterList", filterList);

        List<FileRPD> filesRPD = fileRPDService.getAll();

        if (filter2 == 0) {
            List<FileRPD> entityList = filesRPD.stream()
                    .filter(f -> f.getBasicEducationalProgramDiscipline().getBasicEducationalProgram()
                            .getProfile().getDirection().getDepartment().getId() == filter1)
                    .toList();
            response.put("entityList", entityList);
        } else {
            List<FileRPD> entityList = filesRPD.stream()
                    .filter(f -> f.getBasicEducationalProgramDiscipline().getBasicEducationalProgram()
                            .getProfile().getDirection().getDepartment().getId() == filter1)
                    .filter(f -> f.getBasicEducationalProgramDiscipline().getBasicEducationalProgram().getId() == filter2)
                    .toList();
            response.put("entityList", entityList);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/rpd/discipline-filter/{filter1}/{filter2}/{filter3}")
    public ResponseEntity<Map<String, Object>> filterByDiscipline(@PathVariable Integer filter1, @PathVariable Integer filter2, @PathVariable Integer filter3) {
        Map<String, Object> response = new HashMap<>();

        List<FileRPD> filesRPD = fileRPDService.getAll();

        if (filter3 == 0) {
            List<FileRPD> entityList = filesRPD.stream()
                    .filter(f -> f.getBasicEducationalProgramDiscipline().getBasicEducationalProgram()
                            .getProfile().getDirection().getDepartment().getId() == filter1)
                    .filter(f -> f.getBasicEducationalProgramDiscipline().getBasicEducationalProgram().getId() == filter2)
                    .toList();
            response.put("entityList", entityList);
        } else {
            List<FileRPD> entityList = filesRPD.stream()
                    .filter(f -> f.getBasicEducationalProgramDiscipline().getBasicEducationalProgram()
                            .getProfile().getDirection().getDepartment().getId() == filter1)
                    .filter(f -> f.getBasicEducationalProgramDiscipline().getBasicEducationalProgram().getId() == filter2)
                    .filter(f -> f.getBasicEducationalProgramDiscipline().getDiscipline().getId() == filter3)
                    .toList();
            response.put("entityList", entityList);
        }
        return ResponseEntity.ok(response);
    }

    private boolean isDocxFile(MultipartFile file) {
        // 1. Check extension
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.toLowerCase().endsWith(".docx")) {
            return false;
        }
        // 2. Check MIME type
        String contentType = file.getContentType();
        if (!"application/vnd.openxmlformats-officedocument.wordprocessingml.document".equals(contentType)) {
            return false;
        }
        try (InputStream is = file.getInputStream()) {
            // 3. Check magic bytes (ZIP header)
            byte[] data = is.readAllBytes();
            if (data.length < 4
                    || data[0] != 0x50 || data[1] != 0x4B
                    || data[2] != 0x03 || data[3] != 0x04) {
                return false;
            }
            // 4. Full DOCX validation with Apache POI
            try (
                    ByteArrayInputStream bais = new ByteArrayInputStream(data);
                    OPCPackage opcPackage = OPCPackage.open(bais);
                    XWPFDocument ignored = new XWPFDocument(opcPackage)
            ) {
                // Check for VBA macros
                for (PackagePart part : opcPackage.getParts()) {
                    if (part.getPartName().getName().contains("vbaProject")) {
                        return false;
                    }
                }
                return true;
            }
        } catch (IOException | InvalidFormatException ex) {
            return false;
        }
    }
}
