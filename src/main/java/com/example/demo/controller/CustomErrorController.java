package com.example.demo.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(Model model, HttpServletRequest request) {
        HttpStatus status = getStatus(request);
        model.addAttribute("code", status.value());
        if (status == HttpStatus.NOT_FOUND) {
            model.addAttribute("message", "Страница не найдена");
            model.addAttribute("message2", "");
        } else if (status == HttpStatus.INTERNAL_SERVER_ERROR) {
            model.addAttribute("message", "Ошибка сервера");
            model.addAttribute("message2", "Пожалуйста, попробуйте позже.");
        } else {
            model.addAttribute("code", "Ошибка");
            model.addAttribute("message", "Что-то пошло не так...");
            model.addAttribute("message2", "Пожалуйста, попробуйте позже.");
        }
        return "error";
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
        if (statusCode != null) {
            return HttpStatus.valueOf(statusCode);
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
