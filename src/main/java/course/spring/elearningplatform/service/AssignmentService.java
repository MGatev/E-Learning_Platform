package course.spring.elearningplatform.service;

import course.spring.elearningplatform.dto.AssignmentDto;

import java.util.List;

public interface AssignmentService {
    List<AssignmentDto> getAllAssignments();
    List<AssignmentDto> getAssignmentsByCourseId(Long courseId);
    AssignmentDto getAssignmentById(Long id);
    AssignmentDto saveAssignment(AssignmentDto assignmentDto);
    void deleteAssignment(Long id);
}
