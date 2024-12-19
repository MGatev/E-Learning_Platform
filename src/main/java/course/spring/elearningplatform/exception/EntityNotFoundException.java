package course.spring.elearningplatform.exception;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {
  private final String redirectUrl;

  public EntityNotFoundException(String message, String redirectUrl) {
    super(message);
    this.redirectUrl = redirectUrl;
  }
}
