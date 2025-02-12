package course.spring.elearningplatform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"groups", "courses", "startedCourses", "completedCourses", "tickets", "completedLessons", "certificates"})
public class User {

    public static final String ROLE_STUDENT = "STUDENT";
    public static final String ROLE_INSTRUCTOR = "INSTRUCTOR";
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_UNREGISTERED = "UNREGISTERED";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @EqualsAndHashCode.Include
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profile_picture_id")
    private Image profilePicture;

    @Transient
    private String profilePictureBase64;

    @NonNull
    @NotEmpty
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles;


    public String getFullName() {
        return firstName + " " + lastName;
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Set<Group> groups;

    @OneToMany
    private Set<Course> courses;

    @ManyToMany
    @JoinTable(
            name = "user_started_courses",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> startedCourses;

    @ManyToMany
    @JoinTable(
            name = "user_completed_courses",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> completedCourses;

    @OneToMany(mappedBy = "issuer")
    private List<Ticket> tickets;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_completed_lessons",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "lesson_id")
    )
    private Set<Lesson> completedLessons = new HashSet<>();

    @OneToMany(mappedBy = "issuedTo", cascade = CascadeType.ALL)
    private List<Certificate> certificates;

    public void addCertificate(Certificate certificate) {
        certificates.add(certificate);
    }

    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
    }

    public void addStartedCourse(Course course) {
        startedCourses.add(course);
    }

    public void addCompletedCourse(Course course) {
        startedCourses.remove(course);
        completedCourses.add(course);
    }

    public User(String username, String password, String firstName, String lastName, String email, @NonNull Set<String> roles) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.roles = roles;
    }

    public boolean isAdmin() {
        return roles.contains(Role.ADMIN.getDescription());
    }

    public boolean hasRole(String role) {
        return roles.contains(role);
    }
}

