package com.tutorial.demo.student;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Configuration
public class StudentConfig {


    // Students that are already in DB at start-time
    @Bean
    CommandLineRunner commandLineRunner(StudentRepository studentRepository) {
        return args -> {
            Student s1 = new Student(1L, "Mariam", "mariam.jamal@gmail.com", LocalDate.of(2000, Month.JANUARY, 5));
            Student s2 = new Student("Alex", "alex@gmail.com", LocalDate.of(2004, Month.JANUARY, 5));

            studentRepository.saveAll(List.of(s1, s2));         // Saves list of students to DB
        };
    }

}
