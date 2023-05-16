package com.tutorial.demo.marking;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tutorial.demo.student.Student;

import javax.persistence.*;

@Entity(name="Marking")             // Hibernate DB
@Table(name = "marking")   // Database
public class Marking {

    @Id
    @SequenceGenerator(name = "marking_sequence", sequenceName = "marking_sequence", allocationSize = 1)    //DB
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "marking_sequence")  //DB

    @Column(name = "id", updatable = false)                                             private Long id;        // student's id can't be modified
    @JsonIgnoreProperties("markings") @ManyToOne @JoinColumn(name="student_id", nullable=false)   private Student student;
    @Column(name = "subject", nullable = false, columnDefinition = "TEXT")              private String subject;
    @Column(name = "score", nullable = false, columnDefinition = "TEXT")                private Float score;


    //    @Column(name = "date_of_birth", nullable = false, columnDefinition = "TEXT")        private LocalDate dob;
//    @ElementCollection @CollectionTable(name = "subjects")                              private Set<String> subjects;
//    @Transient                                                                          private Integer age;

    public Marking() {}

    public Marking(Long id,
                   Student student,
                   String subject, Float score) {
        this.id = id;
        this.student = student;
        this.subject = subject;
        this.score = score;
    }

    public Marking(String subject,
                   Student student,
                   Float score) {
        this.student = student;
        this.subject = subject;
        this.score = score;
    }

    public Long getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public String getSubject() {
        return subject;
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

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setScore(Float score) {
        this.score = score;
    }
}
