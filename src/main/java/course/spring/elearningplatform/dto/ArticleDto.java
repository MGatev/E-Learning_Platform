package course.spring.elearningplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class ArticleDto {

    @NotBlank(message = "Content is required")
    @Size(min = 3, max = 300, message = "Content must be between 3 and 300 characters")
    private String content;

    @NotBlank(message = "Author is required")
    private String author;

    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAt = LocalDateTime.now();

    @NotBlank
    private Long groupId;
}
