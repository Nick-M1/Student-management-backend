package com.tutorial.demo.marking;

import com.tutorial.demo.course.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('SCOPE_student:read')")
    public List<Marking> getAllMarks() {
        return markingService.getAllMarks();
    }

    // GET MARKS BY STUDENT ID
    @GetMapping(path = "{studentId}")
    @PreAuthorize("hasAuthority('SCOPE_student:read_single')")
    public Page<Marking> getCoursesByRequest(
            @PathVariable("studentId") Long studentId,
            @RequestParam(value = "searchBy", required = false, defaultValue = "") String searchBy,
            @RequestParam(value = "orderBy",  required = false, defaultValue = "id") String orderBy,
            @RequestParam(value = "isAsc",  required = false, defaultValue = "true") String isAsc,
            @RequestParam(value = "page",  required = false, defaultValue = "0") int pageNumber) {
        return markingService.getMarksByStudentIdByRequest(studentId, searchBy, orderBy, isAsc, pageNumber);
    }

    @GetMapping(path = "{studentId}/statistics")
    @PreAuthorize("hasAuthority('SCOPE_student:read_single')")
    public List<?> getMarksStatisticsByStudentId(@PathVariable("studentId") Long studentId) {
        return markingService.getMarksStatisticsByStudentId(studentId);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_student:write')")
    public Long addNewMark(@RequestBody Marking marking) {
        return markingService.addNewMarking(marking);
    }

    @DeleteMapping(path="{markingId}")
    @PreAuthorize("hasAuthority('SCOPE_student:write')")
    public Long deleteMark(@PathVariable("markingId") Long markingId) {
        return markingService.deleteMarking(markingId);
    }

    @PutMapping(path="{markingId}")
    @PreAuthorize("hasAuthority('SCOPE_student:write')")
    public Long updateMark(
            @PathVariable("markingId") Long markingId,
            @RequestParam(value = "course", required = false, defaultValue = "") Course course,
            @RequestParam(value = "title", required = false, defaultValue = "") String title,
            @RequestParam(value = "score", required = false, defaultValue = "") Float score) {
        return markingService.updateMarking(markingId, course, title, score);
    }
}
