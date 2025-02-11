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
import course.spring.elearningplatform.service.*;
import course.spring.elearningplatform.service.impl.CourseDashboardServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;
    private final LessonService lessonService;
    private final UserService userService;
    private final SolutionService solutionService;
    private final CourseDashboardService courseDashboardService;
    private final ActivityLogService activityLogService;

    @Autowired
    public CourseController(final CourseService courseService, LessonService lessonService, UserService userService,
                            SolutionService solutionService, CourseDashboardService courseDashboardService, ActivityLogService activityLogService) {
        this.courseService = courseService;
        this.lessonService = lessonService;
        this.userService = userService;
        this.solutionService = solutionService;
        this.courseDashboardService = courseDashboardService;
        this.activityLogService = activityLogService;
    }

    @GetMapping
    public String showAllCourses(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        getTop3CoursesGroupedByCategory(model, courseService);
        if (userDetails != null) {
            model.addAttribute("loggedUser", userDetails.getUser());
        }
        return "courses";
    }

    static void getTop3CoursesGroupedByCategory(Model model, CourseService courseService) {
        Map<String, Set<Course>> coursesByCategory = courseService.getCoursesGroupedByCategory();
        Map<String, List<Course>> top3CoursesByCategory = coursesByCategory.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey, // Keep the category key as it is
                        entry -> entry.getValue().stream() // Process the list of courses
                                .limit(3) // Limit to the first 3 courses
                                .toList() // Collect into a new list
                ));

        model.addAttribute("top3CoursesByCategory", top3CoursesByCategory);
    }

    @GetMapping("/create")
    public String showCreateCoursePage(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (!model.containsAttribute("course")) {
            model.addAttribute("course", new CourseDto());
        }
        model.addAttribute("loggedUser", userDetails.getUser());
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

        Course newCourse = courseService.addCourse(course, userDetails.user());
        activityLogService.logActivity("New course created", userDetails.getUsername());
        return "redirect:/courses/" + newCourse.getId();
    }

    @PostMapping("/update-course-name")
    public ResponseEntity<String> updateCourseName(@RequestBody Map<String, String> payload, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long id = Long.parseLong(payload.get("id"));
        String newName = payload.get("course-name");
        courseService.updateCourseDetails(id, "course-name", newName);
        activityLogService.logActivity("Course name updated", userDetails.getUsername());
        return ResponseEntity.ok("Name updated successfully");
    }

    @PostMapping("/update-course-description")
    public ResponseEntity<String> updateCourseDescription(@RequestBody Map<String, String> payload, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long id = Long.parseLong(payload.get("id"));
        String newDescription = payload.get("course-description");
        courseService.updateCourseDetails(id, "course-description", newDescription);
        activityLogService.logActivity("Course description updated", userDetails.getUsername());
        return ResponseEntity.ok("Description updated successfully");
    }

    @PostMapping("/update-add-category")
    public ResponseEntity<String> updateAddCategory(@RequestBody Map<String, String> payload, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long id = Long.parseLong(payload.get("id"));
        String newCategory = payload.get("add-category");
        courseService.updateCourseDetails(id, "add-category", newCategory);
        activityLogService.logActivity("New category added to course", userDetails.getUsername());
        return ResponseEntity.ok("New category added successfully");
    }

    @GetMapping("/{id}")
    public String getCourseById(@PathVariable("id") Long id,
                                @AuthenticationPrincipal UserDetails userDetails,
                                Model model) {
        if (userDetails == null) {
            model.addAttribute("course", courseService.getCourseById(id));
            model.addAttribute("isCreator", false);
            return "course";
        }
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

        model.addAttribute("loggedUser", user);
        model.addAttribute("highscores", courseService.getHighScoresForCourse(id));
        model.addAttribute("course", course);
        model.addAttribute("assignments", assignments);
        model.addAttribute("analytics", course.getAnalytics());
        model.addAttribute("user", user);
        model.addAttribute("userSolutionStatus", userSolutionStatus);
        model.addAttribute("allLessonsCompleted", courseService.areAllLessonsCompletedByUser(user, course));
        model.addAttribute("isCreator", course.getCreatedBy().getId().equals(user.getId()));
        model.addAttribute("isCourseStarted", user.getStartedCourses().contains(course));
        model.addAttribute("isCourseCompleted", user.getCompletedCourses().contains(course));

        return "course";
    }

    @GetMapping("/{id}/lessons/create")
    public String showCreateLessonPage(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        model.addAttribute("course", courseService.getCourseById(id));
        model.addAttribute("lesson", new LessonDto());
        model.addAttribute("loggedUser", userDetails.user());
        return "create-lesson";
    }

    @PostMapping("/{id}/lessons/create")
    public String createLesson(
            @PathVariable("id") Long id,
            @Valid @ModelAttribute("lesson") LessonDto lesson,
            BindingResult bindingResult,
            Model model,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Course course = courseService.getCourseById(id);
        model.addAttribute("course", course);

        if (bindingResult.hasErrors()) {
            return "create-lesson";
        }

        lessonService.addLesson(lesson, (Course) model.getAttribute("course"));

        activityLogService.logActivity("New lesson created", userDetails.getUsername());
        return "redirect:/courses/" + id;
    }

    @GetMapping("/{courseId}/lessons/{lessonId}")
    public String getLessonById(@PathVariable("courseId") Long courseId, @PathVariable Long lessonId,
                                @RequestParam(name = "lessonIndex", required = false) Integer lessonIndex, Model model,
                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        Course course = courseService.getCourseById(courseId);
        Lesson lesson = lessonService.getLessonById(lessonId);
        User user = userDetails.getUser();
        model.addAttribute("course", course);
        model.addAttribute("lesson", lesson);
        model.addAttribute("completedLessons", user.getCompletedLessons());
        model.addAttribute("lessonIndex", lessonIndex);
        model.addAttribute("user", user);
        model.addAttribute("isCreator", course.getCreatedBy().getId().equals(user.getId()));
        model.addAttribute("loggedUser", user);

        return "lesson";
    }

    @PostMapping("/{courseId}/lessons/{lessonId}/complete")
    public String markLessonAsCompleted(@PathVariable Long courseId, @PathVariable Long lessonId, @AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes) {
        User user = userService.getUserByUsername(userDetails.getUsername());
        Lesson lesson = lessonService.getLessonById(lessonId);
        user.getCompletedLessons().add(lesson);
        userService.save(user);

        redirectAttributes.addFlashAttribute("message", "Lesson marked as completed!");
        activityLogService.logActivity("Completed lesson", userDetails.getUsername());
        return "redirect:/courses/" + courseId;
    }

    @PostMapping("/{courseId}/lessons/update-lesson-title")
    public ResponseEntity<String> updateLessonTitle(@PathVariable Long courseId, @RequestBody Map<String, String> payload, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long id = Long.parseLong(payload.get("id"));
        String newTitle = payload.get("lesson-title");

        Course course = courseService.getCourseById(courseId);

        lessonService.updateLessonDetails(course, id, "lesson-title", newTitle);
        activityLogService.logActivity("Lesson title updated", userDetails.getUsername());
        return ResponseEntity.ok("Lesson title updated successfully");
    }

    @PostMapping("/{courseId}/lessons/update-lesson-content")
    public ResponseEntity<String> updateLessonContent(@PathVariable Long courseId, @RequestBody Map<String, String> payload, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long id = Long.parseLong(payload.get("id"));
        String newContent = payload.get("lesson-content");

        Course course = courseService.getCourseById(courseId);

        lessonService.updateLessonDetails(course, id, "lesson-content", newContent);
        activityLogService.logActivity("Lesson content updated", userDetails.getUsername());
        return ResponseEntity.ok("Lesson content updated successfully");
    }


    @GetMapping("/{courseId}/progress")
    public String getCourseDashboardPage(@PathVariable("courseId") Long courseId, Model model) {
        Course course = courseService.getCourseById(courseId);
        Map<User, CourseDashboardServiceImpl.ProgressInfo> userProgress = courseDashboardService.getUserProgressInCourse(courseId);

        model.addAttribute("course", course);
        model.addAttribute("userProgress", userProgress);
        return "course-dashboard";
    }

    @GetMapping("/category/{category}")
    public String showAllCoursesForCategory(@PathVariable("category") String category, Model model) {
        model.addAttribute("category", category);
        model.addAttribute("courses", courseService.getCoursesByCategory(category));

        return "course-by-category";
    }

    @PostMapping("/{id}/start")
    public String startCourse(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByUsername(userDetails.getUsername());
        Course startedCourse = courseService.startCourse(id, user);
        userService.addStartedCourse(user, startedCourse);
        activityLogService.logActivity("Course started", userDetails.getUsername());
        return "redirect:/courses/" + id;
    }

    @GetMapping("/student/{id}")
    public String showStudentCourses(@PathVariable Long id, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        model.addAttribute("startedCourses", courseService.getAllInProgressCoursesByUser(id));
        model.addAttribute("loggedUser", userDetails.user());
        model.addAttribute("completedCourses", courseService.findCompletedCoursesByUserId(id));
        return "student-courses";
    }

    @ExceptionHandler(DuplicatedEntityException.class)
    public String handleDuplicatedEntityException(DuplicatedEntityException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/courses/create";
    }
}
