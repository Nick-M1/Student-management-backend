package com.tutorial.demo.marking;

import com.tutorial.demo.student.Student;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MarkingRepository extends JpaRepository<Marking, Long> {
    List<Marking> findByStudentId(Long student_id);


////    List<Student> findAllByNameIsContainingIgnoreCaseOrEmailIsContainingIgnoreCase(String name, String email, Sort sort);
//
//    @Query("""
//        select distinct
//            s
//        from
//            Student s
//        JOIN
//            s.subjects subject
//        where
//            subject IN :subjectsList
//            AND (
//                upper(s.name) like CONCAT('%',UPPER(:searchterm),'%') escape '/'
//                OR upper(s.email) like CONCAT('%',UPPER(:searchterm),'%') escape '/'
//            )
//    """)
//    List<Student> findAllStudentsCustomQuery(String searchterm, List<String> subjectsList, Sort sort);          // JPA adds Sort automatically
//
//    @Query("SELECT subject FROM Student s join s.subjects subject")
//    Set<String> findAllSubjects();
//
////    @Query("SELECT s FROM Student s JOIN s.subjects subject WHERE ?1 IN subject ")
////    List<Student> findAllStudentsBySubjects(String subject);
//
//    @Query("SELECT s FROM Student s WHERE s.email = ?1")
//    Optional<Student> findStudentByEmail(String email);
}
