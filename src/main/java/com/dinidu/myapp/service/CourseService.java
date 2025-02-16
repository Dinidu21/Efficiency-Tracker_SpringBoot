package com.dinidu.myapp.service;

import com.dinidu.myapp.model.Course;
import com.dinidu.myapp.repo.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public String saveCourse(Course course) {
        String normalizedCourseName = normalizeName(course.getCourseName());
        course.setCourseName(normalizedCourseName);

        if (course.getLabCount() < 0) {
            return "Error: Lab Count must be a non-negative integer.";
        }

        if (course.getLectureHours() <= 0) {
            return "Error: Lecture Hours must be greater than zero.";
        }

        // Check if a course with the same normalized name already exists
        Optional<Course> existingCourse = courseRepository.findByNormalizedCourseName(normalizedCourseName);
        if (existingCourse.isPresent()) {
            return "Error: A course with this name already exists.";
        }

        // Save the course if validation passes
        course.setDaysSpent(course.getDaysSpent());
        courseRepository.save(course);
        return "Course added successfully!";
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    // Helper function to normalize course names
    private String normalizeName(String name) {
        return name.trim().replaceAll("\\s+", " ");
    }
}
