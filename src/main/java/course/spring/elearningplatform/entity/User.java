package course.spring.elearningplatform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    public static final String ROLE_STUDENT = "ROLE_STUDENT";
    public static final String ROLE_INSTRUCTOR = "ROLE_INSTRUCTOR";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_UNREGISTERED = "ROLE_UNREGISTERED";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @EqualsAndHashCode.Include
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String profilePicture;

    @NonNull
    @NotEmpty
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles;


    public String getFullName() {
        return firstName + " " + lastName;
    }
//
//    public User(String username, String password, String firstName, String lastName, String email) {
//        this.username = username;
//        this.password = password;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.email = email;
//    }

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Group> groups;

    @OneToMany
    private Set<Course> courses;

    @ManyToMany
    @JoinTable(
            name = "user_completed_lessons",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "lesson_id")
    )
    private Set<Lesson> completedLessons = new HashSet<>();
}

