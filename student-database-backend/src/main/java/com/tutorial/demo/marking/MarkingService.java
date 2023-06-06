package com.tutorial.demo.marking;

import com.tutorial.demo.course.Course;
import com.tutorial.demo.exception.ApiRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Component
public class MarkingService {
    private final MarkingRepository markingRepository;

    @Value("${config.database.number_of_items_per_page}")
    private int NUMBER_OF_ITEMS_PER_PAGE;

    @Autowired
    public MarkingService(MarkingRepository markingRepository) {
        this.markingRepository = markingRepository;
    }


    public List<Marking> getAllMarks() {
        return markingRepository.findAll();
    }

    public Page<Marking> getMarksByStudentIdByRequest(Long studentId, String searchBy, String orderBy, String isAsc, int pageNumber) {
        Sort.Direction sortDirection = !Objects.equals(isAsc, "false") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort customCategorySort = Sort.by(sortDirection, orderBy);
        Pageable pageableRequest = PageRequest.of(pageNumber, NUMBER_OF_ITEMS_PER_PAGE, customCategorySort);

        return markingRepository.findByStudentIdCustomQuery(
                studentId,
                searchBy,
                pageableRequest
        );
    }

    public List<?> getMarksStatisticsByStudentId(Long studentId) {
        return markingRepository.findMarksStatisticsByStudentId(studentId);
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
    public Long updateMarking(Long markingId, Course course, String title, Float score) {
        Marking currentMarking = markingRepository.findById(markingId)
                .orElseThrow(() -> new ApiRequestException(String.format("marking with id %d doesn't exist", markingId)));

        if (course != null)
            currentMarking.setCourse(course);

        if (title != null)
            currentMarking.setTitle(title);

        if (score != null)
            currentMarking.setScore(score);

        return currentMarking.getId();
    }
}
