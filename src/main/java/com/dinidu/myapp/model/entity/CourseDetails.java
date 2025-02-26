package com.dinidu.myapp.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CourseDetails {
    @Id
    private String courseCode;
    private String courseName;
    private float labCount;
    private int lecHours;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    @Transient // Not persisted to database
    private long totalDaysSpent;

    @Transient // Not persisted to database
    private float totalWorkingHours;

    // Calculate TotalDaysSpent
    public long getTotalDaysSpent() {
        if (startDate != null && endDate != null) {
            // Convert Date to LocalDate for better handling
            LocalDate startLocalDate = startDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            LocalDate endLocalDate = endDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

            // Calculate the total days, inclusive of both start and end dates
            return ChronoUnit.DAYS.between(startLocalDate, endLocalDate) + 1; // Add 1 to include the end date
        }
        return 0;
    }

    // Calculate TotalWorkingHours
    public float getTotalWorkingHours() {
        // Calculate lab time in hours (since each lab is 30 minutes, labCount * 30 will give total minutes)
        float labTimeInHours = (labCount * 30) / 60.0f; // Convert to hours by dividing by 60

        // Add lecture hours and lab hours
        return lecHours + labTimeInHours;
    }

}