package com.tutorial.demo.marking;


import java.util.List;
import java.util.Map;

public class MarkingResponse {
    private List<Marking> markings;
    private Map<String, Double> mean;

    public MarkingResponse(List<Marking> markings, Map<String, Double> mean) {
        this.markings = markings;
        this.mean = mean;
    }

    public List<Marking> getMarkings() {
        return markings;
    }

    public void setMarkings(List<Marking> markings) {
        this.markings = markings;
    }

    public Map<String, Double> getMean() {
        return mean;
    }

    public void setMean(Map<String, Double> mean) {
        this.mean = mean;
    }
}
