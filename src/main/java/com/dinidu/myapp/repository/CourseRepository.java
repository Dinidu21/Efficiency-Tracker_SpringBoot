package com.dinidu.myapp.repository;

import com.dinidu.myapp.model.entity.CourseDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<CourseDetails, String> {
}
