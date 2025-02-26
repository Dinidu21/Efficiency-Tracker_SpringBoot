package com.dinidu.myapp.service;

import com.dinidu.myapp.exception.ResourceNotFoundException;
import com.dinidu.myapp.model.entity.CourseDetails;
import com.dinidu.myapp.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<CourseDetails> getAllCourses() {
        return courseRepository.findAll();
    }

    public CourseDetails getCourseByCode(String courseCode) {
        return courseRepository.findById(courseCode)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with code: " + courseCode));
    }

    public CourseDetails saveCourse(CourseDetails course) {
        return courseRepository.save(course);
    }

    public CourseDetails updateCourse(String courseCode, CourseDetails courseDetails) {
        CourseDetails existingCourse = getCourseByCode(courseCode);
        existingCourse.setCourseName(courseDetails.getCourseName());
        existingCourse.setLabCount(courseDetails.getLabCount());
        existingCourse.setLecHours(courseDetails.getLecHours());
        existingCourse.setStartDate(courseDetails.getStartDate());
        existingCourse.setEndDate(courseDetails.getEndDate());
        return courseRepository.save(existingCourse);
    }

    public void deleteCourse(String courseCode) {
        CourseDetails course = getCourseByCode(courseCode);
        courseRepository.delete(course);
    }
}