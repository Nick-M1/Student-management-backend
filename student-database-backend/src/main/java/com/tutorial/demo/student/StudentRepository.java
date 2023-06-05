package com.tutorial.demo.student;

/*
    DATA ACCESS LAYER:
    Accesses info from database (postreSQL)
*/

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface StudentRepository extends JpaRepository<Student, Long> {
//    List<Student> findAllByNameIsContainingIgnoreCaseOrEmailIsContainingIgnoreCase(String name, String email, Sort sort);

    @Query("""
        select distinct
            s
        from
            Student s
        JOIN
            s.subjects subject
        where
            subject IN :subjectsList
            AND (
                upper(s.name) like CONCAT('%',UPPER(:searchterm),'%') escape '/'
                OR upper(s.email) like CONCAT('%',UPPER(:searchterm),'%') escape '/'
            )
    """)
    Page<Student> findAllStudentsCustomQuery(String searchterm, List<String> subjectsList, Pageable pageable);          // JPA adds Sort automatically

    @Query("""
        select
            COUNT(DISTINCT s)
        from
            Student s
        JOIN
            s.subjects subject
        where
            subject IN :subjectsList
            AND (
                upper(s.name) like CONCAT('%',UPPER(:searchterm),'%') escape '/'
                OR upper(s.email) like CONCAT('%',UPPER(:searchterm),'%') escape '/'
            )
    """)
    long countByCustomQuery(String searchterm, List<String> subjectsList);

    @Query("SELECT subject FROM Student s join s.subjects subject")
    Set<String> findAllSubjects();

//    @Query("SELECT s FROM Student s JOIN s.subjects subject WHERE ?1 IN subject ")
//    List<Student> findAllStudentsBySubjects(String subject);

    @Query("SELECT s FROM Student s WHERE s.email = ?1")
    Optional<Student> findStudentByEmail(String email);
}
