package com.tutorial.demo.student;

import com.tutorial.demo.user.Role;
import com.tutorial.demo.yeargroup.YearGroupEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Set;

@Configuration
public class StudentConfig {
    private static final Logger logger = LoggerFactory.getLogger(StudentConfig.class);
    private final PasswordEncoder passwordEncoder;

    public StudentConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    // Students that are already in DB at start-time
    @Bean
    CommandLineRunner commandLineRunner(StudentRepository studentRepository) {
        return args -> {

            Student s1 = new Student(
                    "mariam.jamal@gmail.com",
                    passwordEncoder.encode("password"),
                    true,
                    true,
                    Role.STUDENT,
                    "Mariam Micheal",
                    "https://images.unsplash.com/photo-1494790108377-be9c29b29330?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=facearea&facepad=4&w=256&h=256&q=60",
                    LocalDate.of(2000, Month.JANUARY, 5),
                    YearGroupEnum.YEAR_1,
                    Set.of("biology", "chemistry")
            );

            Student s2 = new Student(
                    "alexjames@gmail.com",
                    passwordEncoder.encode("password"),
                    true,
                    true,
                    Role.ADMIN,
                    "Alex James",
                    "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=facearea&facepad=4&w=256&h=256&q=60",
                    LocalDate.of(2004, Month.JANUARY, 16),
                    YearGroupEnum.YEAR_3,
                    Set.of("mathematics", "chemistry")
            );

            Student s3 = new Student(
                    "jacksmith@gmail.com",
                    passwordEncoder.encode("password"),
                    true,
                    true,
                    Role.ADMIN,
                    "Jack Smith",
                    "https://images.unsplash.com/photo-1520813792240-56fc4a3765a7?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=facearea&facepad=4&w=256&h=256&q=60",
                    LocalDate.of(2002, Month.SEPTEMBER, 10),
                    YearGroupEnum.YEAR_4,
                    Set.of("physics", "mathematics")
            );


            // Saves list of students to DB
            studentRepository.saveAll(List.of(s1, s2, s3))
                    .forEach(student -> logger.info("Add student to database: " + student.toString()));
        };
    }

}
