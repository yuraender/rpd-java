package com.example.demo.controller;

import com.example.demo.entity.Institute;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    @GetMapping("/api/session/departmentId")
    @ResponseBody
    public Long getDepartmentId(@SessionAttribute(name = "departmentId", required = false) Long departmentId) {
        System.out.println("Department ID from session: " + departmentId);
        return departmentId != null ? departmentId : 0;
    }

    @PostMapping("/api/session/departmentId")
    public void setDepartmentId(@RequestBody Map<String, String> payload, HttpSession session) {
        String id = payload.get("id");
        System.out.println("DepartmentId: " + id);
        if (id != null) {
            session.setAttribute("departmentId", Long.parseLong(id));
            System.out.println("DepartmentId Save: " + id);
        }
    }

//    @GetMapping("/background-information")
//    public String getBackgroundInformation(HttpSession session, Model model) {
//        List<Institute> institutes = instituteService.findAll();
//        List<Department> departments = departmentService.findAll();
//
//        Long activeInstituteId = (Long) session.getAttribute("instituteId");
//        Institute activeInstitute = activeInstituteId != null ? instituteService.findById(activeInstituteId) : null;
//
//        Long activeDepartmentId = (Long) session.getAttribute("activeDepartmentId");
//        Department activeDepartment = activeDepartmentId != null ? departmentService.findById(activeDepartmentId) : null;
//
//        model.addAttribute("institutes", institutes);
//        model.addAttribute("departments", departments);
//        model.addAttribute("activeInstitute", activeInstitute);
//        model.addAttribute("activeDepartment", activeDepartment);
//
//        return "background-information";
//    }
}
