package course.spring.elearningplatform.exception;

public class DuplicatedEntityException extends RuntimeException {
  public DuplicatedEntityException(String message) {
    super(message);
  }
}
