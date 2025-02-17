package com.dinidu.myapp.service;

import com.dinidu.myapp.model.Course;
import com.dinidu.myapp.model.ProgressSummary;
import com.dinidu.myapp.repo.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;
    private Clock clock;

    public List<Course> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        LocalDate today = LocalDate.now();

        for (Course course : courses) {
            // Calculate total spent time
            double totalHours = course.getLectureHours() + (course.getLabCount() * 0.5);
            course.setTotalSpentTime(totalHours);

            // Calculate overdue status
            boolean isOverdue = today.isAfter(course.getEndDate()) && !course.isCompleted();
            course.setOverdue(isOverdue);

            // Calculate completion percentage
            double lectureProgress = course.getTargetLectureHours() > 0 ?
                    (course.getLectureHours() / course.getTargetLectureHours()) * 100 : 0;
            double labProgress = course.getTargetLabCount() > 0 ?
                    ((double) course.getLabCount() / course.getTargetLabCount()) * 100 : 0;
            course.setCompletionPercentage((lectureProgress + labProgress) / 2);
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
        if (!courseRepository.existsById(id)) {
            System.out.println("Error: Course with ID " + id + " not found.");
            return;
        }

        courseRepository.deleteById(id);
        System.out.println("Course with ID " + id + " deleted successfully.");
    }

    public ProgressSummary getProgressSummary() {
        ProgressSummary summary = new ProgressSummary();
        List<Course> courses = getAllCourses();
        LocalDate today = LocalDate.now();

        // Initialize totals
        double totalRequiredHours = 0;
        double completedHours = 0;
        int totalLabs = 0;
        int completedLabs = 0;
        int completedCourses = 0;

        for (Course course : courses) {
            totalRequiredHours += course.getTargetLectureHours() + (course.getTargetLabCount() * 0.5);
            completedHours += course.getLectureHours() + (course.getLabCount() * 0.5);
            totalLabs += course.getTargetLabCount();
            completedLabs += course.getLabCount();

            if(course.isCompleted()) completedCourses++;
        }

        // Set summary values
        summary.setTotalCourses(courses.size());
        summary.setCompletedCourses(completedCourses);
        summary.setTotalLabs(totalLabs);
        summary.setCompletedLabs(completedLabs);
        summary.setDeadline(LocalDate.of(2024, 4, 27));
        summary.setDaysRemaining(ChronoUnit.DAYS.between(today, summary.getDeadline()));

        // Calculate percentages
        summary.setCourseCompletionPercentage(courses.isEmpty() ? 0 :
                (completedCourses * 100.0) / courses.size());
        summary.setLabCompletionPercentage(totalLabs == 0 ? 0 :
                (completedLabs * 100.0) / totalLabs);

        // Time debt calculation
        long daysPassed = ChronoUnit.DAYS.between(LocalDate.of(2024, 1, 27), today);
        double idealHoursPerDay = totalRequiredHours / 90; // 90-day total
        double actualHoursPerDay = daysPassed > 0 ? completedHours / daysPassed : 0;
        double requiredDaily = (totalRequiredHours - completedHours) / Math.max(summary.getDaysRemaining(), 1);

        if(requiredDaily > actualHoursPerDay) {
            double deficit = requiredDaily - actualHoursPerDay;
            summary.setTimeDebt(String.format("⚠️ Need %s/day extra",
                    Course.formatTime(deficit)));
        } else {
            summary.setTimeDebt("On track! Keep going 👍");
        }

        return summary;
    }
}

