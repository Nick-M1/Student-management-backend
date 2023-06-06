package com.tutorial.demo.course;

import com.tutorial.demo.student.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path="api/v1/courses")
public class CourseController {
    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping(path = "all")
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping
    public Page<Course> getCoursesByRequest(
            @RequestParam(value = "searchBy", required = false, defaultValue = "") String searchBy,
            @RequestParam(value = "orderBy",  required = false, defaultValue = "id") String orderBy,
            @RequestParam(value = "isAsc",  required = false, defaultValue = "true") String isAsc,
            @RequestParam(value = "page",  required = false, defaultValue = "0") int pageNumber,
            @RequestParam(value = "departments", required = false, defaultValue = "") List<String> departments) {
        return courseService.getCoursesByRequest(searchBy, orderBy, isAsc, pageNumber, departments);
    }

    @GetMapping(path = "{courseId}")
    public Course getStudentById(@PathVariable("courseId") Long courseId) {
        return courseService.getCourseById(courseId);
    }

    @GetMapping(path = "departments")
    public Set<String> getAllDepartments() {
        return courseService.getAllDepartments();
    }

    @PostMapping
    public Long addCourse(@RequestBody Course course) {      // @RequestBody = Gets this input from user
        return courseService.addCourse(course);
    }

    @DeleteMapping(path="{courseId}")
    public Long deleteCourse(@PathVariable("courseId") Long courseId) {
        return courseService.deleteCourse(courseId);
    }

    @PutMapping(path="{courseId}")
    public Course updateStudent(
            @PathVariable("courseId") Long courseId,
            @RequestParam(value = "code", required = false, defaultValue = "") String code,
            @RequestParam(value = "title", required = false, defaultValue = "") String title,
            @RequestParam(value = "department", required = false, defaultValue = "") String department) {
        return courseService.updateCourse(courseId, code, title, department);
    }
}
