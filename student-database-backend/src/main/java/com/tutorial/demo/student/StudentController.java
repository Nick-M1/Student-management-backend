package com.tutorial.demo.student;

/*
    API LAYER:
    Stores resources for API for Student
*/

import com.tutorial.demo.exception.ApiRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path="api/v1/student")
@CrossOrigin("http://localhost:5173/")          //todo: Put in env variables
public class StudentController {

    private final StudentService studentService;                // Want all variables to be private & final/consts if possible (final needs constructor)


    @Autowired                                                  // Passes 'new StudentService()' into the constructor for StudentController
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

//    @GetMapping                        // Get request
//    public List<Student> getAllStudents() {
//        return studentService.getAllStudents();
//    }

    @GetMapping                        // Get request
    public List<Student> getAllStudents(@RequestParam(required = false) String searchBy, @RequestParam(required = false) String orderBy, @RequestParam(required = false) String isAsc) {
        return studentService.getAllStudents(searchBy, orderBy, isAsc);
    }

    @PostMapping                       // Post request
    public Long registerNewStudent(@RequestBody Student student) {      // @RequestBody = Gets this input from user
        return studentService.addNewStudent(student);
    }

    @DeleteMapping(path="{studentId}")  // Delete request (given studentId)
    public Long deleteStudent(@PathVariable("studentId") Long studentId) {
        return studentService.deleteStudent(studentId);
    } //... Improve: Enforce that id != null ...

    @PutMapping(path="{studentId}")     // Put/update request (update id, name & email)
    public Long updateStudent(@PathVariable("studentId") Long studentId, @RequestParam(required = false) String name, @RequestParam(required = false) String email, @RequestParam(required = false) String dob) {
        return studentService.updateStudent(studentId, name, email, dob);
    }

}
