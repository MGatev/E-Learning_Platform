package course.spring.elearningplatform.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class QuestionDto {

    @NotBlank(message = "Question title is required")
    private String questionTitle;

    @NotBlank(message = "Option 1 is required")
    private String option1;

    @NotBlank(message = "Option 2 is required")
    private String option2;

    @NotBlank(message = "Option 3 is required")
    private String option3;

    @NotBlank(message = "Option 4 is required")
    private String option4;

    @NotBlank(message = "Correct answer is required")
    private String correctAnswer;

    @NotBlank(message = "Difficulty is required")
    private String difficulty;
}