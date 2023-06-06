package com.tutorial.demo.marking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface MarkingRepository extends JpaRepository<Marking, Long> {
    List<Marking> findByStudentId(Long student_id);

    @Query("""
        select distinct
            m
        from
            Marking m
        where
            m.student.id = :studentId
            AND upper(m.title) like CONCAT('%',UPPER(:searchterm),'%') escape '/'
    """)
    Page<Marking> findByStudentIdCustomQuery(Long studentId, String searchterm, Pageable pageable);


    @Query("""
        select
            m.course.code,
            AVG(m.score) AS courseAverage
        from
            Marking m
        where
            m.student.id = :studentId
        group by m.course.code
    """)
    List<?> findMarksStatisticsByStudentId(Long studentId);
}
