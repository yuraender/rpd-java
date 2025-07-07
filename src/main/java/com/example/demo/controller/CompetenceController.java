package com.example.demo.controller;

import com.example.demo.entity.BasicEducationalProgram;
import com.example.demo.entity.Competence;
import com.example.demo.service.BasicEducationalProgramService;
import com.example.demo.service.CompetenceService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class CompetenceController {

    private final CompetenceService competenceService;
    private final BasicEducationalProgramService basicEducationalProgramService;

    @GetMapping("/competences")
    public String getTablePage() {
        return "competences";
    }

    @GetMapping("/competences-data")
    public ResponseEntity<Map<String, Object>> getEntityData(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        List<Competence> competences = competenceService.getAll();
        response.put("data", competences);

        List<Competence.Type> types = Arrays.stream(Competence.Type.values()).toList();
        response.put("types", types);

        List<BasicEducationalProgram> beps = basicEducationalProgramService.getAll();
        response.put("beps", beps);

        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        response.put("role", role);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/competence/get-active/{entityId}")
    public ResponseEntity<Map<String, Object>> getActiveEntity(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        Competence competence = competenceService.getById(entityId);
        response.put("data", competence);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("@userDetailsServiceImpl.isAdmin(principal)")
    @PostMapping("/api/competence/update")
    public ResponseEntity<Map<String, Object>> updateRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();

        String index = payload.get("0");
        String essence = payload.get("1");
        Integer dataId = Integer.parseInt(payload.get("dataId"));

        Competence competence = competenceService.getById(dataId);
        if (competence == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if (competenceService.existsByIndexAndBasicEducationalProgram(
                competence.getId(), index, competence.getBasicEducationalProgram())) {
            response.put("error", "Запись уже существует.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        competence.setIndex(index);
        competence.setEssence(essence);
        competence.setDisabled(false);
        competenceService.save(competence);

        response.put("updatedData", competence);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("@userDetailsServiceImpl.isAdmin(principal)")
    @PutMapping("/api/competence/save-new-record")
    public ResponseEntity<Map<String, Object>> createRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();

        String index = payload.get("0");
        String essence = payload.get("1");
        int param2, param3;
        try {
            param2 = Integer.parseInt(payload.get("2"));
            param3 = Integer.parseInt(payload.get("3"));
        } catch (NumberFormatException ex) {
            response.put("error", "Неверный формат данных.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        BasicEducationalProgram bep = basicEducationalProgramService.getById(param3);
        if (competenceService.existsByIndexAndBasicEducationalProgram(null, index, bep)) {
            response.put("error", "Запись уже существует.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        if (param2 < 0 || param2 >= Competence.Type.values().length) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        Competence.Type type = Competence.Type.values()[param2];
        if (type != Competence.Type.U && bep == null) {
            response.put("error", "Данная компетенция должна относиться к ООП.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Competence competence = new Competence();
        competence.setIndex(index);
        competence.setEssence(essence);
        competence.setType(type);
        competence.setBasicEducationalProgram(bep);
        competence.setDisabled(false);
        competenceService.save(competence);

        response.put("createdData", competence);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("@userDetailsServiceImpl.isAdmin(principal)")
    @DeleteMapping("/api/competence/delete-record/{entityId}")
    public ResponseEntity<Map<String, Object>> deleteRecord(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        Competence competence = competenceService.getById(entityId);
        if (competence == null) {
            response.put("error", "Запись не найдена");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        competence.setDisabled(true);
        competenceService.save(competence);

        response.put("deletedData", competence.getId());
        return ResponseEntity.ok(response);
    }
}
