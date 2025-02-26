package com.dinidu.myapp.controller;

import com.dinidu.myapp.model.entity.CourseDetails;
import com.dinidu.myapp.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    // Dashboard - List all courses
    @GetMapping
    public String getDashboard(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());
        return "dashboard";
    }

    // Show add course form
    @GetMapping("/add")
    public String showAddCourseForm(Model model) {
        model.addAttribute("courses", new CourseDetails());
        return "add-course";
    }

    // Save new course
    @PostMapping("/save")
    public String saveCourse(@ModelAttribute("courses") CourseDetails course) {
        courseService.saveCourse(course);
        return "redirect:/courses";
    }

    // Show edit course form
    @GetMapping("/edit/{courseCode}")
    public String showEditCourseForm(@PathVariable String courseCode, Model model) {
        CourseDetails course = courseService.getCourseByCode(courseCode);
        model.addAttribute("course", course);
        return "edit-course";
    }

    // Update course
    @PostMapping("/update/{courseCode}")
    public String updateCourse(@PathVariable String courseCode, @ModelAttribute("course") CourseDetails course) {
        courseService.updateCourse(courseCode, course);
        return "redirect:/courses";
    }

    // Delete course
    @GetMapping("/delete/{courseCode}")
    public String deleteCourse(@PathVariable String courseCode) {
        courseService.deleteCourse(courseCode);
        return "redirect:/courses";
    }
}