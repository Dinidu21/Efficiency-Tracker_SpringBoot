package com.dinidu.myapp.repo;

import com.dinidu.myapp.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    // Custom query to check for duplicate course names after normalizing spaces
    @Query("SELECT c FROM Course c WHERE TRIM(BOTH ' ' FROM c.courseName) = :name")
    Optional<Course> findByNormalizedCourseName(@Param("name") String name);
}
