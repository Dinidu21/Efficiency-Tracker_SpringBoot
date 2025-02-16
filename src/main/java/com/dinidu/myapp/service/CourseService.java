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
        List<Course> courses = courseRepository.findAll();
        for (Course course : courses) {
            double totalHours = course.getLectureHours() + (course.getLabCount() * 0.5);
            course.setTotalSpentTime(totalHours);
        }
        return courses;
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

        double totalHours = course.getLectureHours() + (course.getLabCount() * 0.5);
        course.setTotalSpentTime(totalHours);

        courseRepository.save(course);
        return "Course added successfully!";
    }

    private String normalizeName(String courseName) {
        return courseName;
    }

    public String getWeeklyEfficiencyChange() {
        List<Course> courses = getAllCourses();
        if (courses.size() < 2) return "Not enough data for comparison.";
        return "Not Implemented !!";
    }
    public void deleteCourse(Long id) {
        System.out.println("not implemented yet");
    }
}
