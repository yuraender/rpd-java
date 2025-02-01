package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/session")
public class SessionController {

    @GetMapping("/instituteId")
    @ResponseBody
    public ResponseEntity<Integer> getInstituteId(@SessionAttribute(name = "instituteId", required = false) Integer instituteId) {
        if (instituteId != null) {
            return ResponseEntity.ok(instituteId);
        } else {
            return ResponseEntity.ok(0);
        }
    }

    @PostMapping("/instituteId")
    public void setInstituteId(@RequestBody Map<String, String> payload, HttpSession session) {
        String id = payload.get("id");
        if (id != null) {
            session.setAttribute("instituteId", Integer.parseInt(id));
        }
    }

    @GetMapping("/activeInstitute")
    public ResponseEntity<String> getActiveInstitute(HttpSession session) {
        String activeInstitute = (String) session.getAttribute("activeInstitute");
        return ResponseEntity.ok(activeInstitute);
    }

    @GetMapping("/api/session/departmentId")
    @ResponseBody
    public Integer getDepartmentId(@SessionAttribute(name = "departmentId", required = false) Integer departmentId) {
        System.out.println("Department ID from session: " + departmentId);
        return departmentId != null ? departmentId : 0;
    }

    @PostMapping("/api/session/departmentId")
    public void setDepartmentId(@RequestBody Map<String, String> payload, HttpSession session) {
        String id = payload.get("id");
        System.out.println("DepartmentId: " + id);
        if (id != null) {
            session.setAttribute("departmentId", Integer.parseInt(id));
            System.out.println("DepartmentId Save: " + id);
        }
    }
}
