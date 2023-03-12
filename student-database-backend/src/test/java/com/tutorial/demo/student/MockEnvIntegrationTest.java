package com.tutorial.demo.student;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*
    Integration testing (whole process) + [Mocking server via MockMvc]
    Note: Tests the whole api endpoint & is the only test that requires the whole server to be running

    Note: as using a config file to prepopulate the DB, this will also be included in tests (only for this test class)
*/


@ExtendWith(MockitoExtension.class)
@SpringBootTest
@Transactional                  // Rolls back changes to DB for each test
@ActiveProfiles({ "real" })     // Want to use the postgresql DB
@AutoConfigureMockMvc
public class MockEnvIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    // If StudentConfig Mocked, then the real StudentConfig won't run and the DB will be empty at startup.
    // If StudentConfig isn't mocked (commented out), it will prepopulate the DB
//    @MockBean
//    StudentConfig studentConfig;


    @Test
    public void testGetAllStudents() throws Exception {
        mockMvc.perform(get("/api/v1/student/all")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))

            .andExpect(jsonPath("$.length()").value(3))
            .andExpect(jsonPath("$.[0].id").value(1))
            .andExpect(jsonPath("$.[1].id").value(2))
            .andExpect(jsonPath("$.[2].id").value(3))
            .andDo(print());
    }

    @Test
    public void testGetStudentById() throws Exception {
        Long studentId = 1L;

        mockMvc.perform(
            get("/api/v1/student/{studentId}", studentId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))

            .andExpect(jsonPath("$.id").value( studentId))
            .andExpect(jsonPath("$.name").value( "Mariam Micheal"))
            .andDo(print());
    }

    @Test
    void testGetAllStudentsByRequest() throws Exception {
        String searchBy = "Alex";
        String orderBy = "name";
        String isAsc = "true";
        String subject1 = "mathematics";


        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/student")
                .param("searchBy", searchBy)
                .param("orderBy", orderBy)
                .param("isAsc", isAsc)
                .param("subjects", subject1)
        );

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].name").value("Alex James"))
                .andDo(print());
    }

    @Test
    void testGetAllSubjects() throws Exception {
        mockMvc.perform(get("/api/v1/student/subjects"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(4))
                .andDo(print());
    }


    @Test
    public void testAddNewStudentValid() throws Exception {
        Student student = new Student("John Doe", "john@example.com", "image.jpg", LocalDate.now(), Set.of("math", "science"));
        String studentJson = studentToJson(student);            // convert the student object to JSON

        // perform the POST request
        mockMvc.perform(
                post("/api/v1/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentJson)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNumber())
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

        mockMvc.perform(delete("/api/v1/student/{studentId}", studentId))
            .andExpect(status().isOk())
            .andExpect(content().string(studentId.toString()))
            .andDo(print());
    }

    @Test
    public void deleteStudentInvalid() throws Exception {
        Long studentId = 100L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/student/{studentId}", studentId))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(jsonPath("$.message").value(String.format("student with id: %s not found", studentId)))
                .andDo(print());
    }

    @Test
    public void testUpdateStudentValid() throws Exception {
        // new values to update the student object
        Long studentId = 1L;
        String updatedName = "John Doe Jr.";
        String updatedEmail = "johndoejr@example.com";
        String updatedImage = "https://example.com/johndoejr.png";
        String updatedDob = "1997-01-01";

        String updatesSubject1 = "biology";
        String updatesSubject2 = "english";


        // send PUT request to update the student
        ResultActions resultActions = mockMvc.perform(
                put("/api/v1/student/{id}", studentId)
                        .param("name", updatedName)
                        .param("email", updatedEmail)
                        .param("image", updatedImage)
                        .param("dob", updatedDob)
                        .param("subjects", updatesSubject1, updatesSubject2)
        );

        // then
        resultActions
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$").value(studentId))
                .andDo(print());
    }


    @Test
    public void testUpdateStudentInvalidEmailTaken() throws Exception {
        // new values to update the student object
        Long studentId = 1L;
        String updatedName = "John Doe Jr.";
        String updatedEmail = "jacksmith@gmail.com";
        String updatedImage = "https://example.com/johndoejr.png";
        String updatedDob = "1997-01-01";

        String updatesSubject1 = "biology";
        String updatesSubject2 = "english";


        // send PUT request to update the student
        ResultActions resultActions = mockMvc.perform(
                put("/api/v1/student/{id}", studentId)
                        .param("name", updatedName)
                        .param("email", updatedEmail)
                        .param("image", updatedImage)
                        .param("dob", updatedDob)
                        .param("subjects", updatesSubject1, updatesSubject2)
        );

        // then
        resultActions
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("email taken"))
                .andDo(print());
    }


    public static String studentToJson(Student student) {
        return String.format("""
            {
                "name": "%s",
                "email": "%s",
                "image": "%s",
                "dob": "%s",
                "subjects": [%s]
            }
        """,
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