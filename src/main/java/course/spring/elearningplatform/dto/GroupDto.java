package course.spring.elearningplatform.dto;

import course.spring.elearningplatform.entity.Article;
import course.spring.elearningplatform.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class GroupDto {

  @NotBlank
  @Size(min = 3, max = 256, message = "Name must be between 3 and 256 characters!")
  private String name;

  @NotNull(message = "Image URL is required!")
  private MultipartFile image;

  private String description;

  private List<User> members;

  private List<Article> articles;
}