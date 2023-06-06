package com.tutorial.demo.student;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tutorial.demo.yeargroup.YearGroupEnum;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.Set;

@Entity(name="Student")             // Hibernate DB
@Table(name = "student", uniqueConstraints = {@UniqueConstraint(name = "student_email_unique", columnNames = "email")})   // Database
public class Student {

    @Id
    @SequenceGenerator(name = "student_sequence", sequenceName = "student_sequence", allocationSize = 1)    //DB
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_sequence")  //DB

    @Column(name = "id", updatable = false)                                             private Long id;        // student's id can't be modified
    @Column(name = "name", nullable = false, columnDefinition = "TEXT")                 private String name;
    @Column(name = "email", nullable = false, columnDefinition = "TEXT", unique = true) private String email;
    @Column(name = "image", nullable = false, columnDefinition = "TEXT")                private String image;
    @Column(name = "date_of_birth", nullable = false, columnDefinition = "TEXT")        private LocalDate dob;
    @Enumerated(EnumType.STRING) @JsonProperty("yeargroup")                             private YearGroupEnum yeargroup;
    @ElementCollection @CollectionTable(name = "subjects")                              private Set<String> subjects;
    @Transient                                                                          private Integer age;    // This value is calculated, not inputted into constructor as argument


    public Student() {
    }

    public Student(Long id, String name, String email, String image, LocalDate dob, YearGroupEnum yeargroup, Set<String> subjects ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.image = image;
        this.dob = dob;
        this.yeargroup = yeargroup;
        this.subjects = subjects;
    }

    public Student(String name, String email, String image, LocalDate dob, YearGroupEnum yeargroup, Set<String> subjects) {
        this.name = name;
        this.email = email;
        this.image = image;
        this.dob = dob;
        this.yeargroup = yeargroup;
        this.subjects = subjects;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public YearGroupEnum getYeargroup() {
        return yeargroup;
    }

    public void setYeargroup(YearGroupEnum yeargroup) {
        this.yeargroup = yeargroup;
    }

    public Set<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<String> subjects) {
        this.subjects = subjects;
    }

    public Integer getAge() {                   // Calcs age from dob
        return Period.between(dob, LocalDate.now()).getYears();
    }


    @Override
    public String toString() {
        return String.format("Student( id=%d, name=%s, email=%s, dob=%s, age=%d, yeargroup=%s, subjects=%s)", id, name, email, dob, age, yeargroup.toString(), subjects.toString());
    }
}
