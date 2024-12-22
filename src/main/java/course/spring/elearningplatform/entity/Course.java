package course.spring.elearningplatform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Course {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @NonNull
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> categories;

    @OneToMany
    private List<Lesson> lessons;

    @OneToMany
    private List<Question> questions;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Quiz quiz;
}
