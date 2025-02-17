package com.dinidu.myapp.model;

import java.time.LocalDate;

public class ProgressSummary {
    private LocalDate deadline;
    private long daysRemaining;
    private int totalCourses;
    private int completedCourses;
    private int totalLabs;
    private int completedLabs;
    private double courseCompletionPercentage;
    private double labCompletionPercentage;
    private String timeDebt;

    public ProgressSummary(LocalDate deadline, long daysRemaining, int totalCourses, int completedCourses, int totalLabs, int completedLabs, double courseCompletionPercentage, double labCompletionPercentage, String timeDebt) {
        this.deadline = deadline;
        this.daysRemaining = daysRemaining;
        this.totalCourses = totalCourses;
        this.completedCourses = completedCourses;
        this.totalLabs = totalLabs;
        this.completedLabs = completedLabs;
        this.courseCompletionPercentage = courseCompletionPercentage;
        this.labCompletionPercentage = labCompletionPercentage;
        this.timeDebt = timeDebt;
    }

    public ProgressSummary() {

    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public long getDaysRemaining() {
        return daysRemaining;
    }

    public void setDaysRemaining(long daysRemaining) {
        this.daysRemaining = daysRemaining;
    }

    public int getTotalCourses() {
        return totalCourses;
    }

    public void setTotalCourses(int totalCourses) {
        this.totalCourses = totalCourses;
    }

    public int getCompletedCourses() {
        return completedCourses;
    }

    public void setCompletedCourses(int completedCourses) {
        this.completedCourses = completedCourses;
    }

    public int getTotalLabs() {
        return totalLabs;
    }

    public void setTotalLabs(int totalLabs) {
        this.totalLabs = totalLabs;
    }

    public int getCompletedLabs() {
        return completedLabs;
    }

    public void setCompletedLabs(int completedLabs) {
        this.completedLabs = completedLabs;
    }

    public double getCourseCompletionPercentage() {
        return courseCompletionPercentage;
    }

    public void setCourseCompletionPercentage(double courseCompletionPercentage) {
        this.courseCompletionPercentage = courseCompletionPercentage;
    }

    public double getLabCompletionPercentage() {
        return labCompletionPercentage;
    }

    public void setLabCompletionPercentage(double labCompletionPercentage) {
        this.labCompletionPercentage = labCompletionPercentage;
    }

    public String getTimeDebt() {
        return timeDebt;
    }

    public void setTimeDebt(String timeDebt) {
        this.timeDebt = timeDebt;
    }
}