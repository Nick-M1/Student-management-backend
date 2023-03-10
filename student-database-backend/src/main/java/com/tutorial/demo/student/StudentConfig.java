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
            Student s1 = new Student(
                    "Mariam Micheal",
                    "mariam.jamal@gmail.com",
                    "https://images.unsplash.com/photo-1494790108377-be9c29b29330?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=facearea&facepad=4&w=256&h=256&q=60",
                    LocalDate.of(2000, Month.JANUARY, 5),
                    Set.of("biology", "chemistry")
            );
            Student s2 = new Student(
                    "Alex James",
                    "alexjames@gmail.com",
                    "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=facearea&facepad=4&w=256&h=256&q=60",
                    LocalDate.of(2004, Month.JANUARY, 16),
                    Set.of("mathematics", "chemistry")
            );
            Student s3 = new Student(
                    "Jack Smith",
                    "jacksmith@gmail.com",
                    "https://images.unsplash.com/photo-1520813792240-56fc4a3765a7?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=facearea&facepad=4&w=256&h=256&q=60",
                    LocalDate.of(2002, Month.SEPTEMBER, 10),
                    Set.of("physics", "mathematics")
            );

            studentRepository.saveAll(List.of(s1, s2, s3));         // Saves list of students to DB
        };
    }

}
