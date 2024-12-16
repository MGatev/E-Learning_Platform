package course.spring.elearningplatform.dto;

import course.spring.elearningplatform.entity.Article;
import course.spring.elearningplatform.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class GroupDto {

  @NotBlank
  @Size(min = 3, max = 256, message = "Name must be between 3 and 256 characters!")
  private String name;

  @NotNull(message = "Image URL is required!")
  private String imageUrl;

  private Set<User> members;

  private List<Article> articles;
}
