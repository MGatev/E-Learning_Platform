package course.spring.elearningplatform.service;

import course.spring.elearningplatform.dto.AssignmentDto;
import course.spring.elearningplatform.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface AssignmentService {
    List<AssignmentDto> getAllAssignments();
    List<AssignmentDto> getAssignmentsByCourseId(Long courseId);
    AssignmentDto getAssignmentById(Long id);
    AssignmentDto saveAssignment(AssignmentDto assignmentDto);
    void deleteAssignment(Long id);
}
