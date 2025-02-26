package com.dinidu.myapp.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseDetails {
    @Id
    private String courseCode;
    private String courseName;
    private float labCount;
    private int lecHours;
    private Date startDate;
    private Date endDate;
}
