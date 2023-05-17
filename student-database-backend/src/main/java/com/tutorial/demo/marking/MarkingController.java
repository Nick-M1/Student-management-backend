package com.tutorial.demo.marking;

import com.tutorial.demo.course.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="api/v1/marking")
public class MarkingController {

    private final MarkingService markingService;

    @Autowired
    public MarkingController(MarkingService markingService) {
        this.markingService = markingService;
    }

    // GET ALL MARKINGS
    @GetMapping(path = "all")
    public List<Marking> getAllMarks() {
        return markingService.getAllMarks();
    }

    // GET MARKS BY STUDENT ID
    @GetMapping(path = "{studentId}")
    public List<Marking> getMarksByStudentId(@PathVariable("studentId") Long studentId) {
        return markingService.getMarksByStudentId(studentId);
    }
    @GetMapping(path = "{studentId}/statistics")
    public MarkingResponse getMarksAndStatisticsByStudentId(@PathVariable("studentId") Long studentId) {
        return markingService.getMarksAndStatisticsByStudentId(studentId);
    }

    @PostMapping
    public Long addNewMark(@RequestBody Marking marking) {
        return markingService.addNewMarking(marking);
    }

    @DeleteMapping(path="{markingId}")
    public Long deleteMark(@PathVariable("markingId") Long markingId) {
        return markingService.deleteMarking(markingId);
    }

    @PutMapping(path="{markingId}")     // Put/update request (update id, name & email)
    public Long updateMark(
            @PathVariable("markingId") Long markingId,
            @RequestParam(value = "course", required = false, defaultValue = "") Course course,
            @RequestParam(value = "score", required = false, defaultValue = "") Float score) {
        return markingService.updateMarking(markingId, course, score);
    }
}
