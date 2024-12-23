package course.spring.elearningplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FAQDto {
    @NotBlank
    @Size(min = 3, max = 256, message = "Question must be between 3 and 256 characters!")
    private String question;

    @NotBlank
    @Size(min = 3, max = 256, message = "Answer must be between 3 and 256 characters!")
    private String answer;
}
