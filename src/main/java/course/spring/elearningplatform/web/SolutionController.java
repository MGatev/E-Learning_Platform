package course.spring.elearningplatform.web;

import course.spring.elearningplatform.entity.CustomUserDetails;
import course.spring.elearningplatform.entity.Solution;
import course.spring.elearningplatform.repository.SolutionRepository;
import course.spring.elearningplatform.service.ActivityLogService;
import course.spring.elearningplatform.service.SolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/solutions")
public class SolutionController {

    private final SolutionService solutionService;
    private final ActivityLogService activityLogService;

    @Autowired
    public SolutionController(SolutionService solutionService, ActivityLogService activityLogService) {
        this.solutionService = solutionService;
        this.activityLogService = activityLogService;
    }

    @PostMapping
    public Solution uploadSolution(@RequestBody Solution solution) {
        activityLogService.logActivity("Solution uploaded", solution.getUser().getUsername());
        return solutionService.saveSolution(solution);
    }

    @GetMapping("/assignment/{assignmentId}")
    public List<Solution> getSolutionsByAssignmentId(@PathVariable Long assignmentId) {
        return solutionService.getSolutionsByAssignmentId(assignmentId);
    }

    @DeleteMapping("/{id}")
    public void deleteSolution(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        solutionService.deleteSolution(id);
        activityLogService.logActivity("Solution deleted", userDetails.getUsername());
    }
}
