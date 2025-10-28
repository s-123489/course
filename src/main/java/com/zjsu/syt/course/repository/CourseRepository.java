package com.zjsu.syt.course.repository;

import com.zjsu.syt.course.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {

    Optional<Course> findByCode(String code);

    List<Course> findByInstructorId(String instructorId);

    @Query("SELECT c FROM Course c WHERE c.enrolled < c.capacity")
    List<Course> findAvailableCourses();

    @Query("SELECT c FROM Course c WHERE c.title LIKE %:keyword%")
    List<Course> findByTitleContaining(@Param("keyword") String keyword);

    boolean existsByCode(String code);
}