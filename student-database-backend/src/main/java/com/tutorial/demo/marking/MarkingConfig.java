package com.tutorial.demo.marking;

import com.tutorial.demo.student.Student;
import com.tutorial.demo.student.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Set;

//@Configuration
//public class MarkingConfig {
//
//    @Bean
//    CommandLineRunner commandLineRunner(MarkingRepository markingRepository) {
//        return args -> {
//            Marking m1 = new Marking(
//                "english",
//                5.2f
//            )
//
//            markingRepository.saveAll(List.of(m1));         // Saves list of students to DB
//        };
//    }
//}
