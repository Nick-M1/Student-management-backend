package com.tutorial.demo.student;

/*
    DATA ACCESS LAYER:
    Accesses info from database (postreSQL)
*/

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findAllByNameIsContainingIgnoreCaseOrEmailIsContainingIgnoreCase(String name, String email, Sort sort);

    @Query("SELECT s FROM Student s WHERE s.email = ?1")
    Optional<Student> findStudentByEmail(String email);



    /// FOR DEMOS:
    //    List<Student> findAllByOrderByIdAsc();
    //    List<Student> findAllByOrderByNameAsc();
    //    List<Student> findAllByOrderByEmailAsc();
    //    List<Student> findAllByOrderByDobAsc();

    //    List<Student> findAllByOrderByDobDesc();

    //    List<Student> findAllByNameIsContainingIgnoreCaseOrEmailIsContainingIgnoreCase(String name, String email);
    //    List<Student> findAllByNameIsContainingIgnoreCase(String name, Sort sort);
}
