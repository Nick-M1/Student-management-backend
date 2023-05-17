package com.tutorial.demo.marking;

import com.tutorial.demo.course.Course;

import java.util.List;
import java.util.Map;

public class MarkingResponse {
    private List<Marking> markings;
    private Map<Course, Double> mean;

    public MarkingResponse(List<Marking> markings, Map<Course, Double> mean) {
        this.markings = markings;
        this.mean = mean;
    }

    public List<Marking> getMarkings() {
        return markings;
    }

    public void setMarkings(List<Marking> markings) {
        this.markings = markings;
    }

    public Map<Course, Double> getMean() {
        return mean;
    }

    public void setMean(Map<Course, Double> mean) {
        this.mean = mean;
    }
}
