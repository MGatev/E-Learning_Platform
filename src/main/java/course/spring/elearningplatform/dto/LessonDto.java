package course.spring.elearningplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class LessonDto {
    @NotBlank
    @Size(min = 3, max = 256, message = "Title must be between 3 and 256 characters!")
    private String title;

    @NotBlank
    @Size(min = 10, max = 65535, message = "Content must be between 10 and 65535 characters!")
    private String content;
}