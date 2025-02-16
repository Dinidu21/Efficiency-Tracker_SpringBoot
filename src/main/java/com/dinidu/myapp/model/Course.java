package com.dinidu.myapp.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String courseName;

    private LocalDate startDate;
    private LocalDate endDate;
    private double lectureHours;
    private int labCount;
    private long daysSpent;

    @Transient
    private double efficiency;

    @Transient
    private String totalSpentTime;

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

    public String getTotalSpentTime() {
        return totalSpentTime;
    }

    public void setTotalSpentTime(double totalSpentTime) {
        this.totalSpentTime = formatTime(totalSpentTime);
    }

    public long getDaysSpent() { return ChronoUnit.DAYS.between(startDate, endDate) + 1; }

    public String getEfficiency() {
        if (daysSpent <= 0) {
            return "N/A";
        }

        double daysSpentInHours = daysSpent * 24;
        double efficiencyValue = (lectureHours + (labCount * 0.5)) / daysSpentInHours;

        return formatTime(efficiencyValue * 24);
    }


    public static String formatTime(double hours) {
        int fullHours = (int) hours;
        int minutes = (int) ((hours - fullHours) * 60);
        return fullHours + " hrs " + minutes + " mins";
    }

    public void setDaysSpent(long daysSpent) {
        this.daysSpent = daysSpent;
    }

    public void setEfficiency(double efficiency) {
        this.efficiency = efficiency;
    }
}
