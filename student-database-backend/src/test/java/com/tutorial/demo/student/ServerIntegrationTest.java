package com.tutorial.demo.student;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
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
    Integration testing (whole process) + [uses a real server via WebTestClient]
    Note: Tests the whole api endpoint & is the only test that requires the whole server to be running

    Note: as using a config file to prepopulate the DB, this will also be included in tests (only for this test class)
*/

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Transactional                              // Resets DB after each test
@ActiveProfiles({ "real" })                 // Want to use the postgresql DB
public class ServerIntegrationTest {

    @Autowired
    private WebTestClient webClient;

    @Test
    public void testGetAllStudents() {
        webClient.get().uri("/api/v1/student/all")
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    public void testGetStudentById() {
        Long studentId = 1L;

        webClient.get().uri("/api/v1/student/{studentId}", studentId)
            .exchange()
            .expectStatus().isOk();
    }

    @Test
    void testGetAllStudentsByRequest() {
        String searchBy = "Alex";
        String orderBy = "name";
        String isAsc = "true";
        String subject1 = "mathematics";


        // when
        webClient.get().uri(uriBuilder ->
            uriBuilder.path("/api/v1/student")
                .queryParam("searchBy", searchBy)
                .queryParam("orderBy", orderBy)
                .queryParam("isAsc", isAsc)
                .queryParam("subjects", subject1)
                .build()

        ).exchange().expectStatus().isOk();
    }

    @Test
    void testGetAllSubjects() {
        webClient.get().uri("/api/v1/student/subjects")
            .exchange()
            .expectStatus().isOk();
    }


    @Test
    public void testAddNewStudentValid() {
        Student student = new Student("John Doe", "john@example.com", "image.jpg", LocalDate.now(), Set.of("math", "science"));
        String studentJson = studentToJson(student);            // convert the student object to JSON

        // perform the POST request
        webClient.post().uri("/api/v1/student")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(studentJson)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void testAddNewStudentInvalid() throws Exception {
        String studentJson = "{ \"id\": 5, }";

        // perform the POST request
        webClient.post().uri("/api/v1/student")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(studentJson)
                .exchange()
                .expectStatus().is4xxClientError();
    }


    @Test
    public void deleteStudentValid() {
        Long studentId = 2L;

        // perform the DELETE request
        webClient.delete().uri("/api/v1/student/{studentId}", studentId)
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(studentId.toString());
    }

    @Test
    public void deleteStudentInvalid() {
        Long studentId = 100L;

        // perform the DELETE request
        webClient.delete().uri("/api/v1/student/{studentId}", studentId)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody().jsonPath("$.message").isEqualTo(String.format("student with id: %s not found", studentId));
    }

    @Test
    public void testUpdateStudentValid() {
        // new values to update the student object
        Long studentId = 1L;
        String updatedName = "John Doe Jr.";
        String updatedEmail = "johndoejr@example.com";
        String updatedImage = "https://example.com/johndoejr.png";
        String updatedDob = "1997-01-01";

        String updatesSubject1 = "biology";
        String updatesSubject2 = "english";


        // when
        webClient.put().uri(uriBuilder ->
                uriBuilder.path("/api/v1/student/" + studentId)
                        .queryParam("name", updatedName)
                        .queryParam("email", updatedEmail)
                        .queryParam("image", updatedImage)
                        .queryParam("dob", updatedDob)
                        .queryParam("subjects", updatesSubject1, updatesSubject2)
                        .build()

        ).exchange()
                .expectStatus().isOk()
                .expectBody().json(studentId.toString());
    }


    @Test
    public void testUpdateStudentInvalidEmailTaken() {
        // new values to update the student object
        Long studentId = 1L;
        String updatedName = "John Doe Jr.";
        String updatedEmail = "jacksmith@gmail.com";
        String updatedImage = "https://example.com/johndoejr.png";
        String updatedDob = "1997-01-01";

        String updatesSubject1 = "biology";
        String updatesSubject2 = "english";

        // when
        webClient.put().uri(uriBuilder ->
                        uriBuilder.path("/api/v1/student/" + studentId)
                                .queryParam("name", updatedName)
                                .queryParam("email", updatedEmail)
                                .queryParam("image", updatedImage)
                                .queryParam("dob", updatedDob)
                                .queryParam("subjects", updatesSubject1, updatesSubject2)
                                .build()

                ).exchange()
                .expectStatus().is4xxClientError()
                .expectBody().jsonPath("$.message").isEqualTo("email taken");
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