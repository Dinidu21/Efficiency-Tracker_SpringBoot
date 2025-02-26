package com.dinidu.myapp.controller;

import com.dinidu.myapp.model.entity.CourseDetails;
import com.dinidu.myapp.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public String getDashboard(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());
        return "dashboard";
    }

    @GetMapping("/add")
    public String showAddCourseForm(Model model) {
        model.addAttribute("course", new CourseDetails());
        return "add-course";
    }

    @PostMapping("/save")
    public String saveCourse(@Validated @ModelAttribute("course") CourseDetails course,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "add-course"; // Return to form with errors
        }
        courseService.saveCourse(course);
        return "redirect:/courses";
    }

    @GetMapping("/edit/{courseCode}")
    public String showEditCourseForm(@PathVariable String courseCode, Model model) {
        CourseDetails course = courseService.getCourseByCode(courseCode);
        model.addAttribute("course", course);
        return "edit-course";
    }

    @PostMapping("/update/{courseCode}")
    public String updateCourse(@PathVariable String courseCode,
                               @Validated @ModelAttribute("course") CourseDetails course,
                               BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "edit-course"; // Return to form with errors
        }
        courseService.updateCourse(courseCode, course);
        return "redirect:/courses";
    }

    @GetMapping("/delete/{courseCode}")
    public String deleteCourse(@PathVariable String courseCode) {
        courseService.deleteCourse(courseCode);
        return "redirect:/courses";
    }
}