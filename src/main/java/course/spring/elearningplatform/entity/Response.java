package course.spring.elearningplatform.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Response {
    private long questionId;
    private String answer;
}
