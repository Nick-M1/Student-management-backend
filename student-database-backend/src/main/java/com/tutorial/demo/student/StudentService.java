package com.tutorial.demo.student;

/*
    SERVICE LAYER:
    For business logic
*/

import com.tutorial.demo.exception.ApiRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component      // Allows this class to be 'injected' into constructor for another class -> For @Autowired
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    // GETTERS
    public List<Student> getAllStudents(String searchBy, String orderBy, String isAsc, List<String> subjects) {
        // Ascending or Descending order
        Sort.Direction sortDirection = (isAsc == null || !Objects.equals(isAsc, "false")) ? Sort.Direction.ASC : Sort.Direction.DESC;          // default to true

        // Sort by category
        Sort customCategorySort = switch (orderBy) {
            case null       -> Sort.by(sortDirection, "id");            // Need to check for null (don't remove)
            case "name"     -> Sort.by(sortDirection, "name");
            case "email"    -> Sort.by(sortDirection, "email");
            case "dob"      -> Sort.by(sortDirection, "dob");
            default         -> Sort.by(sortDirection, "id");
        };

        // Search by search term
        String searchByParam = searchBy == null ? "" : searchBy;
        return studentRepository.findAllStudentsCustomQuery(
            searchByParam,
            subjects == null ? new LinkedList<>() : subjects,
            customCategorySort
        );
    }

    public Set<String> getAllSubjects() {
        return studentRepository.findAllSubjects();
    }

    // POSTs
    // When adding a new student to DB, check that this email doesn't already exist in DB & add to DB
    public Long addNewStudent(Student student) {
        studentRepository.findStudentByEmail(student.getEmail())
                .ifPresent(e -> {throw new ApiRequestException("email taken");} );        // If email already present -> throw exception

        studentRepository.save(student);    // Save to DB
        return student.getId();
    }

    // DELETEs
    public Long deleteStudent(Long studentId) {
        studentRepository.findById(studentId)
                .orElseThrow(() -> new ApiRequestException(String.format("student with id: %d not found", studentId)));

        studentRepository.deleteById(studentId);
        return studentId;
    }

    // PUTs
    @Transactional
    public Long updateStudent(Long studentId, String name, String email, String dob) {
        Student currentStudent = studentRepository.findById(studentId)                      // Check student with this id exists (else error)
                .orElseThrow(() -> new ApiRequestException(String.format("student with id %d doesn't exist", studentId)));

        if (name != null && name.length() > 0 && !Objects.equals(currentStudent.getName(), name)) {         // Check & update name
            currentStudent.setName(name);
        }

        if (email != null && email.length() > 0 && !Objects.equals(currentStudent.getEmail(), email)) {     // Check & update email
            studentRepository.findStudentByEmail(email)
                    .ifPresent(e -> {throw new ApiRequestException("email taken");} );        // If email already present -> throw exception

            currentStudent.setEmail(email);
        }

        if (dob != null && dob.length() > 0) {
            LocalDate dobLocalDate = LocalDate.parse(dob);

            if (!Objects.equals(currentStudent.getDob(), dobLocalDate))
                currentStudent.setDob(dobLocalDate);
        }

        return currentStudent.getId();
    }
}
