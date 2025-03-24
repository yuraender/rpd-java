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
public class CompetenciesDisciplinesEducationalProgramController {

    private final CompetenciesDisciplinesEducationalProgramService competenciesDisciplinesEducationalProgramService;
    private final DepartmentService departmentService;
    private final DisciplineEducationalProgramService disciplineEducationalProgramService;
    private final CompetencieService competencieService;
    private final TeacherService teacherService;

    @GetMapping("/cdeps")
    public String getTablePage() {
        return "competencies-disciplines-educational-programs";
    }

    @GetMapping("/api/cdep/set-active/{entityId}")
    public ResponseEntity<Map<String, Object>> setActive(@PathVariable Integer entityId, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        CompetenciesDisciplinesEducationalProgram cdep = competenciesDisciplinesEducationalProgramService.getById(entityId);
        if (cdep == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        response.put("dataName", cdep.getId());

        HttpSession session = request.getSession();
        session.setAttribute("competenciesDisciplinesEducationalProgramId", entityId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cdeps-data")
    public ResponseEntity<Map<String, Object>> getEntityData(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        List<CompetenciesDisciplinesEducationalProgram> cdeps = competenciesDisciplinesEducationalProgramService.getAll();
        response.put("data", cdeps);

        List<Department> departments = departmentService.getAll();
        response.put("departments", departments);

        List<DisciplineEducationalProgram> deps = disciplineEducationalProgramService.getAll();
        response.put("deps", deps);

        List<Competencie> competencies = competencieService.getAll();
        response.put("competencies", competencies);

        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        response.put("role", role);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/cdep/get-active/{entityId}")
    public ResponseEntity<Map<String, Object>> getActiveEntity(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        CompetenciesDisciplinesEducationalProgram cdep = competenciesDisciplinesEducationalProgramService.getById(entityId);
        response.put("data", cdep);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/cdep/save-new-record")
    public ResponseEntity<Map<String, Object>> createRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();

        int param0, param1;
        try {
            param0 = Integer.parseInt(payload.get("0"));
            param1 = Integer.parseInt(payload.get("1"));
        } catch (NumberFormatException ex) {
            response.put("error", "Неверный формат данных.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        DisciplineEducationalProgram disciplineEducationalProgram = disciplineEducationalProgramService.getById(param0);
        Competencie competencie = competencieService.getById(param1);
        if (disciplineEducationalProgram == null || competencie == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        CompetenciesDisciplinesEducationalProgram cdep = new CompetenciesDisciplinesEducationalProgram();
        cdep.setDisciplineEducationalProgram(disciplineEducationalProgram);
        cdep.setCompetencie(competencie);
        cdep.setDisabled(false);
        competenciesDisciplinesEducationalProgramService.save(cdep);

        response.put("createdData", cdep);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/cdep/delete-record/{entityId}")
    public ResponseEntity<Map<String, Object>> deleteRecord(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        CompetenciesDisciplinesEducationalProgram cdep = competenciesDisciplinesEducationalProgramService.getById(entityId);
        if (cdep == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        cdep.setDisabled(true);
        competenciesDisciplinesEducationalProgramService.save(cdep);

        response.put("deletedData", cdep.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/cdep/department-filter/{filter1}")
    public ResponseEntity<Map<String, Object>> filterByDepartment(@PathVariable Integer filter1) {
        Map<String, Object> response = new HashMap<>();

        List<Teacher> filterList = teacherService.getAll().stream()
                .filter(t -> t.getDepartment().getId() == filter1)
                .toList();
        response.put("filterList", filterList);

        List<CompetenciesDisciplinesEducationalProgram> cdeps = competenciesDisciplinesEducationalProgramService.getAll();

        if (filter1 == 0) {
            response.put("entityList", cdeps);
        } else {
            List<CompetenciesDisciplinesEducationalProgram> entityList = cdeps.stream()
                    .filter(cdep -> cdep.getDisciplineEducationalProgram().getDiscipline().getDeveloper().getDepartment().getId() == filter1)
                    .toList();
            response.put("entityList", entityList);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/cdep/developer-filter/{filter1}/{filter2}")
    public ResponseEntity<Map<String, Object>> filterByDeveloper(@PathVariable Integer filter1, @PathVariable Integer filter2) {
        Map<String, Object> response = new HashMap<>();

        List<BasicEducationalProgram> filterList = disciplineEducationalProgramService.getAll().stream()
                .filter(dep -> dep.getDiscipline().getDeveloper().getDepartment().getId() == filter1)
                .filter(dep -> dep.getDiscipline().getDeveloper().getId() == filter2)
                .map(DisciplineEducationalProgram::getBasicEducationalProgram)
                .distinct()
                .toList();
        response.put("filterList", filterList);

        List<CompetenciesDisciplinesEducationalProgram> cdeps = competenciesDisciplinesEducationalProgramService.getAll();

        if (filter2 == 0) {
            List<CompetenciesDisciplinesEducationalProgram> entityList = cdeps.stream()
                    .filter(cdep -> cdep.getDisciplineEducationalProgram().getDiscipline().getDeveloper().getDepartment().getId() == filter1)
                    .toList();
            response.put("entityList", entityList);
        } else {
            List<CompetenciesDisciplinesEducationalProgram> entityList = cdeps.stream()
                    .filter(cdep -> cdep.getDisciplineEducationalProgram().getDiscipline().getDeveloper().getDepartment().getId() == filter1)
                    .filter(cdep -> cdep.getDisciplineEducationalProgram().getDiscipline().getDeveloper().getId() == filter2)
                    .toList();
            response.put("entityList", entityList);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/cdep/oop-filter/{filter1}/{filter2}/{filter3}")
    public ResponseEntity<Map<String, Object>> filterByOOP(@PathVariable Integer filter1, @PathVariable Integer filter2, @PathVariable Integer filter3) {
        Map<String, Object> response = new HashMap<>();

        List<Discipline> filterList = disciplineEducationalProgramService.getAll().stream()
                .filter(d -> d.getDiscipline().getDeveloper().getDepartment().getId() == filter1)
                .filter(d -> d.getDiscipline().getDeveloper().getId() == filter2)
                .filter(d -> d.getBasicEducationalProgram().getId() == filter3)
                .map(DisciplineEducationalProgram::getDiscipline)
                .distinct()
                .toList();
        response.put("filterList", filterList);

        List<CompetenciesDisciplinesEducationalProgram> cdeps = competenciesDisciplinesEducationalProgramService.getAll();

        if (filter3 == 0) {
            List<CompetenciesDisciplinesEducationalProgram> entityList = cdeps.stream()
                    .filter(cdep -> cdep.getDisciplineEducationalProgram().getDiscipline().getDeveloper().getDepartment().getId() == filter1)
                    .filter(cdep -> cdep.getDisciplineEducationalProgram().getDiscipline().getDeveloper().getId() == filter2)
                    .toList();
            response.put("entityList", entityList);
        } else {
            List<CompetenciesDisciplinesEducationalProgram> entityList = cdeps.stream()
                    .filter(cdep -> cdep.getDisciplineEducationalProgram().getDiscipline().getDeveloper().getDepartment().getId() == filter1)
                    .filter(cdep -> cdep.getDisciplineEducationalProgram().getDiscipline().getDeveloper().getId() == filter2)
                    .filter(cdep -> cdep.getDisciplineEducationalProgram().getBasicEducationalProgram().getId() == filter3)
                    .toList();
            response.put("entityList", entityList);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/cdep/discipline-filter/{filter1}/{filter2}/{filter3}/{filter4}")
    public ResponseEntity<Map<String, Object>> filterByDiscipline(@PathVariable Integer filter1, @PathVariable Integer filter2, @PathVariable Integer filter3, @PathVariable Integer filter4) {
        Map<String, Object> response = new HashMap<>();

        List<CompetenciesDisciplinesEducationalProgram> cdeps = competenciesDisciplinesEducationalProgramService.getAll();

        if (filter4 == 0) {
            List<CompetenciesDisciplinesEducationalProgram> entityList = cdeps.stream()
                    .filter(cdep -> cdep.getDisciplineEducationalProgram().getDiscipline().getDeveloper().getDepartment().getId() == filter1)
                    .filter(cdep -> cdep.getDisciplineEducationalProgram().getDiscipline().getDeveloper().getId() == filter2)
                    .filter(cdep -> cdep.getDisciplineEducationalProgram().getBasicEducationalProgram().getId() == filter3)
                    .toList();
            response.put("entityList", entityList);
        } else {
            List<CompetenciesDisciplinesEducationalProgram> entityList = cdeps.stream()
                    .filter(cdep -> cdep.getDisciplineEducationalProgram().getDiscipline().getDeveloper().getDepartment().getId() == filter1)
                    .filter(cdep -> cdep.getDisciplineEducationalProgram().getDiscipline().getDeveloper().getId() == filter2)
                    .filter(cdep -> cdep.getDisciplineEducationalProgram().getBasicEducationalProgram().getId() == filter3)
                    .filter(cdep -> cdep.getDisciplineEducationalProgram().getDiscipline().getId() == filter4)
                    .toList();
            response.put("entityList", entityList);
        }
        return ResponseEntity.ok(response);
    }
}
