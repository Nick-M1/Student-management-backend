package com.tutorial.demo.marking;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tutorial.demo.course.Course;
import com.tutorial.demo.student.Student;

import javax.persistence.*;

@Entity(name="Marking")             // Hibernate DB
@Table(name = "marking")   // Database
public class Marking {

    @Id
    @SequenceGenerator(name = "marking_sequence", sequenceName = "marking_sequence", allocationSize = 1)    //DB
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "marking_sequence")  //DB

    @Column(name = "id", updatable = false)                                                         private Long id;
    @JsonIgnoreProperties("markings") @ManyToOne @JoinColumn(name="student_id", nullable=false)     private Student student;
    @JsonIgnoreProperties("markings") @ManyToOne @JoinColumn(name="course_id", nullable=false)      private Course course;
    @Column(name = "title", nullable = false, columnDefinition = "TEXT")                            private String title;
    @Column(name = "score", nullable = false, columnDefinition = "FLOAT")                           private Float score;


    public Marking() {}

    public Marking(Long id,
                   Student student,
                   Course course,
                   String title,
                   Float score) {
        this.id = id;
        this.student = student;
        this.course = course;
        this.title = title;
        this.score = score;
    }

    public Marking(Course course,
                   Student student,
                   String title,
                   Float score) {
        this.student = student;
        this.course = course;
        this.title = title;
        this.score = score;
    }

    public Long getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public Float getScore() {
        return score;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
