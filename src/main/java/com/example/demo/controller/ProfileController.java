package com.example.demo.controller;

import com.example.demo.entity.Direction;
import com.example.demo.entity.Profile;
import com.example.demo.service.DirectionService;
import com.example.demo.service.ProfileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final DirectionService directionService;

    @GetMapping("/profiles")
    public String getTablePage() {
        return "profiles";
    }

    @PostMapping("/api/profile/set-active/{entityId}")
    public ResponseEntity<Map<String, Object>> setActive(@PathVariable Integer entityId, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        Profile profile = profileService.getById(entityId);
        if (profile == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        response.put("dataName", profile.getName());

        HttpSession session = request.getSession();
        session.setAttribute("profileId", entityId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profiles-data")
    public ResponseEntity<Map<String, Object>> getEntityData(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        List<Profile> profiles = profileService.getAll();
        response.put("data", profiles);

        List<Direction> directions = directionService.getAll();
        response.put("directions", directions);

        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");
        response.put("role", role);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/profile/get-active/{entityId}")
    public ResponseEntity<Map<String, Object>> getActiveEntity(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        Profile profile = profileService.getById(entityId);
        response.put("data", profile);

        List<Direction> directions = directionService.getAll();
        response.put("directions", directions);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("@userDetailsServiceImpl.isAdmin(principal)")
    @PostMapping("/api/profile/update")
    public ResponseEntity<Map<String, Object>> updateRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();

        String name = payload.get("0");
        int param1;
        try {
            param1 = Integer.parseInt(payload.get("1"));
        } catch (NumberFormatException ex) {
            response.put("error", "Неверный формат данных.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        Integer dataId = Integer.parseInt(payload.get("dataId"));

        Profile profile = profileService.getById(dataId);
        Direction direction = directionService.getById(param1);
        if (profile == null || direction == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if (profileService.existsByName(profile.getId(), name)) {
            response.put("error", "Запись уже существует.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        profile.setName(name);
        profile.setDirection(direction);
        profile.setDisabled(false);
        profileService.save(profile);

        response.put("updatedData", profile);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("@userDetailsServiceImpl.isAdmin(principal)")
    @PutMapping("/api/profile/save-new-record")
    public ResponseEntity<Map<String, Object>> createRecord(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();

        String name = payload.get("0");
        int param1;
        try {
            param1 = Integer.parseInt(payload.get("1"));
        } catch (NumberFormatException ex) {
            response.put("error", "Неверный формат данных.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if (profileService.existsByName(null, name)) {
            response.put("error", "Запись уже существует.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        Direction direction = directionService.getById(param1);
        if (direction == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Profile profile = new Profile();
        profile.setName(name);
        profile.setDirection(direction);
        profile.setDisabled(false);
        profileService.save(profile);

        response.put("createdData", profile);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("@userDetailsServiceImpl.isAdmin(principal)")
    @DeleteMapping("/api/profile/delete-record/{entityId}")
    public ResponseEntity<Map<String, Object>> deleteRecord(@PathVariable Integer entityId) {
        Map<String, Object> response = new HashMap<>();

        Profile profile = profileService.getById(entityId);
        if (profile == null) {
            response.put("error", "Запись не найдена.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        profile.setDisabled(true);
        profileService.save(profile);

        response.put("deletedData", profile.getId());
        return ResponseEntity.ok(response);
    }
}
