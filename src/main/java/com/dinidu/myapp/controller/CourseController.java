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
        logger.info("Fetching all courses for dashboard.");
        model.addAttribute("courses", courseService.getAllCourses());
        return "dashboard";
    }

    @GetMapping("/add")
    public String showAddCourseForm(Model model) {
        logger.info("Showing add course form.");
        CourseDetails course = new CourseDetails();
        course.setCourseCode(courseService.generateNextCourseCode());
        model.addAttribute("course", course);
        return "add-course";
    }

    @PostMapping("/save")
    public String saveCourse(@Validated @ModelAttribute("course") CourseDetails course,
                             BindingResult bindingResult, Model model) {
        logger.info("Attempting to save course: {}", course.getCourseCode());
        if (bindingResult.hasErrors()) {
            logger.warn("Validation errors occurred while saving course: {}", course.getCourseCode());
            return "add-course";
        }

        if (courseService.existsByCourseCode(course.getCourseCode())) {
            logger.warn("Course code already exists: {}", course.getCourseCode());
            bindingResult.rejectValue("courseCode", "error.course", "Course code already exists.");
            return "add-course";
        }

        courseService.saveCourse(course);
        logger.info("New course added: {}", course.getCourseCode());
        return "redirect:/courses";
    }

    @GetMapping("/edit/{courseCode}")
    public String showEditCourseForm(@PathVariable("courseCode") String courseCode, Model model) {
        logger.info("Fetching course for editing: {}", courseCode);
        Optional<CourseDetails> course = Optional.ofNullable(courseService.getCourseByCode(courseCode));
        if (course.isEmpty()) {
            logger.error("Course not found: {}", courseCode);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found");
        }
        model.addAttribute("course", course.get());
        return "edit-course";
    }

    @PostMapping("/update/{courseCode}")
    public String updateCourse(@PathVariable("courseCode") String courseCode,
                               @Validated  @ModelAttribute("course") CourseDetails course,
                               BindingResult bindingResult, Model model) {
        logger.info("Attempting to update course: {}", courseCode);
        if (bindingResult.hasErrors()) {
            logger.warn("Validation errors occurred while updating course: {}", courseCode);
            return "edit-course";
        }

        if (!courseService.existsByCourseCode(courseCode)) {
            logger.error("Course not found: {} Update", courseCode);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found");
        }

        courseService.updateCourse(courseCode, course);
        logger.info("Course updated: {}", courseCode);
        return "redirect:/courses";
    }

    @DeleteMapping("/delete/{courseCode}")
    public String deleteCourse(@PathVariable("courseCode") String courseCode) {
        logger.info("Attempting to delete course: {}", courseCode);
        if (!courseService.existsByCourseCode(courseCode)) {
            logger.error("Course not found: {} Delete", courseCode);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found");
        }

        courseService.deleteCourse(courseCode);
        logger.warn("Course deleted: {}", courseCode);
        return "redirect:/courses";
    }
}
