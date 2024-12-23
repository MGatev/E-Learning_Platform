package course.spring.elearningplatform.entity;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
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

    @NonNull
    @NotEmpty
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles;


    public String getFullName() {
        return firstName + " " + lastName;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Group> groups;

}

