package com.tutorial.demo.course;

import javax.persistence.*;

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

    public Course(Long id, String code, String title) {
        this.id = id;
        this.code = code;
        this.title = title;
    }

    public Course(String code, String title) {
        this.code = code;
        this.title = title;
    }

    public Course() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
