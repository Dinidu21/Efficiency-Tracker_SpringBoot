package com.dinidu.myapp.service;

import com.dinidu.myapp.exception.ResourceNotFoundException;
import com.dinidu.myapp.model.entity.CourseDetails;
import com.dinidu.myapp.repository.CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {
    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<CourseDetails> getAllCourses() {
        logger.info("Fetching all courses from the database.");
        return courseRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(CourseDetails::getStartDate))
                .collect(Collectors.toList());
    }

    public CourseDetails getCourseByCode(String courseCode) {
        logger.info("Fetching course with code: {}", courseCode);
        return courseRepository.findById(courseCode)
                .orElseThrow(() -> {
                    logger.error("Course not found with code: {}", courseCode);
                    return new ResourceNotFoundException("Course not found with code: " + courseCode);
                });
    }

    public void saveCourse(CourseDetails course) {
        logger.info("Saving new course: {}", course.getCourseCode());
        courseRepository.save(course);
    }

    public void updateCourse(String courseCode, CourseDetails courseDetails) {
        logger.info("Updating course with code: {}", courseCode);
        CourseDetails existingCourse = getCourseByCode(courseCode);
        existingCourse.setCourseName(courseDetails.getCourseName());
        existingCourse.setLabCount(courseDetails.getLabCount());
        existingCourse.setLecHours(courseDetails.getLecHours());
        existingCourse.setStartDate(courseDetails.getStartDate());
        existingCourse.setEndDate(courseDetails.getEndDate());
        courseRepository.save(existingCourse);
        logger.info("Course updated successfully: {}", courseCode);
    }

    public void deleteCourse(String courseCode) {
        logger.info("Attempting to delete course with code: {}", courseCode);
        CourseDetails course = getCourseByCode(courseCode);
        courseRepository.delete(course);
        logger.warn("Course deleted successfully: {}", courseCode);
    }

    public String generateNextCourseCode() {
        logger.info("Generating next course code.");
        List<CourseDetails> allCourses = courseRepository.findAll();
        if (allCourses.isEmpty()) {
            logger.info("No courses found, starting with course code C001.");
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
        String nextCourseCode = String.format("C%03d", number);
        logger.info("Next course code generated: {}", nextCourseCode);
        return nextCourseCode;
    }

    public boolean existsByCourseCode(String courseCode) {
        logger.info("Checking if course exists with code: {}", courseCode);
        return courseRepository.existsByCourseCode(courseCode);
    }
}
