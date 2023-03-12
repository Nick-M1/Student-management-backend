package com.tutorial.demo.student;

import com.tutorial.demo.exception.ApiRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/*
    Unit testing service
    Note: Tests only the Service class & mocks the repository/DB
*/

@ActiveProfiles({ "inmemory" })
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;     // classes being mocked

    @InjectMocks
    private StudentService studentService;          // the class being tested

    private List<Student> studentList;

    @BeforeEach
    void setUp() {
        studentList = new ArrayList<>();
        studentList.add(new Student(1L, "John Doe", "jdoe@example.com", "https://example.com/jdoe.jpg", LocalDate.of(2000, 1, 1), new HashSet<>(Arrays.asList("Math", "Science"))));
        studentList.add(new Student(2L, "Jane Smith", "jsmith@example.com", "https://example.com/jsmith.jpg", LocalDate.of(2001, 2, 2), new HashSet<>(Arrays.asList("English", "History"))));
    }

    // GETTERS
    @Test
    @DisplayName("Test get all students")
    void testGetAllStudents() {
        when(studentRepository.findAll()).thenReturn(studentList);

        List<Student> actualResult = studentService.getAllStudents();

        assertEquals(studentList, actualResult);
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void testGetAllStudentsByRequest() {
        List<String> subjects = Arrays.asList("Math", "Science");
        String searchBy = "John";
        String orderBy = "name";
        String isAsc = "true";
        Sort.Direction sortDirection = Sort.Direction.ASC;

        when(studentRepository.findAllStudentsCustomQuery(searchBy, subjects, Sort.by(sortDirection, orderBy))).thenReturn(studentList);

        List<Student> actualResult = studentService.getAllStudentsByRequest(searchBy, orderBy, isAsc, subjects);

        assertEquals(studentList, actualResult);
        verify(studentRepository, times(1)).findAllStudentsCustomQuery(searchBy, subjects, Sort.by(sortDirection, orderBy));
    }

    @Test
    void testGetStudentByIdWithValidId() {
        // Arrange
        long id = 1L;
        Student student = new Student();
        student.setId(id);
        Mockito.when(studentRepository.findById(id)).thenReturn(Optional.of(student));
        // Act
        Student result = studentService.getStudentById(id);
        // Assert
        assertEquals(student, result);
    }

    @Test
    @DisplayName("Test getStudent with invalid id")
    public void testGetStudentWithInvalidId() throws Exception {
        // Arrange
        Long id = 999L;

        when(studentRepository.findById(id)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ApiRequestException.class, () -> {
            studentService.getStudentById(id);
        });

        verify(studentRepository, times(1)).findById(id);
    }


    @Test
    void testGetAllStudentsByRequestWithSubjects() {
        // Create a list of students with specific subjects
        List<Student> students = Arrays.asList(
                new Student("John Doe", "john.doe@example.com", "image", LocalDate.now(), new HashSet<>(Arrays.asList("Math", "Physics"))),
                new Student("Jane Smith", "jane.smith@example.com", "image", LocalDate.now(), new HashSet<>(Arrays.asList("Math", "Chemistry"))),
                new Student("Bob Johnson", "bob.johnson@example.com", "image", LocalDate.now(), new HashSet<>(Arrays.asList("Physics", "Chemistry")))
        );
        // Stub the student repository to return the list of students
        when(studentRepository.findAllStudentsCustomQuery("", Arrays.asList("Math", "Physics"), Sort.by(Sort.Direction.ASC, "id"))).thenReturn(students);

        // Call the method to get students by subjects
        List<Student> result = studentService.getAllStudentsByRequest("", "id", "true", Arrays.asList("Math", "Physics"));

        // Verify the result contains only students with the specified subjects
        assertEquals(3, result.size());
    }

    @Test
    void testGetStudentsBySubjectsWhenNoMatch() {
        // Arrange
        String searchBy = null;
        String orderBy = null;
        String isAsc = null;
        List<String> subjects = List.of("French", "German");

        when(studentRepository.findAllStudentsCustomQuery(anyString(), anyList(), any(Sort.class)))
                .thenReturn(List.of());

        // Act
        List<Student> result = studentService.getAllStudentsByRequest(searchBy, orderBy, isAsc, subjects);

        // Assert
        assertEquals(Collections.emptyList(), result);
    }


    @Test
    void testGetAllSubjects() {
        Set<String> expectedSubjects = new HashSet<>(Arrays.asList("Math", "Science", "English", "History"));

        when(studentRepository.findAllSubjects()).thenReturn(expectedSubjects);

        Set<String> actualSubjects = studentService.getAllSubjects();

        assertEquals(expectedSubjects, actualSubjects);
        verify(studentRepository, times(1)).findAllSubjects();
    }

    // POST
    @Test
    void testAddNewStudent() {
        Student newStudent = new Student(3L,"New Student", "newstudent@example.com", "", LocalDate.now(), Set.of("mathematics"));

        when(studentRepository.findStudentByEmail(newStudent.getEmail())).thenReturn(Optional.empty());
        when(studentRepository.save(newStudent)).thenReturn(newStudent);

        Long actualId = studentService.addNewStudent(newStudent);

        assertNotNull(actualId);
        verify(studentRepository, times(1)).findStudentByEmail(newStudent.getEmail());
        verify(studentRepository, times(1)).save(newStudent);
    }

    @Test
    void testAddNewStudentWithEmailTaken() {
        Student newStudent = new Student(4L,"New Student", "newstudent@example.com", "", LocalDate.now(), Set.of("mathematics"));
        Optional<Student> existingStudent = Optional.of(new Student(3L, "Existing Student", "existingstudent@example.com", null, null, null));

        when(studentRepository.findStudentByEmail(newStudent.getEmail())).thenReturn(existingStudent);

        assertThrows(ApiRequestException.class, () -> studentService.addNewStudent(newStudent));
        verify(studentRepository, times(1)).findStudentByEmail(newStudent.getEmail());
    }

    // DELETE
    @Test
    void testDeleteStudentWithValidId() {
        // Arrange
        Long studentId = 1L;
        Student student = new Student(
                "John Doe",
                "john.doe@example.com",
                "image-url",
                LocalDate.of(2000, 1, 1),
                new HashSet<>(Arrays.asList("Maths", "Physics"))
        );

        Mockito.when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        // Act
        Long deletedStudentId = studentService.deleteStudent(studentId);

        // Assert
        assertEquals(studentId, deletedStudentId);
        Mockito.verify(studentRepository, Mockito.times(1)).deleteById(studentId);
    }

    @Test
    void testDeleteStudentWithInvalidId() {
        // Arrange
        Long studentId = 1L;

        Mockito.when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ApiRequestException.class, () -> studentService.deleteStudent(studentId));
        Mockito.verify(studentRepository, Mockito.times(1)).findById(studentId);
        Mockito.verify(studentRepository, Mockito.never()).deleteById(studentId);
    }

    // PUT
    @Test
    void testUpdateStudentWithValidId() {
        // Create a student to update
        Student studentToUpdate = new Student(
                "John Doe",
                "john.doe@example.com",
                "http://example.com/john_doe.jpg",
                LocalDate.of(1995, 2, 1),
                new HashSet<>(Arrays.asList("Math", "Science"))
        );
        Long studentId = studentService.addNewStudent(studentToUpdate);

        // Mock repository method
        Mockito.when(studentRepository.findById(studentId))
                .thenReturn(Optional.of(studentToUpdate));

        // Update the student
        String updatedName = "Jane Doe";
        String updatedEmail = "jane.doe@example.com";
        String updatedImage = "http://example.com/jane_doe.jpg";
        String updatedDob = "1996-03-01";
        Set<String> updatedSubjects = new HashSet<>(Arrays.asList("Math", "Science", "English"));

        Long updatedStudentId = studentService.updateStudent(studentId, updatedName, updatedEmail, updatedImage, updatedDob, updatedSubjects);

        // Verify that the student was updated correctly
        Student updatedStudent = studentService.getStudentById(updatedStudentId);
        Assertions.assertEquals(updatedName, updatedStudent.getName());
        Assertions.assertEquals(updatedEmail, updatedStudent.getEmail());
        Assertions.assertEquals(updatedImage, updatedStudent.getImage());
        Assertions.assertEquals(LocalDate.parse(updatedDob), updatedStudent.getDob());
        Assertions.assertEquals(updatedSubjects, updatedStudent.getSubjects());
    }

    @Test
    void testUpdateStudentWithInvalidId() {
        // Arrange
        Long studentId = 1L;
        String name = "Jane Doe";
        String email = "jane.doe@example.com";
        String image = "new-image-url";
        LocalDate dob = LocalDate.of(2000, 1, 1);
        Set<String> subjects = new HashSet<>(Arrays.asList("Maths", "Chemistry"));

        Mockito.when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ApiRequestException.class, () -> studentService.updateStudent(studentId, name, email, image, dob.toString(), subjects));
        Mockito.verify(studentRepository, Mockito.times(1)).findById(studentId);
    }

}


