package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class BasicEducationalProgramController {

    private final BasicEducationalProgramService basicEducationalProgramService;
    private final DepartmentService departmentService;
    private final DirectionService directionService;
    private final ProfileService profileService;
    private final EducationTypeService educationTypeService;
    private final ProtocolService protocolService;

    @GetMapping("/beps")
    public String getTablePage() {
        return "basic-educational-programs";
    }

    @GetMapping("/api/bep/set-active/{entityId}")
    public ResponseEntity<Map<String, Object>> setActive(@PathVariable Integer entityId, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        BasicEducationalProgram bep = basicEducationalProgramService.getById(entityId);
        if (bep == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        response.put("dataName", bep.getId());

        HttpSession session = request.getSession();
        session.setAttribute("oopId", entityId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/beps-data")
    public ResponseEntity<Map<String, Object>> getEntityData(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        List<BasicEducationalProgram> beps = basicEducationalProgramService.getAll();
        response.put("data", beps);

        List<Department> departments = departmentService.getAll();
        response.put("departments", departments);

        List<Direction> directions = directionService.getAll();
        response.put("directions", directions);

        List<Profile> profiles = profileService.getAll();
        response.put("profiles", profiles);

        List<EducationType> educationTypes = educationTypeService.getAll();
        response.put("educationTypes", educationTypes);

        List<Protocol> protocols = protocolService.getAllByType(Protocol.Type.APPROVE);
        response.put("protocols", protocols);

        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        response.put("role", role);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/bep/get-active/{entityId}")
    public ResponseEntity<Map<String, Object>> getActiveEntity(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        BasicEducationalProgram bep = basicEducationalProgramService.getById(entityId);
        response.put("data", bep);

        List<Direction> directions = directionService.getAll();
        response.put("directions", directions);

        List<Profile> profiles = profileService.getAll();
        response.put("profiles", profiles);

        List<EducationType> educationTypes = educationTypeService.getAll();
        response.put("educationTypes", educationTypes);

        List<Protocol> protocols = protocolService.getAllByType(Protocol.Type.APPROVE);
        response.put("protocols", protocols);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/bep/update")
    public ResponseEntity<Map<String, Object>> updateRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();

        int academicYear, param2, param3, param4;
        try {
            academicYear = Integer.parseInt(payload.get("0"));
            param2 = Integer.parseInt(payload.get("2"));
            param3 = Integer.parseInt(payload.get("3"));
            param4 = Integer.parseInt(payload.get("4"));
        } catch (NumberFormatException ex) {
            response.put("error", "Неверный формат данных.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        Integer dataId = Integer.parseInt(payload.get("dataId"));

        BasicEducationalProgram bep = basicEducationalProgramService.getById(dataId);
        Profile profile = profileService.getById(param2);
        EducationType educationType = educationTypeService.getById(param3);
        Protocol protocol = protocolService.getByIdAndType(param4, Protocol.Type.APPROVE);
        if (bep == null || profile == null || educationType == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if (protocol.getDate().toLocalDate().getYear() != academicYear) {
            response.put("error", "Дата утверждения должна соответствовать учебному году.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        bep.setAcademicYear(academicYear);
        bep.setProfile(profile);
        bep.setEducationType(educationType);
        bep.setProtocol(protocol);
        bep.setDisabled(false);
        basicEducationalProgramService.save(bep);

        response.put("updatedData", bep);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/bep/save-new-record")
    public ResponseEntity<Map<String, Object>> createRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();

        int academicYear, param2, param3, param4;
        try {
            academicYear = Integer.parseInt(payload.get("0"));
            param2 = Integer.parseInt(payload.get("2"));
            param3 = Integer.parseInt(payload.get("3"));
            param4 = Integer.parseInt(payload.get("4"));
        } catch (NumberFormatException ex) {
            response.put("error", "Неверный формат данных.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Profile profile = profileService.getById(param2);
        EducationType educationType = educationTypeService.getById(param3);
        Protocol protocol = protocolService.getByIdAndType(param4, Protocol.Type.APPROVE);
        if (profile == null || educationType == null || protocol == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if (protocol.getDate().toLocalDate().getYear() != academicYear) {
            response.put("error", "Дата утверждения должна соответстовать учебному году.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        BasicEducationalProgram bep = new BasicEducationalProgram();
        bep.setAcademicYear(academicYear);
        bep.setProfile(profile);
        bep.setEducationType(educationType);
        bep.setProtocol(protocol);
        bep.setDisabled(false);
        basicEducationalProgramService.save(bep);

        response.put("createdData", bep);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/bep/delete-record/{entityId}")
    public ResponseEntity<Map<String, Object>> deleteRecord(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        BasicEducationalProgram bep = basicEducationalProgramService.getById(entityId);
        if (bep == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        bep.setDisabled(true);
        basicEducationalProgramService.save(bep);

        response.put("deletedData", bep.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/bep/department-filter/{filter1}")
    public ResponseEntity<Map<String, Object>> filterByDepartment(@PathVariable Integer filter1) {
        Map<String, Object> response = new HashMap<>();

        List<BasicEducationalProgram> beps = basicEducationalProgramService.getAll();

        if (filter1 == 0) {
            response.put("entityList", beps);
        } else {
            List<BasicEducationalProgram> entityList = beps.stream()
                    .filter(bep -> bep.getProfile().getDirection().getDepartment().getId() == filter1)
                    .toList();
            response.put("entityList", entityList);
        }
        return ResponseEntity.ok(response);
    }
}
