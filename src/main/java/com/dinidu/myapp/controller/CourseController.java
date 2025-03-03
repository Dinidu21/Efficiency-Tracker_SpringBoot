package com.dinidu.myapp.controller;

import com.dinidu.myapp.model.entity.CourseDetails;
import com.dinidu.myapp.service.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Controller
@RequestMapping("/courses")
public class CourseController {
    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

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
        CourseDetails course = new CourseDetails();
        course.setCourseCode(courseService.generateNextCourseCode());
        model.addAttribute("course", course);
        return "add-course";
    }

    @PostMapping("/save")
    public String saveCourse(@Validated @ModelAttribute("course") CourseDetails course,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "add-course";
        }

        if (courseService.existsByCourseCode(course.getCourseCode())) {
            bindingResult.rejectValue("courseCode", "error.course", "Course code already exists.");
            return "add-course";
        }

        courseService.saveCourse(course);
        logger.info("New course added: {}", course.getCourseCode());
        return "redirect:/courses";
    }

    @GetMapping("/edit/{courseCode}")
    public String showEditCourseForm(@PathVariable("courseCode") String courseCode, Model model) {
        Optional<CourseDetails> course = Optional.ofNullable(courseService.getCourseByCode(courseCode));
        if (course.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found");
        }
        model.addAttribute("course", course.get());
        return "edit-course";
    }

    @PostMapping("/update/{courseCode}")
    public String updateCourse(@PathVariable("courseCode") String courseCode,
                               @Validated  @ModelAttribute("course") CourseDetails course,
                               BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "edit-course";
        }

        if (!courseService.existsByCourseCode(courseCode)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found");
        }

        courseService.updateCourse(courseCode, course);
        logger.info("Course updated: {}", courseCode);
        return "redirect:/courses";
    }

    @DeleteMapping("/delete/{courseCode}")
    public String deleteCourse(@PathVariable("courseCode") String courseCode) {
        if (!courseService.existsByCourseCode(courseCode)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found");
        }

        courseService.deleteCourse(courseCode);
        logger.warn("Course deleted: {}", courseCode);
        return "redirect:/courses";
    }
}
