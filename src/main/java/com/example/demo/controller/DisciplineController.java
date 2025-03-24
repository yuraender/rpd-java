package com.example.demo.controller;

import com.example.demo.entity.Discipline;
import com.example.demo.entity.Teacher;
import com.example.demo.service.DisciplineService;
import com.example.demo.service.TeacherService;
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
public class DisciplineController {

    private final DisciplineService disciplineService;
    private final TeacherService teacherService;

    @GetMapping("/disciplines")
    public String getTablePage() {
        return "disciplines";
    }

    @GetMapping("/disciplines-data")
    public ResponseEntity<Map<String, Object>> getEntityData(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        List<Discipline> disciplines = disciplineService.getAll();
        response.put("data", disciplines);

        List<Teacher> teachers = teacherService.getAll();
        response.put("teachers", teachers);

        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        response.put("role", role);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/discipline/get-active/{entityId}")
    public ResponseEntity<Map<String, Object>> getActiveEntity(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        Discipline discipline = disciplineService.getById(entityId);
        response.put("data", discipline);

        List<Teacher> teachers = teacherService.getAll();
        response.put("teachers", teachers);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/discipline/update")
    public ResponseEntity<Map<String, Object>> updateRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();

        String index = payload.get("0");
        String name = payload.get("1");
        int param2;
        try {
            param2 = Integer.parseInt(payload.get("2"));
        } catch (NumberFormatException ex) {
            response.put("error", "Неверный формат данных.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        Integer dataId = Integer.parseInt(payload.get("dataId"));

        Discipline discipline = disciplineService.getById(dataId);
        Teacher developer = teacherService.getById(param2);
        if (discipline == null || developer == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        discipline.setIndex(index);
        discipline.setName(name);
        discipline.setDeveloper(developer);
        discipline.setDisabled(false);
        disciplineService.save(discipline);

        response.put("updatedData", discipline);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/discipline/save-new-record")
    public ResponseEntity<Map<String, Object>> createRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();

        String index = payload.get("0");
        String name = payload.get("1");
        int param2;
        try {
            param2 = Integer.parseInt(payload.get("2"));
        } catch (NumberFormatException ex) {
            response.put("error", "Неверный формат данных.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Teacher developer = teacherService.getById(param2);
        if (developer == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Discipline discipline = new Discipline();
        discipline.setIndex(index);
        discipline.setName(name);
        discipline.setDeveloper(developer);
        discipline.setDisabled(false);
        disciplineService.save(discipline);

        response.put("createdData", discipline);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/discipline/delete-record/{entityId}")
    public ResponseEntity<Map<String, Object>> deleteRecord(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        Discipline discipline = disciplineService.getById(entityId);
        if (discipline == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        discipline.setDisabled(true);
        disciplineService.save(discipline);

        response.put("deletedData", discipline.getId());
        return ResponseEntity.ok(response);
    }
}
