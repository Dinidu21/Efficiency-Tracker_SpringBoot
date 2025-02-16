package com.dinidu.myapp.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String courseName;
    private LocalDate startDate;
    private LocalDate endDate;
    private double lectureHours;
    private int labCount;

    private long daysSpent;

    @Transient
    private double efficiency;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public double getLectureHours() { return lectureHours; }
    public void setLectureHours(double lectureHours) { this.lectureHours = lectureHours; }

    public int getLabCount() { return labCount; }
    public void setLabCount(int labCount) { this.labCount = labCount; }

    public long getDaysSpent() {
        return ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }

    public double getEfficiency() {
        return (lectureHours + labCount) / (double) getDaysSpent();
    }
}
