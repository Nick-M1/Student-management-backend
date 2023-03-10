package com.tutorial.demo.student;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Set;

@Configuration
public class StudentConfig {


    // Students that are already in DB at start-time
    @Bean
    CommandLineRunner commandLineRunner(StudentRepository studentRepository) {
        return args -> {
            Student s1 = new Student(1L, "Mariam Micheal", "mariam.jamal@gmail.com", LocalDate.of(2000, Month.JANUARY, 5), Set.of("biology", "chemistry"));
            Student s2 = new Student("Alex James", "alexjames@gmail.com", LocalDate.of(2004, Month.JANUARY, 16), Set.of("mathematics", "chemistry"));
            Student s3 = new Student("Jack Smith", "jacksmith@gmail.com", LocalDate.of(2002, Month.SEPTEMBER, 10), Set.of("physics", "mathematics"));

            studentRepository.saveAll(List.of(s1, s2, s3));         // Saves list of students to DB
        };
    }

}
