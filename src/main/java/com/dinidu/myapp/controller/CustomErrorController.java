package com.dinidu.myapp.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");

        if (statusCode != null) {
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("errorMessage", "Oops! The page you're looking for doesn't exist.");
                return "error-404";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("errorMessage", "Something went wrong on our end.");
                return "error-500";
            }
        }

        model.addAttribute("errorMessage", "An unexpected error occurred.");
        return "error";
    }
}
