package course.spring.elearningplatform.service;

import course.spring.elearningplatform.entity.Solution;
import course.spring.elearningplatform.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface SolutionService {
    Solution saveSolution(Solution solution);
    List<Solution> getSolutionsByAssignmentId(Long assignmentId);
    void deleteSolution(Long id);
    boolean hasUserUploadedSolution(Long userId, Long assignmentId) ;
    void handleSolutionUpload(User user, Long assignmentId, MultipartFile file) throws IOException;
}
