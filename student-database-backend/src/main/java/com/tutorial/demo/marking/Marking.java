package com.tutorial.demo.marking;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tutorial.demo.course.Course;
import com.tutorial.demo.student.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// Lombok:
@Data
@NoArgsConstructor
@AllArgsConstructor

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


    public Marking(Course course, Student student, String title, Float score) {
        this.student = student;
        this.course = course;
        this.title = title;
        this.score = score;
    }
}
