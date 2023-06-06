package com.tutorial.demo.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// Lombok:
@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity(name="Course")             // Hibernate DB
@Table(name = "course", uniqueConstraints = {@UniqueConstraint(name = "course_code_unique", columnNames = "code")})
public class Course {

    @Id
    @SequenceGenerator(name = "student_sequence", sequenceName = "student_sequence", allocationSize = 1)    //DB
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_sequence")  //DB

    @Column(name = "id", updatable = false)
    private Long id;
    @Column(name = "code", nullable = false, columnDefinition = "TEXT", unique = true)
    private String code;
    @Column(name = "title", nullable = false, columnDefinition = "TEXT")
    private String title;
    @Column(name = "department", nullable = false, columnDefinition = "TEXT")
    private String department;


    public Course(String code, String title, String department) {
        this.code = code;
        this.title = title;
        this.department = department;
    }
}
