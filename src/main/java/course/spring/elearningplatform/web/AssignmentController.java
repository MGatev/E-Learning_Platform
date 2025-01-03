package course.spring.elearningplatform.web;

import course.spring.elearningplatform.dto.AssignmentDto;
import course.spring.elearningplatform.dto.mapper.Mapper;
import course.spring.elearningplatform.entity.*;
import course.spring.elearningplatform.service.AssignmentService;
import course.spring.elearningplatform.service.CourseService;
import course.spring.elearningplatform.service.SolutionService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static course.spring.elearningplatform.dto.mapper.EntityMapper.mapCreateDtoToEntity;

@AllArgsConstructor
@Controller
@RequestMapping("/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;
    private final SolutionService solutionService;
    private final CourseService courseService;

    @GetMapping
    public String getAllAssignments(Model model) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = customUserDetails.getUser();

        List<AssignmentDto> assignments = assignmentService.getAllAssignments();
        List<Course> courses = courseService.getAllCourses();

        Map<Long, String> coursesMap = courses.stream()
                .collect(Collectors.toMap(Course::getId, Course::getName));

        Map<Long, Boolean> userSolutionStatus = assignments.stream()
                .collect(Collectors.toMap(
                        AssignmentDto::getId,
                        assignment -> solutionService.hasUserUploadedSolution(user.getId(), assignment.getId())
                ));

        model.addAttribute("assignments", assignments);
        model.addAttribute("userSolutionStatus", userSolutionStatus);
        model.addAttribute("courses", courses);
        model.addAttribute("coursesMap", coursesMap);

        return "assignments";
    }


    @GetMapping("/course/{courseId}")
    public String getAssignmentsByCourseId(@PathVariable Long courseId, Model model) {
        List<AssignmentDto> assignments = assignmentService.getAssignmentsByCourseId(courseId);
        model.addAttribute("assignments", assignments);
        model.addAttribute("courseId", courseId);
        return "assignments";
    }

    @GetMapping("/{id}")
    public String getAssignmentById(@PathVariable Long id, Model model) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = customUserDetails.getUser();

        AssignmentDto assignment = assignmentService.getAssignmentById(id);
        model.addAttribute("assignment", assignment);

        String courseName = courseService.findById(assignment.getCourseId()).getName();

        model.addAttribute("assignment", assignment);
        model.addAttribute("courseName", courseName);

        boolean userSolutionUploaded = solutionService.hasUserUploadedSolution(user.getId(), id);
        model.addAttribute("userSolutionUploaded", userSolutionUploaded);

        return "assignment-detail";
    }


    @GetMapping("/new")
    public String createAssignmentForm(Model model) {
        model.addAttribute("assignment", new AssignmentDto());
        model.addAttribute("courses", courseService.getAllCourses());
        return "assignment-form";
    }

    @PostMapping
    public String createAssignment(@ModelAttribute AssignmentDto assignmentDto) {
        assignmentService.saveAssignment(assignmentDto);
        return "redirect:/assignments";
    }

    @GetMapping("/delete/{id}")
    public String deleteAssignment(@PathVariable Long id) {
        assignmentService.deleteAssignment(id);
        return "redirect:/assignments";
    }

    @PostMapping("/upload")
    public String uploadSolution(
            @RequestParam("assignmentId") Long assignmentId,
            @RequestParam("solutionFile") MultipartFile file,
            Model model) {
        try {
            CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = customUserDetails.getUser();

            if (solutionService.hasUserUploadedSolution(user.getId(), assignmentId)) {
                model.addAttribute("error", "You have already uploaded a solution for this assignment.");
                return "redirect:/assignments";
            }

            if (file.isEmpty()) {
                model.addAttribute("error", "File is empty. Please upload a valid file.");
                return "redirect:/assignments";
            }

            String uploadDir = "uploads/solutions/";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            Solution solution = new Solution();
            solution.setFilePath(filePath.toString());
            AssignmentDto assignmentDto = assignmentService.getAssignmentById(assignmentId);
            Assignment assignment = mapCreateDtoToEntity(assignmentDto, Assignment.class);
            solution.setUser(user);
            solution.setAssignment(assignment);

            solutionService.saveSolution(solution);

            return "redirect:/assignments?success=true&assignmentId=" + assignmentId;
        } catch (IOException e) {
            model.addAttribute("error", "Failed to upload file: " + e.getMessage());
            return "redirect:/assignments";
        }
    }


}
