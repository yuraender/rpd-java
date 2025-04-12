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

import java.util.*;

@Controller
@RequiredArgsConstructor
public class BasicEducationalProgramDisciplineController {

    private final BasicEducationalProgramDisciplineService basicEducationalProgramDisciplineService;
    private final DepartmentService departmentService;
    private final BasicEducationalProgramService basicEducationalProgramService;
    private final DisciplineService disciplineService;
    private final CompetenceService competenceService;
    private final IndicatorService indicatorService;
    private final AuditoriumService auditoriumService;
    private final ProtocolService protocolService;
    private final DirectionService directionService;
    private final ProfileService profileService;

    @GetMapping("/bep-disciplines")
    public String getTablePage() {
        return "basic-educational-program-disciplines";
    }

    @GetMapping("/bep-disciplines-data")
    public ResponseEntity<Map<String, Object>> getEntityData(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        List<BasicEducationalProgramDiscipline> bepDisciplines = basicEducationalProgramDisciplineService.getAll();
        response.put("data", bepDisciplines);

        List<Department> departments = departmentService.getAll();
        response.put("departments", departments);

        List<BasicEducationalProgram> beps = basicEducationalProgramService.getAll();
        response.put("beps", beps);

        List<Discipline> disciplines = disciplineService.getAll();
        response.put("disciplines", disciplines);

        List<Competence> competences = competenceService.getAll();
        response.put("competences", competences);

        List<Indicator> indicators = indicatorService.getAll();
        response.put("indicators", indicators);

        List<Indicator.Type> indicatorTypes = Arrays.stream(Indicator.Type.values()).toList();
        response.put("indicatorTypes", indicatorTypes);

        List<Auditorium> auditoriums = auditoriumService.getAll();
        response.put("auditoriums", auditoriums);

        List<Protocol> protocols = protocolService.getAllByType(Protocol.Type.ACTUALIZE);
        response.put("protocols", protocols);

        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        response.put("role", role);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/bep-discipline/set-active/{entityId}")
    public ResponseEntity<Map<String, Object>> setActive(@PathVariable Integer entityId, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        BasicEducationalProgramDiscipline bepDiscipline = basicEducationalProgramDisciplineService.getById(entityId);
        if (bepDiscipline == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        response.put("dataName", bepDiscipline.getId());

        HttpSession session = request.getSession();
        session.setAttribute("bepDisciplineId", entityId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/bep-discipline/get-active/{entityId}")
    public ResponseEntity<Map<String, Object>> getActiveEntity(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        BasicEducationalProgramDiscipline bepDiscipline = basicEducationalProgramDisciplineService.getById(entityId);
        response.put("data", bepDiscipline);

        List<Competence> competences = competenceService.getAll();
        response.put("competences", competences);

        List<Indicator> indicators = indicatorService.getAll();
        response.put("indicators", indicators);

        List<Indicator.Type> indicatorTypes = Arrays.stream(Indicator.Type.values()).toList();
        response.put("indicatorTypes", indicatorTypes);

        List<Auditorium> auditoriums = auditoriumService.getAll();
        response.put("auditoriums", auditoriums);

        List<Protocol> protocols = protocolService.getAllByType(Protocol.Type.ACTUALIZE);
        response.put("protocols", protocols);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/bep-discipline/update")
    public ResponseEntity<Map<String, Object>> updateRecord(@RequestBody Map<String, Object> payload) {
        Map<String, Object> response = new HashMap<>();

        String index = (String) payload.get("0");
        int[] param1, param2, param3, param4;
        try {
            param1 = ((List<String>) payload.get("1")).stream()
                    .mapToInt(Integer::parseInt)
                    .toArray();
            param2 = ((List<String>) payload.get("2")).stream()
                    .mapToInt(Integer::parseInt)
                    .toArray();
            param3 = ((List<String>) payload.get("3")).stream()
                    .mapToInt(Integer::parseInt)
                    .toArray();
            param4 = ((List<String>) payload.get("4")).stream()
                    .mapToInt(Integer::parseInt)
                    .toArray();
        } catch (NumberFormatException ex) {
            response.put("error", "Неверный формат данных.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        Integer dataId = Integer.parseInt((String) payload.get("dataId"));

        BasicEducationalProgramDiscipline bepDiscipline = basicEducationalProgramDisciplineService.getById(dataId);
        List<Competence> competences = Arrays.stream(param1)
                .mapToObj(competenceService::getById)
                .filter(c -> c.getBasicEducationalProgram() == null
                        || c.getBasicEducationalProgram().getId() == bepDiscipline.getBasicEducationalProgram().getId())
                .distinct()
                .toList();
        List<Indicator> indicators = Arrays.stream(param2)
                .mapToObj(indicatorService::getById)
                .filter(i -> i == null || competences.contains(i.getCompetence()))
                .distinct()
                .toList();
        List<Auditorium> auditoriums = Arrays.stream(param3)
                .mapToObj(auditoriumService::getById)
                .distinct()
                .toList();
        List<Protocol> protocols = Arrays.stream(param4)
                .mapToObj(i -> protocolService.getByIdAndType(i, Protocol.Type.ACTUALIZE))
                .distinct()
                .toList();
        if (bepDiscipline == null
                || competences.stream().anyMatch(Objects::isNull)
                || indicators.stream().anyMatch(Objects::isNull)
                || auditoriums.stream().anyMatch(Objects::isNull)
                || protocols.stream().anyMatch(Objects::isNull)) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if (protocols.stream().anyMatch(p -> p.getDate().toLocalDate().getYear()
                <= bepDiscipline.getBasicEducationalProgram().getProtocol().getDate().toLocalDate().getYear())) {
            response.put("error", "Дата актуализации должна быть позднее даты утверждения.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        bepDiscipline.setIndex(index);
        bepDiscipline.setIndicators(new ArrayList<>(indicators));
        bepDiscipline.setAuditoriums(new ArrayList<>(auditoriums));
        bepDiscipline.setProtocols(new ArrayList<>(protocols));
        bepDiscipline.setDisabled(false);
        basicEducationalProgramDisciplineService.save(bepDiscipline);

        response.put("updatedData", bepDiscipline);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/bep-discipline/save-new-record")
    public ResponseEntity<Map<String, Object>> createRecord(@RequestBody Map<String, Object> payload) {
        Map<String, Object> response = new HashMap<>();

        int param0, param2;
        String index = (String) payload.get("1");
        int[] param3, param4, param5, param6;
        try {
            param0 = Integer.parseInt((String) payload.get("0"));
            param2 = Integer.parseInt((String) payload.get("2"));
            param3 = ((List<String>) payload.get("3")).stream()
                    .mapToInt(Integer::parseInt)
                    .toArray();
            param4 = ((List<String>) payload.get("4")).stream()
                    .mapToInt(Integer::parseInt)
                    .toArray();
            param5 = ((List<String>) payload.get("5")).stream()
                    .mapToInt(Integer::parseInt)
                    .toArray();
            param6 = ((List<String>) payload.get("6")).stream()
                    .mapToInt(Integer::parseInt)
                    .toArray();
        } catch (NumberFormatException ex) {
            response.put("error", "Неверный формат данных.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        BasicEducationalProgram bep = basicEducationalProgramService.getById(param0);
        Discipline discipline = disciplineService.getById(param2);
        List<Competence> competences = Arrays.stream(param3)
                .mapToObj(competenceService::getById)
                .filter(c -> c.getBasicEducationalProgram() == null
                        || bep == null
                        || c.getBasicEducationalProgram().getId() == bep.getId())
                .distinct()
                .toList();
        List<Indicator> indicators = Arrays.stream(param4)
                .mapToObj(indicatorService::getById)
                .filter(i -> i == null || competences.contains(i.getCompetence()))
                .distinct()
                .toList();
        List<Auditorium> auditoriums = Arrays.stream(param5)
                .mapToObj(auditoriumService::getById)
                .distinct()
                .toList();
        List<Protocol> protocols = Arrays.stream(param6)
                .mapToObj(i -> protocolService.getByIdAndType(i, Protocol.Type.ACTUALIZE))
                .distinct()
                .toList();
        if (bep == null || discipline == null
                || competences.stream().anyMatch(Objects::isNull)
                || indicators.stream().anyMatch(Objects::isNull)
                || auditoriums.stream().anyMatch(Objects::isNull)
                || protocols.stream().anyMatch(Objects::isNull)) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if (protocols.stream().anyMatch(p ->
                p.getDate().toLocalDate().getYear() <= bep.getProtocol().getDate().toLocalDate().getYear())) {
            response.put("error", "Дата актуализации должна быть позднее даты утверждения.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        BasicEducationalProgramDiscipline bepDiscipline = new BasicEducationalProgramDiscipline();
        bepDiscipline.setIndex(index);
        bepDiscipline.setBasicEducationalProgram(bep);
        bepDiscipline.setDiscipline(discipline);
        bepDiscipline.setIndicators(new ArrayList<>(indicators));
        bepDiscipline.setAuditoriums(new ArrayList<>(auditoriums));
        bepDiscipline.setProtocols(new ArrayList<>(protocols));
        bepDiscipline.setDisabled(false);
        basicEducationalProgramDisciplineService.save(bepDiscipline);

        response.put("createdData", bepDiscipline);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/bep-discipline/delete-record/{entityId}")
    public ResponseEntity<Map<String, Object>> deleteRecord(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        BasicEducationalProgramDiscipline bepDiscipline = basicEducationalProgramDisciplineService.getById(entityId);
        if (bepDiscipline == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        bepDiscipline.setDisabled(true);
        basicEducationalProgramDisciplineService.save(bepDiscipline);

        response.put("deletedData", bepDiscipline.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/bep-discipline/department-filter/{filter1}")
    public ResponseEntity<Map<String, Object>> filterByDepartment(@PathVariable Integer filter1) {
        Map<String, Object> response = new HashMap<>();

        List<Direction> filterList = directionService.getAll().stream()
                .filter(d -> d.getDepartment().getId() == filter1)
                .toList();
        response.put("filterList", filterList);

        List<BasicEducationalProgramDiscipline> bepDisciplines = basicEducationalProgramDisciplineService.getAll();

        if (filter1 == 0) {
            response.put("entityList", bepDisciplines);
        } else {
            List<BasicEducationalProgramDiscipline> entityList = bepDisciplines.stream()
                    .filter(bepd -> bepd.getBasicEducationalProgram().getProfile().getDirection().getDepartment().getId() == filter1)
                    .toList();
            response.put("entityList", entityList);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/bep-discipline/direction-filter/{filter1}/{filter2}")
    public ResponseEntity<Map<String, Object>> filterByDirection(@PathVariable Integer filter1, @PathVariable Integer filter2) {
        Map<String, Object> response = new HashMap<>();

        List<Profile> filterList = profileService.getAll().stream()
                .filter(p -> p.getDirection().getDepartment().getId() == filter1)
                .filter(p -> p.getDirection().getId() == filter2)
                .toList();
        response.put("filterList", filterList);

        List<BasicEducationalProgramDiscipline> bepDisciplines = basicEducationalProgramDisciplineService.getAll();

        if (filter2 == 0) {
            List<BasicEducationalProgramDiscipline> entityList = bepDisciplines.stream()
                    .filter(bepd -> bepd.getBasicEducationalProgram().getProfile().getDirection().getDepartment().getId() == filter1)
                    .toList();
            response.put("entityList", entityList);
        } else {
            List<BasicEducationalProgramDiscipline> entityList = bepDisciplines.stream()
                    .filter(bepd -> bepd.getBasicEducationalProgram().getProfile().getDirection().getDepartment().getId() == filter1)
                    .filter(bepd -> bepd.getBasicEducationalProgram().getProfile().getDirection().getId() == filter2)
                    .toList();
            response.put("entityList", entityList);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/bep-discipline/profile-filter/{filter1}/{filter2}/{filter3}")
    public ResponseEntity<Map<String, Object>> filterByProfile(@PathVariable Integer filter1, @PathVariable Integer filter2, @PathVariable Integer filter3) {
        Map<String, Object> response = new HashMap<>();

        List<BasicEducationalProgramDiscipline> bepDisciplines = basicEducationalProgramDisciplineService.getAll();

        if (filter3 == 0) {
            List<BasicEducationalProgramDiscipline> entityList = bepDisciplines.stream()
                    .filter(bepd -> bepd.getBasicEducationalProgram().getProfile().getDirection().getDepartment().getId() == filter1)
                    .filter(bepd -> bepd.getBasicEducationalProgram().getProfile().getDirection().getId() == filter2)
                    .toList();
            response.put("entityList", entityList);
        } else {
            List<BasicEducationalProgramDiscipline> entityList = bepDisciplines.stream()
                    .filter(bepd -> bepd.getBasicEducationalProgram().getProfile().getDirection().getDepartment().getId() == filter1)
                    .filter(bepd -> bepd.getBasicEducationalProgram().getProfile().getDirection().getId() == filter2)
                    .filter(bepd -> bepd.getBasicEducationalProgram().getProfile().getId() == filter3)
                    .toList();
            response.put("entityList", entityList);
        }
        return ResponseEntity.ok(response);
    }
}
