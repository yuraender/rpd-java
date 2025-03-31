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
public class DisciplineEducationalProgramController {

    private final DisciplineEducationalProgramService disciplineEducationalProgramService;
    private final DepartmentService departmentService;
    private final BasicEducationalProgramService basicEducationalProgramService;
    private final DisciplineService disciplineService;
    private final DirectionService directionService;
    private final ProfileService profileService;

    @GetMapping("/deps")
    public String getTablePage() {
        return "disciplines-educational-programs";
    }

    @GetMapping("/deps-data")
    public ResponseEntity<Map<String, Object>> getEntityData(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        List<DisciplineEducationalProgram> deps = disciplineEducationalProgramService.getAll();
        response.put("data", deps);

        List<Department> departments = departmentService.getAll();
        response.put("departments", departments);

        List<BasicEducationalProgram> beps = basicEducationalProgramService.getAll();
        response.put("beps", beps);

        List<Discipline> disciplines = disciplineService.getAll();
        response.put("disciplines", disciplines);

        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        response.put("role", role);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/dep/set-active/{entityId}")
    public ResponseEntity<Map<String, Object>> setActive(@PathVariable Integer entityId, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        DisciplineEducationalProgram dep = disciplineEducationalProgramService.getById(entityId);
        if (dep == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        response.put("dataName", dep.getId());

        HttpSession session = request.getSession();
        session.setAttribute("disciplinesEducationalProgramId", entityId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/dep/get-active/{entityId}")
    public ResponseEntity<Map<String, Object>> getActiveEntity(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        DisciplineEducationalProgram dep = disciplineEducationalProgramService.getById(entityId);
        response.put("data", dep);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/dep/update")
    public ResponseEntity<Map<String, Object>> updateRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();

        String index = payload.get("0");
        Integer dataId = Integer.parseInt(payload.get("dataId"));

        DisciplineEducationalProgram dep = disciplineEducationalProgramService.getById(dataId);
        if (dep == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        dep.setIndex(index);
        dep.setDisabled(false);
        disciplineEducationalProgramService.save(dep);

        response.put("updatedData", dep);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/dep/save-new-record")
    public ResponseEntity<Map<String, Object>> createRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();

        int param0, param2;
        String index = payload.get("1");
        try {
            param0 = Integer.parseInt(payload.get("0"));
            param2 = Integer.parseInt(payload.get("2"));
        } catch (NumberFormatException ex) {
            response.put("error", "Неверный формат данных.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        BasicEducationalProgram bep = basicEducationalProgramService.getById(param0);
        Discipline discipline = disciplineService.getById(param2);
        if (bep == null || discipline == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        DisciplineEducationalProgram dep = new DisciplineEducationalProgram();
        dep.setIndex(index);
        dep.setBasicEducationalProgram(bep);
        dep.setDiscipline(discipline);
        dep.setDisabled(false);
        disciplineEducationalProgramService.save(dep);

        response.put("createdData", dep);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/dep/delete-record/{entityId}")
    public ResponseEntity<Map<String, Object>> deleteRecord(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        DisciplineEducationalProgram dep = disciplineEducationalProgramService.getById(entityId);
        if (dep == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        dep.setDisabled(true);
        disciplineEducationalProgramService.save(dep);

        response.put("deletedData", dep.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/dep/department-filter/{filter1}")
    public ResponseEntity<Map<String, Object>> filterByDepartment(@PathVariable Integer filter1) {
        Map<String, Object> response = new HashMap<>();

        List<Direction> filterList = directionService.getAll().stream()
                .filter(d -> d.getDepartment().getId() == filter1)
                .toList();
        response.put("filterList", filterList);

        List<DisciplineEducationalProgram> deps = disciplineEducationalProgramService.getAll();

        if (filter1 == 0) {
            response.put("entityList", deps);
        } else {
            List<DisciplineEducationalProgram> entityList = deps.stream()
                    .filter(dep -> dep.getBasicEducationalProgram().getProfile().getDirection().getDepartment().getId() == filter1)
                    .toList();
            response.put("entityList", entityList);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/dep/direction-filter/{filter1}/{filter2}")
    public ResponseEntity<Map<String, Object>> filterByDirection(@PathVariable Integer filter1, @PathVariable Integer filter2) {
        Map<String, Object> response = new HashMap<>();

        List<Profile> filterList = profileService.getAll().stream()
                .filter(p -> p.getDirection().getDepartment().getId() == filter1)
                .filter(p -> p.getDirection().getId() == filter2)
                .toList();
        response.put("filterList", filterList);

        List<DisciplineEducationalProgram> deps = disciplineEducationalProgramService.getAll();

        if (filter2 == 0) {
            List<DisciplineEducationalProgram> entityList = deps.stream()
                    .filter(dep -> dep.getBasicEducationalProgram().getProfile().getDirection().getDepartment().getId() == filter1)
                    .toList();
            response.put("entityList", entityList);
        } else {
            List<DisciplineEducationalProgram> entityList = deps.stream()
                    .filter(dep -> dep.getBasicEducationalProgram().getProfile().getDirection().getDepartment().getId() == filter1)
                    .filter(dep -> dep.getBasicEducationalProgram().getProfile().getDirection().getId() == filter2)
                    .toList();
            response.put("entityList", entityList);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/dep/profile-filter/{filter1}/{filter2}/{filter3}")
    public ResponseEntity<Map<String, Object>> filterByProfile(@PathVariable Integer filter1, @PathVariable Integer filter2, @PathVariable Integer filter3) {
        Map<String, Object> response = new HashMap<>();

        List<DisciplineEducationalProgram> deps = disciplineEducationalProgramService.getAll();

        if (filter3 == 0) {
            List<DisciplineEducationalProgram> entityList = deps.stream()
                    .filter(dep -> dep.getBasicEducationalProgram().getProfile().getDirection().getDepartment().getId() == filter1)
                    .filter(dep -> dep.getBasicEducationalProgram().getProfile().getDirection().getId() == filter2)
                    .toList();
            response.put("entityList", entityList);
        } else {
            List<DisciplineEducationalProgram> entityList = deps.stream()
                    .filter(dep -> dep.getBasicEducationalProgram().getProfile().getDirection().getDepartment().getId() == filter1)
                    .filter(dep -> dep.getBasicEducationalProgram().getProfile().getDirection().getId() == filter2)
                    .filter(dep -> dep.getBasicEducationalProgram().getProfile().getId() == filter3)
                    .toList();
            response.put("entityList", entityList);
        }
        return ResponseEntity.ok(response);
    }
}
