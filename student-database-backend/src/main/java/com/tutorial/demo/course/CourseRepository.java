package com.tutorial.demo.course;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByCode(String code);

    @Query("SELECT DISTINCT c.department FROM Course c")
    Set<String> findAllDepartments();

    @Query("""
        select distinct
            c
        from
            Course c
        where
            c.department IN :departments
            AND (
                upper(c.code) like CONCAT('%',UPPER(:searchterm),'%') escape '/'
                OR upper(c.title) like CONCAT('%',UPPER(:searchterm),'%') escape '/'
            )
    """)
    Page<Course> findAllCoursesCustomQuery(String searchterm, List<String> departments, Pageable pageable);
}
