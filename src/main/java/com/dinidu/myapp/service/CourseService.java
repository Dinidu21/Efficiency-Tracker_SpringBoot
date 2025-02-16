package com.dinidu.myapp.service;

import com.dinidu.myapp.model.Course;
import com.dinidu.myapp.repo.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public String saveCourse(Course course) {
        if (course.getLabCount() < 0) {
            return "Error: Lab Count must be a non-negative integer (0 or greater).";
        }

        if (course.getLectureHours() < 0) {
            return "Error: Lecture Hours must be a non-negative number (0 or greater).";
        }

        if (course.getLectureHours() == 0) {
            return "Error: Lecture Hours cannot be zero.";
        }

        course.setDaysSpent(course.getDaysSpent());
        courseRepository.save(course);

        return "Course added successfully!";
    }


    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }
}
