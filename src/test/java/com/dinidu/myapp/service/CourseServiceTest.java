package com.dinidu.myapp.service;

import com.dinidu.myapp.exception.ResourceNotFoundException;
import com.dinidu.myapp.model.entity.CourseDetails;
import com.dinidu.myapp.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    private CourseDetails sampleCourse;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @BeforeEach
    void setUp() throws ParseException {
        sampleCourse = new CourseDetails();
        sampleCourse.setCourseCode("C001");
        sampleCourse.setCourseName("Spring Boot Fundamentals");
        sampleCourse.setLecHours(10);
        sampleCourse.setLabCount(5);
        sampleCourse.setStartDate(dateFormat.parse("2025-01-01"));
        sampleCourse.setEndDate(dateFormat.parse("2025-03-01"));
    }

    @Test
    void testGetAllCourses() {
        CourseDetails course2 = new CourseDetails();
        try {
            course2.setCourseCode("C002");
            course2.setStartDate(dateFormat.parse("2025-02-01"));
        } catch (ParseException e) {
            fail("Failed to parse date in test setup");
        }

        when(courseRepository.findAll()).thenReturn(Arrays.asList(sampleCourse, course2));

        List<CourseDetails> courses = courseService.getAllCourses();

        assertEquals(2, courses.size());
        assertEquals("C001", courses.get(0).getCourseCode());
        assertEquals("C002", courses.get(1).getCourseCode());
    }

    @Test
    void testGetCourseByCode_WhenCourseExists() {
        when(courseRepository.findById("C001")).thenReturn(Optional.of(sampleCourse));

        CourseDetails foundCourse = courseService.getCourseByCode("C001");

        assertNotNull(foundCourse);
        assertEquals("C001", foundCourse.getCourseCode());
    }

    @Test
    void testGetCourseByCode_WhenCourseNotFound() {
        when(courseRepository.findById("C999")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> courseService.getCourseByCode("C999"));
    }

    @Test
    void testSaveCourse() {
        courseService.saveCourse(sampleCourse);
        verify(courseRepository, times(1)).save(sampleCourse);
    }

    @Test
    void testUpdateCourse_WhenCourseExists() {
        CourseDetails updatedCourse = new CourseDetails();
        try {
            updatedCourse.setCourseName("Advanced Spring Boot");
            updatedCourse.setLecHours(20);
            updatedCourse.setLabCount(10);
            updatedCourse.setStartDate(dateFormat.parse("2025-04-01"));
            updatedCourse.setEndDate(dateFormat.parse("2025-06-01"));
        } catch (ParseException e) {
            fail("Failed to parse date in test setup");
        }

        when(courseRepository.findById("C001")).thenReturn(Optional.of(sampleCourse));

        courseService.updateCourse("C001", updatedCourse);

        assertEquals("Advanced Spring Boot", sampleCourse.getCourseName());
        assertEquals(20, sampleCourse.getLecHours());
        assertEquals(10, sampleCourse.getLabCount());

        verify(courseRepository, times(1)).save(sampleCourse);
    }

    @Test
    void testDeleteCourse_WhenCourseExists() {
        when(courseRepository.findById("C001")).thenReturn(Optional.of(sampleCourse));

        courseService.deleteCourse("C001");

        verify(courseRepository, times(1)).delete(sampleCourse);
    }

    @Test
    void testDeleteCourse_WhenCourseNotFound() {
        when(courseRepository.findById("C999")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> courseService.deleteCourse("C999"));
    }

    @Test
    void testGenerateNextCourseCode_WhenNoCoursesExist() {
        when(courseRepository.findAll()).thenReturn(List.of());

        String nextCode = courseService.generateNextCourseCode();

        assertEquals("C001", nextCode);
    }

    @Test
    void testGenerateNextCourseCode_WhenCoursesExist() {
        CourseDetails course2 = new CourseDetails();
        course2.setCourseCode("C002");

        when(courseRepository.findAll()).thenReturn(Arrays.asList(sampleCourse, course2));

        String nextCode = courseService.generateNextCourseCode();

        assertEquals("C003", nextCode);
    }

    @Test
    void testExistsByCourseCode() {
        when(courseRepository.existsByCourseCode("C001")).thenReturn(true);

        boolean exists = courseService.existsByCourseCode("C001");

        assertTrue(exists);
        verify(courseRepository, times(1)).existsByCourseCode("C001");
    }
}
