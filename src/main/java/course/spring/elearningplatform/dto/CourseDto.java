package course.spring.elearningplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {

    @NotBlank(message = "Name of the course cannot be empty")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;

    @NotBlank(message = "There should be a description to the course")
    @Size(min = 5, max = 1000, message = "Description must be between 3 and 1000 characters")
    private String description;

    @NotNull(message = "Image URL is required!")
    private ImageDto image;

    @NotEmpty(message = "At least one category should be added")
    private List<String> categories;
}