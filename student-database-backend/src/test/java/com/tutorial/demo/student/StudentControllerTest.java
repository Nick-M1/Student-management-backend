package com.tutorial.demo.student;

import com.tutorial.demo.exception.ApiRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*
    Integration testing (narrow)
    Note: Tests only the controller, and mocks the service (the repo/db is never reached)
*/

@ActiveProfiles({ "inmemory" })
@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Test
    public void testUnauthorised() throws Exception {
        mockMvc.perform(get("/api/v1/student/all")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetAllStudents() throws Exception {
        Student student1 = new Student("Alice", "alice@example.com", "https://example.com/alice.jpg", LocalDate.now(), Set.of("math", "physics"));
        Student student2 = new Student("Bob", "bob@example.com", "https://example.com/bob.jpg", LocalDate.now(), Set.of("history", "english"));
        List<Student> students = List.of(student1, student2);

        when(studentService.getAllStudents()).thenReturn(students);

        mockMvc.perform(get("/api/v1/student/all")
            .with(httpBasic("username", "password"))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))

            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))

            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$.[0].id").value(student1.getId()))
            .andExpect(jsonPath("$.[1].id").value(student2.getId()))
            .andExpect(jsonPath("$.[0].name").value(student1.getName()))
            .andExpect(jsonPath("$.[1].name").value(student2.getName()))
            .andDo(print());
    }

    @Test
    public void testGetStudentById() throws Exception {
        Long studentId = 1L;
        Student student = new Student(studentId, "Alice", "alice@example.com", "https://example.com/alice.jpg", LocalDate.now(), new HashSet<>(Arrays.asList("math", "physics")));

        when(studentService.getStudentById(studentId)).thenReturn(student);

        mockMvc.perform(
            get("/api/v1/student/{studentId}", studentId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))

            .andExpect(jsonPath("$.id").value( student.getId()))
            .andExpect(jsonPath("$.name").value( student.getName()))
            .andExpect(jsonPath("$.email").value( student.getEmail()))
            .andExpect(jsonPath("$.subjects").isArray())
            .andExpect(jsonPath("$.subjects.length()").value(2))
            .andExpect(jsonPath("$.subjects[0]").value("physics"))
            .andExpect(jsonPath("$.subjects[1]").value("math"))
            .andDo(print());
    }

    @Test
    void testGetAllStudentsByRequest() throws Exception {
        // given
        Student student1 = new Student(1L, "John Doe", "johndoe@example.com", "image.jpg", LocalDate.now(), Set.of("Math", "English"));
        Student student2 = new Student(2L, "Jane Doe", "janedoe@example.com", "image.jpg", LocalDate.now(), Set.of("Science", "History"));
        List<Student> students = List.of(student1, student2);

        String searchBy = "Doe";
        String orderBy = "name";
        String isAsc = "true";
        String subject1 = "Math";
        String subject2 = "English";

        when(studentService.getAllStudentsByRequest(searchBy, orderBy, isAsc, List.of(subject1, subject2))).thenReturn(students);

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/student")
                .param("searchBy", searchBy)
                .param("orderBy", orderBy)
                .param("isAsc", isAsc)
                .param("subjects", subject1, subject2)
        );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value(student1.getName()))
                .andExpect(jsonPath("$[1].name").value(student2.getName()))
                .andExpect(jsonPath("$[0].id").value(student1.getId()))
                .andExpect(jsonPath("$[1].id").value(student2.getId()))
                .andDo(print());
    }

    @Test
    void testGetAllSubjects() throws Exception {
        Set<String> subjects = Set.of("math", "chemistry", "english");

        when(studentService.getAllSubjects()).thenReturn(subjects);

        mockMvc.perform(get("/api/v1/student/subjects"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3))
                .andDo(print());
    }


    @Test
    public void testAddNewStudentValid() throws Exception {
        Student student = new Student(1L, "John Doe", "john@example.com", "image.jpg", LocalDate.now(), Set.of("math", "science"));

        String studentJson = studentToJson(student);            // convert the student object to JSON

        // mock the service to return the ID of the newly created student
        when(studentService.addNewStudent(any(Student.class))).thenReturn(student.getId());

        // perform the POST request
        mockMvc.perform(
                post("/api/v1/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentJson)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(student.getId()))
                .andDo(print());
    }

    @Test
    public void testAddNewStudentInvalid() throws Exception {
        String studentJson = "{ \"id\": 5, }";

        // perform the POST request
        mockMvc.perform(
                        post("/api/v1/student")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(studentJson)
                )
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }


    @Test
    public void deleteStudentValid() throws Exception {
        Long studentId = 1L;
        Student student = new Student(studentId, "John Doe", "john@example.com", "image.jpg", LocalDate.now(), Set.of("math", "science"));

        when(studentService.getStudentById(studentId)).thenReturn(student);
        when(studentService.deleteStudent(studentId)).thenReturn(studentId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/student/{studentId}", studentId))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string(studentId.toString()))
            .andDo(print());
    }

    @Test
    public void deleteStudentInvalid() throws Exception {
        Long studentId = 1L;
        String errorMsg = "student doesn't exist";

        when(studentService.deleteStudent(studentId)).thenThrow(new ApiRequestException(errorMsg));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/student/{studentId}", studentId))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(errorMsg))
                .andDo(print());
    }

    @Test
    public void testUpdateStudent() throws Exception {
        // new values to update the student object
        Long studentId = 1L;
        String updatedName = "John Doe Jr.";
        String updatedEmail = "johndoejr@example.com";
        String updatedImage = "https://example.com/johndoejr.png";
        String updatedDob = "1997-01-01";

        String updatesSubject1 = "biology";
        String updatesSubject2 = "english";
        Set<String> updatedSubjects = Set.of(updatesSubject1, updatesSubject2);

        // mock student service to return updated student object
        when(studentService.updateStudent(eq(studentId), eq(updatedName), eq(updatedEmail), eq(updatedImage), eq(updatedDob), eq(updatedSubjects))).thenReturn(studentId);

        // send PUT request to update the student
        ResultActions resultActions = mockMvc.perform(
                put("/api/v1/student/{id}", studentId)
                        .param("name", updatedName)
                        .param("email", updatedEmail)
                        .param("image", updatedImage)
                        .param("subjects", updatesSubject1, updatesSubject2)
        );

        // then
        resultActions
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$").value(0))
                .andDo(print());

        // verify that student service was called with updated values
//        verify(studentService, times(1))
//                .updateStudent(eq(studentId), eq(updatedName), eq(updatedEmail), eq(updatedImage), eq(updatedDob), eq(updatedSubjects));
    }

    public static String studentToJson(Student student) {
        return String.format("""
            {
                "id": %s,
                "name": "%s",
                "email": "%s",
                "image": "%s",
                "dob": "%s",
                "subjects": [%s]
            }
        """,
            student.getId(),
            student.getName(),
            student.getEmail(),
            student.getImage(),
            student.getDob(),
            setToString(student.getSubjects())
        );
    }

    public static String setToString(Set<? extends CharSequence> set) {
        return set.isEmpty() ? "" : "\"" + String.join("\", \"", set) + "\"";
    }
}