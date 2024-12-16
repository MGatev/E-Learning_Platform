package course.spring.elearningplatform.dto;

import course.spring.elearningplatform.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Set;

public class GroupDto {

  @NotBlank
  @Size(min = 3, max = 256)
  private String name;

  @NotNull
  private String imageUrl;

  private Set<User> members;

  private List<Article> articles;
}
