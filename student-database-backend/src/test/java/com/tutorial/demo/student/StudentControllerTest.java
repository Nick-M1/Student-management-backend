package com.tutorial.demo.student;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    public void testGetAllStudents() throws Exception {
        Student student1 = new Student(1L, "Alice", "alice@example.com", "https://example.com/alice.jpg", LocalDate.now(), new HashSet<>(Arrays.asList("math", "physics")));
        Student student2 = new Student(2L, "Bob", "bob@example.com", "https://example.com/bob.jpg", LocalDate.now(), new HashSet<>(Arrays.asList("history", "english")));
        List<Student> students = new LinkedList<>(Arrays.asList(student1, student2));

        when(studentRepository.findAll()).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/students/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value("Alice"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].name").value("Bob"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testGetStudentById() throws Exception {
        Student student = new Student(1L, "Alice", "alice@example.com", "https://example.com/alice.jpg", LocalDate.now(), new HashSet<>(Arrays.asList("math", "physics")));

        when(studentRepository.findById(1L)).thenReturn(java.util.Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Alice"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("alice@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.subjects").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.subjects.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.subjects[0]").value("math"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.subjects[1]").value("physics"))
                .andDo(MockMvcResultHandlers.print());
    }

//    @Test
//    public void testAddNewStudent() throws Exception {
//        Student student = new Student(null, "
}