package com.example.demo.controller;

import com.example.demo.entity.Direction;
import com.example.demo.entity.Profile;
import com.example.demo.service.DirectionService;
import com.example.demo.service.ProfileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private DirectionService directionService;

    @GetMapping("/profiles")
    public String getTablePage(Model model) {
        return "profiles";
    }

    @GetMapping("/profiles-data-set-active/{entityId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> setActive(@PathVariable Integer entityId, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        Profile entity = profileService.getById(entityId);
        if (entity == null) {
            response.put("error", "Запись не найдена");
            return ResponseEntity.ok(response);
        }
        response.put("dataName", entity.getName());

        HttpSession session = request.getSession();
        session.setAttribute("profileId", entityId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profiles-data")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getEntityData(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        HttpSession session = request.getSession();
        List<Profile> profiles = profileService.getAllProfiles();
        response.put("data", profiles);
        List<Direction> directions = directionService.getAllDirections();
        response.put("directions", directions);
        String role = (String) session.getAttribute("role");
        response.put("role", role);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/profile/get-active/{entityId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getActiveEntity(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();
        Profile profile = profileService.getById(entityId);
        response.put("data", profile);
        List<Direction> directions = directionService.getAllDirections();
        response.put("directions", directions);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/profile/update")
    public ResponseEntity<Map<String, Object>> updateRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();
        String param1 = payload.get("0");
        Integer param2 = Integer.parseInt(payload.get("1"));
        Integer dataId = Integer.parseInt(payload.get("dataId"));

        Profile entity = profileService.getById(dataId);
        Direction direction = directionService.getById(param2);

        if (entity == null || direction == null) {
            response.put("error", "Запись не найдена. Запись не обновлена.");
            return ResponseEntity.ok(response);
        }
        // Обновляем поле audience у Department
        entity.setName(param1);
        entity.setDirection(direction);
        entity.setDisabled(false);
        // Сохраняем обновленную запись
        profileService.save(entity);
        // Добавляем обновленную запись в ответ
        response.put("updatedData", entity);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/profile/save-new-record")
    public ResponseEntity<Map<String, Object>> createRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();
        String param1 = payload.get("0");
        Integer param2 = Integer.parseInt(payload.get("1"));
        Direction direction = directionService.getById(param2);

        if (direction == null) {
            response.put("error", "Запись не найдена. Запись не обновлена.");
            return ResponseEntity.ok(response);
        }

        Profile entity = new Profile();
        entity.setName(param1);
        entity.setDirection(direction);
        entity.setDisabled(false);
        // Сохраняем обновленную запись
        profileService.save(entity);
        // Добавляем обновленную запись в ответ
        response.put("createdData", entity);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/profile/delete-record/{entityId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteRecord(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        // Получаем запись TechSupport по techSupportId
        Profile entity = profileService.getById(entityId);
        if (entity == null) {
            response.put("error", "Запись не найдена");
            return ResponseEntity.ok(response);
        }
        entity.setDisabled(true);
        profileService.save(entity);
        response.put("deletedData", entity.getId());
        return ResponseEntity.ok(response);
    }
}
