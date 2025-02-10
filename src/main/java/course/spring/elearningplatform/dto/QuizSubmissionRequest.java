package course.spring.elearningplatform.dto;

import course.spring.elearningplatform.entity.Response;

import java.util.List;

public class QuizSubmissionRequest {
    private List<Response> answers;
    private long elapsedTime;
    public List<Response> getAnswers() {
        return answers;
    }
    public long getElapsedTime() {
        return elapsedTime;
    }

}
