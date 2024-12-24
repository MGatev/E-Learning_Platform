package course.spring.elearningplatform.exception;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {
  private String redirectUrl;

  public EntityNotFoundException(String message, String redirectUrl) {
    super(message);
    this.redirectUrl = redirectUrl;
  }

  public EntityNotFoundException(String message) {
    super(message);
  }
}
