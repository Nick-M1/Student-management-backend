package com.tutorial.demo.student;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @AfterEach
    void tearDown() {
        studentRepository.deleteAll();
    }

    // TESTING - Find by custom query
    @Test
    void findAllStudentsCustomQueryBySingleSubjectAscendingOrder() {
        String searchterm = "";
        List<String> subjectsList = List.of("mathematics");
        Sort sort = Sort.by(Sort.Direction.ASC, "id");

        Student student1 = new Student(1L, "James", "james@gmail.com", "", LocalDate.now(), Set.of("biology", "chemistry"));
        Student student2 = new Student(2L, "Jake", "jake@gmail.com", "", LocalDate.now(), Set.of("mathematics", "chemistry"));
        Student student3 = new Student(3L, "Maria", "maria@gmail.com", "", LocalDate.now(), Set.of("physics"));
        studentRepository.saveAll(List.of(student1, student2, student3));

        List<Student> foundStudents = studentRepository.findAllStudentsCustomQuery(searchterm, subjectsList, sort);

        assertEquals(1, foundStudents.size());
        assertEquals(student2.getId(), foundStudents.get(0).getId());
        assertEquals(student2.getName(), foundStudents.get(0).getName());
        assertEquals(student2.getEmail(), foundStudents.get(0).getEmail());
        assertEquals(student2.getSubjects(), foundStudents.get(0).getSubjects());
    }

    @Test
    void findAllStudentsCustomQueryByMultipleSubjectAscendingOrder() {
        String searchterm = "";
        List<String> subjectsList = List.of("mathematics", "biology");
        Sort sort = Sort.by(Sort.Direction.ASC, "id");

        Student student1 = new Student(1L, "James", "james@gmail.com", "", LocalDate.now(), Set.of("biology", "chemistry"));
        Student student2 = new Student(2L, "Jake", "jake@gmail.com", "", LocalDate.now(), Set.of("mathematics", "chemistry"));
        Student student3 = new Student(3L, "Maria", "maria@gmail.com", "", LocalDate.now(), Set.of("physics"));
        studentRepository.saveAll(List.of(student1, student2, student3));

        List<Student> foundStudents = studentRepository.findAllStudentsCustomQuery(searchterm, subjectsList, sort);
        System.out.println(foundStudents);
        assertEquals(2, foundStudents.size());
        assertEquals(student1.getId(), foundStudents.get(0).getId());
        assertEquals(student2.getId(), foundStudents.get(1).getId());
        assertEquals(student1.getSubjects(), foundStudents.get(0).getSubjects());
        assertEquals(student2.getSubjects(), foundStudents.get(1).getSubjects());
    }

    @Test
    void findAllStudentsCustomQueryBySingleSubjectDescendingOrder() {
        String searchterm = "";
        List<String> subjectsList = List.of("biology");
        Sort sort = Sort.by(Sort.Direction.DESC, "id");

        Student student1 = new Student(1L, "James", "james@gmail.com", "", LocalDate.now(), Set.of("biology"));
        Student student2 = new Student(2L, "Jake", "jake@gmail.com", "", LocalDate.now(), Set.of("biology"));
        Student student3 = new Student(3L, "Maria", "maria@gmail.com", "", LocalDate.now(), Set.of("biology"));
        studentRepository.saveAll(List.of(student1, student2, student3));

        List<Student> foundStudents = studentRepository.findAllStudentsCustomQuery(searchterm, subjectsList, sort);
        assertEquals(3, foundStudents.size());
        assertEquals(student1.getId(), foundStudents.get(2).getId());
        assertEquals(student2.getId(), foundStudents.get(1).getId());
        assertEquals(student3.getId(), foundStudents.get(0).getId());
    }

    @Test
    void findAllStudentsCustomQueryBySingleSubjectAscendingOrderByName() {
        String searchterm = "";
        List<String> subjectsList = List.of("biology");
        Sort sort = Sort.by(Sort.Direction.ASC, "name");

        Student student1 = new Student(1L, "James", "james@gmail.com", "", LocalDate.now(), Set.of("biology"));
        Student student2 = new Student(2L, "Jake", "jake@gmail.com", "", LocalDate.now(), Set.of("biology"));
        Student student3 = new Student(3L, "Maria", "maria@gmail.com", "", LocalDate.now(), Set.of("biology"));
        studentRepository.saveAll(List.of(student1, student2, student3));

        List<Student> foundStudents = studentRepository.findAllStudentsCustomQuery(searchterm, subjectsList, sort);
        assertEquals(3, foundStudents.size());
        assertEquals(student1.getId(), foundStudents.get(1).getId());
        assertEquals(student2.getId(), foundStudents.get(0).getId());
        assertEquals(student3.getId(), foundStudents.get(2).getId());
    }

    @Test
    void findAllStudentsCustomQueryBySearchterm() {
        String searchterm = "maria";
        List<String> subjectsList = List.of("biology");
        Sort sort = Sort.by(Sort.Direction.ASC, "id");

        Student student1 = new Student(1L, "James", "james@gmail.com", "", LocalDate.now(), Set.of("biology"));
        Student student2 = new Student(2L, "Jake", "jake@gmail.com", "", LocalDate.now(), Set.of("biology"));
        Student student3 = new Student(3L, "Maria", "maria@gmail.com", "", LocalDate.now(), Set.of("biology"));
        studentRepository.saveAll(List.of(student1, student2, student3));

        List<Student> foundStudents = studentRepository.findAllStudentsCustomQuery(searchterm, subjectsList, sort);
        assertEquals(1, foundStudents.size());
        assertEquals(student3.getId(), foundStudents.get(0).getId());
    }

    @Test
    void findNoStudentsCustomQueryBySearchterm() {
        String searchterm = "zzzzzzz";
        List<String> subjectsList = List.of("biology");
        Sort sort = Sort.by(Sort.Direction.ASC, "id");

        Student student1 = new Student(1L, "James", "james@gmail.com", "", LocalDate.now(), Set.of("biology"));
        Student student2 = new Student(2L, "Jake", "jake@gmail.com", "", LocalDate.now(), Set.of("biology"));
        Student student3 = new Student(3L, "Maria", "maria@gmail.com", "", LocalDate.now(), Set.of("biology"));
        studentRepository.saveAll(List.of(student1, student2, student3));

        List<Student> foundStudents = studentRepository.findAllStudentsCustomQuery(searchterm, subjectsList, sort);
        assertEquals(0, foundStudents.size());
    }



    // TESTING - Find by email
    @Test
    void itShouldfindStudentByEmail() {
        String email = "james@gmail.com";
        Student student = new Student(1L, "James", email, "", LocalDate.now(), Set.of());
        studentRepository.save(student);

        Optional<Student> foundStudent = studentRepository.findStudentByEmail(email);

        assertThat(foundStudent).isNotEmpty();
        assertEquals(student.getId(), foundStudent.get().getId());
        assertEquals(student.getName(), foundStudent.get().getName());
        assertEquals(student.getEmail(), foundStudent.get().getEmail());
        assertEquals(student.getSubjects(), foundStudent.get().getSubjects());
    }

    @Test
    void itShouldNotfindStudentByEmail() {
        String email = "example@gmail.com";
        Student student = new Student(1L, "James", "james@gmail.com", "", LocalDate.now(), Set.of());
        studentRepository.save(student);

        Optional<Student> foundStudent = studentRepository.findStudentByEmail(email);

        assertThat(foundStudent).isEmpty();
    }



    // TESTING - Get all subjects
    @Test
    void itShouldfindAllSubjects() {
        Student student1 = new Student(1L, "James", "james@gmail.com", "", LocalDate.now(), Set.of("biology", "chemistry"));
        Student student2 = new Student(2L, "Jake", "jake@gmail.com", "", LocalDate.now(), Set.of("mathematics", "chemistry"));
        studentRepository.saveAll(List.of(student1, student2));

        Set<String> foundSubjects = studentRepository.findAllSubjects();

        assertThat(foundSubjects).isEqualTo(Set.of("biology", "chemistry", "mathematics"));
    }

    // TESTING - Adding students
    @Test
    void shouldAddStudent() {
        Student existingStudent = new Student(1L, "James", "james@gmail.com", "", LocalDate.now(), Set.of());
        studentRepository.save(existingStudent);

        Student newStudent = new Student(2L, "Smith", "smith@gmail.com", "", LocalDate.now(), Set.of());
        studentRepository.save(newStudent);

        List<Student> foundStudents = studentRepository.findAll();
        assertEquals(2, foundStudents.size());
        assertTrue(studentRepository.existsById(existingStudent.getId()));
        assertTrue(studentRepository.existsById(newStudent.getId()));
    }

    @Test
    void shouldNotAddStudentBecauseEmailAlreadyExists() {
        Student existingStudent = new Student(1L, "James", "james@gmail.com", "", LocalDate.now(), Set.of());
        studentRepository.save(existingStudent);

        Student newStudent = new Student(2L, "Smith", "james@gmail.com", "", LocalDate.now(), Set.of());

        assertThrows(org.springframework.dao.DataIntegrityViolationException.class, () -> studentRepository.save(newStudent));
    }

    @Test
    void shouldNotAddStudentBecauseIdAlreadyExists() {
        Student existingStudent = new Student(1L, "James", "james@gmail.com", "", LocalDate.now(), Set.of());
        studentRepository.save(existingStudent);
        Student newStudent = new Student(1L, "Smith", "smith@gmail.com", "", LocalDate.now(), Set.of());
        studentRepository.save(newStudent);

        List<Student> foundStudents = studentRepository.findAll();

        assertEquals(1, foundStudents.size());
        assertEquals(1, foundStudents.get(0).getId());
    }
}