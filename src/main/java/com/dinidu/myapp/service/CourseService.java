package com.dinidu.myapp.service;

import com.dinidu.myapp.exception.ResourceNotFoundException;
import com.dinidu.myapp.model.entity.CourseDetails;
import com.dinidu.myapp.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<CourseDetails> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(CourseDetails::getStartDate))
                .collect(Collectors.toList());
    }


    public CourseDetails getCourseByCode(String courseCode) {
        return courseRepository.findById(courseCode)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with code: " + courseCode));
    }

    public void saveCourse(CourseDetails course) {
        courseRepository.save(course);
    }

    public void updateCourse(String courseCode, CourseDetails courseDetails) {
        CourseDetails existingCourse = getCourseByCode(courseCode);
        existingCourse.setCourseName(courseDetails.getCourseName());
        existingCourse.setLabCount(courseDetails.getLabCount());
        existingCourse.setLecHours(courseDetails.getLecHours());
        existingCourse.setStartDate(courseDetails.getStartDate());
        existingCourse.setEndDate(courseDetails.getEndDate());
        courseRepository.save(existingCourse);
    }

    public void deleteCourse(String courseCode) {
        CourseDetails course = getCourseByCode(courseCode);
        courseRepository.delete(course);
    }

    public String generateNextCourseCode() {
        List<CourseDetails> allCourses = courseRepository.findAll();
        if (allCourses.isEmpty()) {
            return "C001"; // Start with C001 if no courses exist
        }

        // Find the highest course code
        String highestCode = allCourses.stream()
                .map(CourseDetails::getCourseCode)
                .filter(code -> code.startsWith("C") && code.length() == 4)
                .max(String::compareTo)
                .orElse("C000");

        // Extract the numeric part and increment
        int number = Integer.parseInt(highestCode.substring(1)); // Remove "C" and parse
        number++;

        // Format as CXXX (e.g., C001, C002, etc.)
        return String.format("C%03d", number);
    }
}