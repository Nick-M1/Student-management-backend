package com.tutorial.demo.marking;

import com.tutorial.demo.course.Course;
import com.tutorial.demo.exception.ApiRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Component
public class MarkingService {
    private final MarkingRepository markingRepository;

    @Autowired
    public MarkingService(MarkingRepository markingRepository) {
        this.markingRepository = markingRepository;
    }


    public List<Marking> getAllMarks() {
        return markingRepository.findAll();
    }

    public List<Marking> getMarksByStudentId(Long studentId) {
        return markingRepository.findByStudentId(studentId);
    }

    public MarkingResponse getMarksAndStatisticsByStudentId(Long studentId) {
        List<Marking> markings = markingRepository.findByStudentId(studentId);
        Map<Course, List<Marking>> groupedMarkings = markings.stream().collect(groupingBy(Marking::getCourse));

        Map<Course, Double> meanMap = new HashMap<>();
        groupedMarkings.forEach((key, value) ->
                meanMap.put(key ,value.stream().mapToDouble(Marking::getScore).sum() / value.size())
        );

        return new MarkingResponse(
                markings,
                meanMap
        );
    }

    public Long addNewMarking(Marking marking) {
        markingRepository.save(marking);    // Save to DB
        return marking.getId();
    }

    public Long deleteMarking(Long markingId) {
        markingRepository.findById(markingId)
                .orElseThrow(() -> new ApiRequestException(String.format("marking with id: %d not found", markingId)));

        markingRepository.deleteById(markingId);
        return markingId;
    }

    @Transactional
    public Long updateMarking(Long markingId, Course course, Float score) {
        Marking currentMarking = markingRepository.findById(markingId)
                .orElseThrow(() -> new ApiRequestException(String.format("marking with id %d doesn't exist", markingId)));

        if (course != null)
            currentMarking.setCourse(course);

        if (score != null)
            currentMarking.setScore(score);

        return currentMarking.getId();
    }
}
