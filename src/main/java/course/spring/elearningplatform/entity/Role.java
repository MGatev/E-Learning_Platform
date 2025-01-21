package course.spring.elearningplatform.entity;

import lombok.Getter;

@Getter
public enum Role {

    STUDENT("ROLE_STUDENT"),
    INSTRUCTOR("ROLE_INSTRUCTOR"),
    ADMIN("ROLE_ADMIN"),
    UNREGISTERED("ROLE_UNREGISTERED");

    Role(String description) {
        this.description = description;
    }
    private final String description;
}
