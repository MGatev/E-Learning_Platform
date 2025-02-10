package course.spring.elearningplatform.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDto {

    private Long id;
    @NotNull
    private String title;
    private String description;
    @NotEmpty
    private LocalDateTime startTime;
    @NotEmpty
    private LocalDateTime endTime;
    @NotNull
    private String instructor;

    private ImageDto image;
}
