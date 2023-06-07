package com.tutorial.demo.student;

/*
    API LAYER:
    Stores resources for API for Student
*/

import com.tutorial.demo.yeargroup.YearGroupEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public Page<Student> getAllStudentsByRequest(
            @RequestParam(value = "searchBy", required = false, defaultValue = "") String searchBy,
            @RequestParam(value = "orderBy",  required = false, defaultValue = "id") String orderBy,
            @RequestParam(value = "isAsc",  required = false, defaultValue = "true") String isAsc,
            @RequestParam(value = "page",  required = false, defaultValue = "0") int pageNumber,
            @RequestParam(value = "subjects", required = false, defaultValue = "") List<String> subjects) {
        return studentService.getAllStudentsByRequest(searchBy, orderBy, isAsc, pageNumber, subjects);
    }

    @GetMapping(path = "count")                      // Get request
    public long getCountStudentsByRequest(
            @RequestParam(value = "searchBy", required = false, defaultValue = "") String searchBy,
            @RequestParam(value = "subjects", required = false, defaultValue = "") List<String> subjects) {
        return studentService.getCountStudentsByRequest(searchBy, subjects);
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

    @PostMapping                        // Post request
    @ResponseStatus(HttpStatus.CREATED)
//    @PreAuthorize("hasAuthority('student:write')")
    public Long registerNewStudent(@RequestBody Student student) {      // @RequestBody = Gets this input from user
        return studentService.addNewStudent(student);
    }

    @DeleteMapping(path="{studentId}")  // Delete request (given studentId)
    @ResponseStatus(HttpStatus.OK)
    public Long deleteStudent(@PathVariable("studentId") Long studentId) {
        return studentService.deleteStudent(studentId);
    }

    @PutMapping(path="{studentId}")     // Put/update request (update id, name & email)
    @ResponseStatus(HttpStatus.OK)
    public Long updateStudent(
            @PathVariable("studentId") Long studentId,
            @RequestParam(value = "name", required = false, defaultValue = "") String name,
            @RequestParam(value = "email", required = false, defaultValue = "") String email,
            @RequestParam(value = "image", required = false, defaultValue = "") String image,
            @RequestParam(value = "dob", required = false, defaultValue = "") String dob,
            @RequestParam(value = "yeargroup", required = false, defaultValue = "") YearGroupEnum yeargroup,
            @RequestParam(required = false) Set<String> subjects) {
        return studentService.updateStudent(studentId, name, email, image, dob, yeargroup, subjects);
    }

}
