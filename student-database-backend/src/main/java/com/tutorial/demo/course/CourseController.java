package com.tutorial.demo.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="api/v1/courses")
public class CourseController {
    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping(path = "")
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping(path = "{courseId}")
    public Course getStudentById(@PathVariable("courseId") Long courseId) {
        return courseService.getCourseById(courseId);
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
            @RequestParam(value = "title", required = false, defaultValue = "") String title) {
        return courseService.updateCourse(courseId, code, title);
    }
}
