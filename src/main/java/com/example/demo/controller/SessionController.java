package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/session")
public class SessionController {

    @GetMapping("/instituteId")
    @ResponseBody
    public ResponseEntity<Long> getInstituteId(@SessionAttribute(name = "instituteId", required = false) Long instituteId) {
        if (instituteId != null) {
            return ResponseEntity.ok(instituteId);
        } else {
            return ResponseEntity.ok(0L);
        }
    }

    @PostMapping("/instituteId")
    public void setInstituteId(@RequestBody Map<String, String> payload, HttpSession session) {
        String id = payload.get("id");
        if (id != null) {
            session.setAttribute("instituteId", Long.parseLong(id));
        }
    }

    @GetMapping("/activeInstitute")
    public ResponseEntity<String> getActiveInstitute(HttpSession session) {
        String activeInstitute = (String) session.getAttribute("activeInstitute");
        return ResponseEntity.ok(activeInstitute);
    }
}
