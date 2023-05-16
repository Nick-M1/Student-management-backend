package com.tutorial.demo.student;

/*
    API LAYER:
    Stores resources for API for Student
*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path="api/v1/student")
public class StudentController {

    private final StudentService studentService;                // Want all variables to be private & final/consts if possible (final needs constructor)


    @Autowired                                                  // Passes 'new StudentService()' into the constructor for StudentController
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // GET ALL STUDENTS (with ordering & filtering
    @GetMapping(path = "all")                        // Get request
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping                        // Get request
    public List<Student> getAllStudentsByRequest(
            @RequestParam(value = "searchBy", required = false, defaultValue = "") String searchBy,
            @RequestParam(value = "orderBy",  required = false, defaultValue = "") String orderBy,
            @RequestParam(value = "isAsc",  required = false, defaultValue = "true") String isAsc,
            @RequestParam(required = false) List<String> subjects) {
        return studentService.getAllStudentsByRequest(searchBy, orderBy, isAsc, subjects);
    }

    @GetMapping(path = "{studentId}")                        // Get request
    public Student getStudentById(@PathVariable("studentId") Long studentId) {
        return studentService.getStudentById(studentId);
    }

    // GET ALL SUBJECTS
    @GetMapping(path = "subjects")
    public Set<String> getAllSubjects() {
        return studentService.getAllSubjects();
    }

    @PostMapping                       // Post request
    public Long registerNewStudent(@RequestBody Student student) {      // @RequestBody = Gets this input from user
        return studentService.addNewStudent(student);
    }

    @DeleteMapping(path="{studentId}")  // Delete request (given studentId)
    public Long deleteStudent(@PathVariable("studentId") Long studentId) {
        return studentService.deleteStudent(studentId);
    }

    @PutMapping(path="{studentId}")     // Put/update request (update id, name & email)
    public Long updateStudent(
            @PathVariable("studentId") Long studentId,
            @RequestParam(value = "name", required = false, defaultValue = "") String name,
            @RequestParam(value = "email", required = false, defaultValue = "") String email,
            @RequestParam(value = "image", required = false, defaultValue = "") String image,
            @RequestParam(value = "dob", required = false, defaultValue = "") String dob,
            @RequestParam(required = false) Set<String> subjects) {
        return studentService.updateStudent(studentId, name, email, image, dob, subjects);
    }

}
