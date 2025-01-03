package course.spring.elearningplatform.web;

import course.spring.elearningplatform.entity.Solution;
import course.spring.elearningplatform.service.SolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/solutions")
public class SolutionController {

    @Autowired
    private SolutionService solutionService;

    @PostMapping
    public Solution uploadSolution(@RequestBody Solution solution) {
        return solutionService.saveSolution(solution);
    }

    @GetMapping("/assignment/{assignmentId}")
    public List<Solution> getSolutionsByAssignmentId(@PathVariable Long assignmentId) {
        return solutionService.getSolutionsByAssignmentId(assignmentId);
    }

    @DeleteMapping("/{id}")
    public void deleteSolution(@PathVariable Long id) {
        solutionService.deleteSolution(id);
    }
}
