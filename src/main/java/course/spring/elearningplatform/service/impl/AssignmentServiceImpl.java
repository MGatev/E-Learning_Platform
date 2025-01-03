
package course.spring.elearningplatform.service.impl;

import course.spring.elearningplatform.dto.AssignmentDto;
import course.spring.elearningplatform.entity.*;
import course.spring.elearningplatform.exception.EntityNotFoundException;
import course.spring.elearningplatform.repository.AssignmentRepository;
import course.spring.elearningplatform.repository.CourseRepository;
import course.spring.elearningplatform.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

import static course.spring.elearningplatform.dto.mapper.EntityMapper.mapCreateDtoToEntity;

@Service
public class AssignmentServiceImpl implements AssignmentService {
    private final AssignmentRepository assignmentRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public AssignmentServiceImpl(AssignmentRepository assignmentRepository, CourseRepository courseRepository) {
        this.assignmentRepository = assignmentRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public List<AssignmentDto> getAllAssignments() {
        return assignmentRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AssignmentDto> getAssignmentsByCourseId(Long courseId) {
        return assignmentRepository.findByCourseId(courseId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public AssignmentDto getAssignmentById(Long id) {
        return assignmentRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new EntityNotFoundException("Assignment not found"));
    }

    @Override
    public AssignmentDto saveAssignment(AssignmentDto assignmentDto) {
        Assignment assignment = new Assignment();
        assignment.setTitle(assignmentDto.getTitle());
        assignment.setDescription(assignmentDto.getDescription());
        assignment.setDueDate(assignmentDto.getDueDate());

        if (assignmentDto.getCourseId() != null) {
            Course course = courseRepository.findById(assignmentDto.getCourseId())
                    .orElseThrow(() -> new EntityNotFoundException("Course not found"));
            assignment.setCourse(course);
        }

        Assignment savedAssignment = assignmentRepository.save(assignment);
        return mapToDto(savedAssignment);
    }

    @Override
    public void deleteAssignment(Long id) {
        assignmentRepository.deleteById(id);
    }

    private AssignmentDto mapToDto(Assignment assignment) {
        return new AssignmentDto(
                assignment.getId(),
                assignment.getTitle(),
                assignment.getDescription(),
                assignment.getDueDate(),
                assignment.getCourse() != null ? assignment.getCourse().getId() : null
        );
    }
}