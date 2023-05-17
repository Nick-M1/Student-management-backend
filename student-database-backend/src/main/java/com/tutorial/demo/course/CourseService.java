package com.tutorial.demo.course;

import com.tutorial.demo.exception.ApiRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Component
public class CourseService {
    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new ApiRequestException(String.format("course with id: %d not found", courseId)));
    }

    public Long addCourse(Course course) {
        courseRepository.findByCode(course.getCode())
                .ifPresent(e -> { throw new ApiRequestException("course code taken"); } );

        course.setCode(course.getCode().toUpperCase());
        course.setTitle(course.getTitle().toLowerCase());

        courseRepository.save(course);
        return course.getId();
    }

    public Long deleteCourse(Long courseId) {
        courseRepository.findById(courseId)
                .orElseThrow(() -> new ApiRequestException(String.format("course with id: %d not found", courseId)));

        courseRepository.deleteById(courseId);
        return courseId;
    }

    @Transactional
    public Course updateCourse(Long courseId, String code, String title) {
        Course foundCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> new ApiRequestException(String.format("course with id: %d not found", courseId)));

        if (code != null && !Objects.equals(foundCourse.getCode(), code) && courseRepository.findByCode(code).isEmpty())
            foundCourse.setCode(code.toUpperCase());

        if (title != null && !Objects.equals(foundCourse.getTitle(), title))
            foundCourse.setTitle(title.toLowerCase());

        return foundCourse;
    }
}
