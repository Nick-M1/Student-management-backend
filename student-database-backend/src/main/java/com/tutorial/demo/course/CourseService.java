package com.tutorial.demo.course;

import com.tutorial.demo.exception.ApiRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
public class CourseService {
    private final CourseRepository courseRepository;
    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);

    @Value("${config.database.number_of_items_per_page}")
    private int NUMBER_OF_ITEMS_PER_PAGE;


    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Page<Course> getCoursesByRequest(String searchBy, String orderBy, String isAsc, int pageNumber, List<String> departments) {
        logger.info("Request - Get all courses with params");

        Sort.Direction sortDirection = !Objects.equals(isAsc, "false") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort customCategorySort = Sort.by(sortDirection, orderBy);
        Pageable pageableRequest = PageRequest.of(pageNumber, NUMBER_OF_ITEMS_PER_PAGE, customCategorySort);

        return courseRepository.findAllCoursesCustomQuery(
                searchBy,
                departments,
                pageableRequest
        );
    }

    public Course getCourseById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new ApiRequestException(String.format("course with id: %d not found", courseId)));
    }

    public Set<String> getAllDepartments() {
        return courseRepository.findAllDepartments();
    }


    public Long addCourse(Course course) {
        courseRepository.findByCode(course.getCode())
                .ifPresent(e -> { throw new ApiRequestException("course code taken"); } );

        course.setCode(course.getCode().toUpperCase());
        course.setTitle(course.getTitle().toLowerCase());
        course.setDepartment(course.getDepartment().toLowerCase());

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
    public Course updateCourse(Long courseId, String code, String title, String department) {
        Course foundCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> new ApiRequestException(String.format("course with id: %d not found", courseId)));

        if (code != null && !Objects.equals(foundCourse.getCode(), code) && courseRepository.findByCode(code).isEmpty())
            foundCourse.setCode(code.toUpperCase());

        if (title != null && !Objects.equals(foundCourse.getTitle(), title))
            foundCourse.setTitle(title.toLowerCase());

        if (department != null && !Objects.equals(foundCourse.getDepartment(), department))
            foundCourse.setDepartment(department.toLowerCase());

        return foundCourse;
    }
}
