package course.spring.elearningplatform.web;

import course.spring.elearningplatform.dto.AssignmentDto;
import course.spring.elearningplatform.dto.CourseDto;
import course.spring.elearningplatform.dto.LessonDto;
import course.spring.elearningplatform.dto.mapper.EntityMapper;
import course.spring.elearningplatform.entity.Course;
import course.spring.elearningplatform.entity.CustomUserDetails;
import course.spring.elearningplatform.entity.Lesson;
import course.spring.elearningplatform.entity.User;
import course.spring.elearningplatform.exception.DuplicatedEntityException;
import course.spring.elearningplatform.service.CourseService;
import course.spring.elearningplatform.service.LessonService;
import course.spring.elearningplatform.service.SolutionService;
import course.spring.elearningplatform.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;
    private final LessonService lessonService;
    private final UserService userService;
    private final SolutionService solutionService;

    @Autowired
    public CourseController(final CourseService courseService, LessonService lessonService, UserService userService, SolutionService solutionService) {
        this.courseService = courseService;
        this.lessonService = lessonService;
        this.userService = userService;
        this.solutionService = solutionService;
    }

    @GetMapping("/create")
    public String showCreateCoursePage(Model model) {
        if (!model.containsAttribute("course")) {
            model.addAttribute("course", new CourseDto());
        }
        return "create-course";
    }

    @PostMapping("/create")
    public String createCourse(@Valid @ModelAttribute("course") CourseDto course, BindingResult bindingResult, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (course.getCategories().stream().anyMatch(category -> category == null || category.trim().isEmpty())) {
            bindingResult.rejectValue("categories", "error.categories", "Please enter a category.");
            return "create-course";
        }

        if (bindingResult.hasErrors()) {
            return "create-course";
        }

        Course newCourse = courseService.addCourse(course, userDetails.getUser());
        return "redirect:/courses/" + newCourse.getId();
    }

    @GetMapping("/{id}")
    public String getCourseById(@PathVariable("id") Long id,
                                @AuthenticationPrincipal UserDetails userDetails,
                                Model model) {
        User user = userService.getUserByUsername(userDetails.getUsername());
        Course course = courseService.getCourseById(id);

        List<AssignmentDto> assignments = course.getAssignments().stream()
                .map(assignment -> EntityMapper.mapEntityToDto(assignment, AssignmentDto.class))
                .toList();

        Map<Long, Boolean> userSolutionStatus = assignments.stream()
                .collect(Collectors.toMap(
                        AssignmentDto::getId,
                        assignment -> solutionService.hasUserUploadedSolution(user.getId(), assignment.getId())
                ));

        model.addAttribute("course", course);
        model.addAttribute("assignments", assignments);
        model.addAttribute("user", user);
        model.addAttribute("userSolutionStatus", userSolutionStatus); // Add this to the model
        model.addAttribute("allLessonsCompleted", courseService.areAllLessonsCompletedByUser(user, course));

        return "course";
    }

    @GetMapping("/{id}/lessons/create")
    public String showCreateLessonPage(@PathVariable("id") Long id, Model model) {
        model.addAttribute("course", courseService.getCourseById(id));
        model.addAttribute("lesson", new LessonDto());
        return "create-lesson";
    }

    @PostMapping("/{id}/lessons/create")
    public String createLesson(
            @PathVariable("id") Long id,
            @Valid @ModelAttribute("lesson") LessonDto lesson,
            BindingResult bindingResult,
            Model model) {

        Course course = courseService.getCourseById(id);
        model.addAttribute("course", course);

        if (bindingResult.hasErrors()) {
            return "create-lesson";
        }

        lessonService.addLesson(lesson, (Course) model.getAttribute("course"));

        return "redirect:/courses/" + id;
    }

    @GetMapping("/{courseId}/lessons/{lessonId}")
    public String getLessonById(@PathVariable("courseId") Long courseId, @PathVariable Long lessonId,
                                @RequestParam(name = "lessonIndex", required = false) Integer lessonIndex, Model model,
                                @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("course", courseService.getCourseById(courseId));
        model.addAttribute("lesson", lessonService.getLessonById(lessonId));
        model.addAttribute("lessonIndex", lessonIndex);
        model.addAttribute("user", userService.getUserByUsername(userDetails.getUsername()));

        return "lesson";
    }

    @PostMapping("/{courseId}/lessons/{lessonId}/complete")
    public String markLessonAsCompleted(@PathVariable Long courseId, @PathVariable Long lessonId, @AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes) {
        User user = userService.getUserByUsername(userDetails.getUsername());
        Lesson lesson = lessonService.getLessonById(lessonId);
        user.getCompletedLessons().add(lesson);
        userService.save(user);

        redirectAttributes.addFlashAttribute("message", "Lesson marked as completed!");
        return "redirect:/courses/" + courseId;
    }

    @GetMapping("/category/{category}")
    public String showAllCoursesForCategory(@PathVariable("category") String category, Model model) {
        model.addAttribute("category", category);
        model.addAttribute("courses", courseService.getCoursesByCategory(category));

        return "course-by-category";
    }

    @ExceptionHandler(DuplicatedEntityException.class)
    public String handleDuplicatedEntityException(DuplicatedEntityException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/courses/create";
    }
}
