package course.spring.elearningplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private Long courseId;
}
