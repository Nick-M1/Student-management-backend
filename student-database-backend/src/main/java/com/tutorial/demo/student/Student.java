package com.tutorial.demo.student;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;

@Entity(name="Student")             // Hibernate DB
@Table(name = "student", uniqueConstraints = {@UniqueConstraint(name = "student_email_unique", columnNames = "email")})   // Database
public class Student {

    @Id
    @SequenceGenerator(name = "student_sequence", sequenceName = "student_sequence", allocationSize = 1)    //DB
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_sequence")  //DB

    @Column(name = "id", updatable = false)                                             private Long id;        // student's id can't be modified
    @Column(name = "name", nullable = false, columnDefinition = "TEXT")                 private String name;
    @Column(name = "email", nullable = false, columnDefinition = "TEXT", unique = true) private String email;
    @Column(name = "date_of_birth", nullable = false, columnDefinition = "TEXT")        private LocalDate dob;
    @Transient                                                                          private Integer age;    // This value is calculated, not inputted into constructor as argument


    public Student() {
    }

    public Student(Long id, String name, String email, LocalDate dob) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.dob = dob;
    }

    public Student(String name, String email, LocalDate dob) {
        this.name = name;
        this.email = email;
        this.dob = dob;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public Integer getAge() {                   // Calcs age from dob
        return Period.between(dob, LocalDate.now()).getYears();
    }


    @Override
    public String toString() {
        return String.format("Student( id=%d, name=%s, email=%s, dob=%s, age=%d)", id, name, email, dob, age);
    }
}
