package com.tutorial.demo.student;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tutorial.demo.user.Role;
import com.tutorial.demo.user.User;
import com.tutorial.demo.yeargroup.YearGroupEnum;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.Collection;
import java.util.List;
import java.util.Set;

// Lombok:
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)

@Entity(name="Student")             // Hibernate DB
@Table(name = "student", uniqueConstraints = {@UniqueConstraint(name = "student_email_unique", columnNames = "email")})
public class Student extends User {

    @Id
    @SequenceGenerator(name = "student_sequence", sequenceName = "student_sequence", allocationSize = 1)    //DB
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_sequence")  //DB

    @Column(name = "id", updatable = false)                                             private Long id;        // student's id can't be modified

    @Column(name = "email", nullable = false, columnDefinition = "TEXT", unique = true) private String email;
    @JsonIgnore @Column(name = "password", nullable = false, columnDefinition = "TEXT")             private String password;
    @JsonIgnore @Column(name = "enabled", nullable = false, columnDefinition = "BOOLEAN")           private boolean enabled;
    @JsonIgnore @Column(name = "verified", nullable = false, columnDefinition = "BOOLEAN")          private boolean verified;
    @Enumerated(EnumType.STRING)                                                        private Role role;

    @Column(name = "name", nullable = false, columnDefinition = "TEXT")                 private String name;
    @Column(name = "image", nullable = false, columnDefinition = "TEXT")                private String image;
    @Column(name = "date_of_birth", nullable = false, columnDefinition = "TEXT")        private LocalDate dob;
    @Enumerated(EnumType.STRING) @JsonProperty("yeargroup")                             private YearGroupEnum yeargroup;
    @ElementCollection @CollectionTable(name = "subjects")                              private Set<String> subjects;
    @Transient                                                                          private Integer age;    // This value is calculated, not inputted into constructor as argument

    public Student(String email, String password, boolean enabled, boolean verified, Role role, String name, String image, LocalDate dob, YearGroupEnum yeargroup, Set<String> subjects) {
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.verified = verified;
        this.role = role;
        this.name = name;
        this.image = image;
        this.dob = dob;
        this.yeargroup = yeargroup;
        this.subjects = subjects;
    }

    public Integer getAge() {
        return Period.between(dob, LocalDate.now()).getYears();
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getGrantedAuthorities();
//        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
