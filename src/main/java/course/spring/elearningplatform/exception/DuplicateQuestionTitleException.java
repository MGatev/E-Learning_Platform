package course.spring.elearningplatform.exception;

public class DuplicateQuestionTitleException extends RuntimeException {
    public DuplicateQuestionTitleException(String message) {
        super(message);
    }

    public DuplicateQuestionTitleException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateQuestionTitleException(Throwable cause) {
        super(cause);
    }
}
