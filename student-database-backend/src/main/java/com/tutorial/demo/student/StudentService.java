package com.tutorial.demo.student;

/*
    SERVICE LAYER:
    For business logic
*/

import com.tutorial.demo.exception.ApiRequestException;
import com.tutorial.demo.yeargroup.YearGroupEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component      // Allows this class to be 'injected' into constructor for another class -> For @Autowired
public class StudentService {

    private final StudentRepository studentRepository;
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    @Value("${config.database.number_of_items_per_page}")
    private int NUMBER_OF_ITEMS_PER_PAGE;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getAllStudents() {
        logger.info("Request - Get all students");
        return studentRepository.findAll();
    }

    // GETTERS
    public Page<Student> getAllStudentsByRequest(String searchBy, String orderBy, String isAsc, int pageNumber, List<String> subjects) {
        logger.info("Request - Get all students with params");

        // Ascending or Descending order
        Sort.Direction sortDirection = !Objects.equals(isAsc, "false") ? Sort.Direction.ASC : Sort.Direction.DESC;

        // Sort by category
        Sort customCategorySort = Sort.by(sortDirection, orderBy);

        // Page request (sort + pagination)
        Pageable pageableRequest = PageRequest.of(pageNumber, NUMBER_OF_ITEMS_PER_PAGE, customCategorySort);

        return studentRepository.findAllStudentsCustomQuery(
            searchBy,
            subjects,
            pageableRequest
        );
    }

    public long getCountStudentsByRequest(String searchBy, List<String> subjects) {
        return studentRepository.countByCustomQuery(
            searchBy,
            subjects
        );
    }

    public Student getStudentById(Long studentId) {
        logger.info("Request - Get student by student-id");

        return studentRepository.findById(studentId)
                .orElseThrow(() -> new ApiRequestException(String.format("student with id: %d not found", studentId)));
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
        logger.info("POST student: " + student);

        return student.getId();
    }

    // DELETEs
    public Long deleteStudent(Long studentId) {
        studentRepository.findById(studentId)
                .orElseThrow(() -> new ApiRequestException(String.format("student with id: %d not found", studentId)));

        studentRepository.deleteById(studentId);
        logger.info("DELETE student, with ID = " + studentId);

        return studentId;
    }

    // PUTs
    @Transactional
    public Long updateStudent(Long studentId, String name, String email, String image, String dob, YearGroupEnum yeargroup, Set<String> subjects) {
        // Check student with this id exists (else error)
        Student currentStudent = studentRepository.findById(studentId)
                .orElseThrow(() -> new ApiRequestException(String.format("student with id %d doesn't exist", studentId)));

        // Check & update name
        if (name != null && name.length() > 0 && !Objects.equals(currentStudent.getName(), name)) {
            currentStudent.setName(name);
        }

        // Check & update email
        if (email != null && email.length() > 0 && !Objects.equals(currentStudent.getEmail(), email)) {
            studentRepository.findStudentByEmail(email)
                    .ifPresent(e -> {throw new ApiRequestException("email taken");} );        // If email already present -> throw exception

            currentStudent.setEmail(email);
        }

        // Check & update image
        if (image != null && image.length() > 0 && !Objects.equals(currentStudent.getImage(), image)) {
            currentStudent.setImage(image);
        }

        // Check & update dob
        if (dob != null && dob.length() > 0) {
            LocalDate dobLocalDate = LocalDate.parse(dob);

            if (!Objects.equals(currentStudent.getDob(), dobLocalDate))
                currentStudent.setDob(dobLocalDate);
        }

        // Check & update yeargroup
        if (yeargroup != null && !Objects.equals(currentStudent.getYeargroup(), yeargroup)) {
            currentStudent.setYeargroup(yeargroup);
        }

        // Check & update subjects[]
        if (subjects != null && !Objects.equals(currentStudent.getSubjects(), subjects)) {
            currentStudent.setSubjects(subjects);
        }

        logger.info("PUT student: " + currentStudent);
        return currentStudent.getId();
    }
}
