package com.tutorial.demo.marking;


import com.tutorial.demo.course.Course;

public class MarkingResponse {
    private Course course;
    private long courseAverage;

    public MarkingResponse(Course course, long courseAverage) {
        this.course = course;
        this.courseAverage = courseAverage;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public long getCourseAverage() {
        return courseAverage;
    }

    public void setCourseAverage(long courseAverage) {
        this.courseAverage = courseAverage;
    }
}
