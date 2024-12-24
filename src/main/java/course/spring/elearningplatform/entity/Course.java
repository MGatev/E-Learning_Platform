package course.spring.elearningplatform.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Base64;
import java.util.Date;
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

    @EqualsAndHashCode.Include
    private String name;
    private String description;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> categories;

    @ManyToOne(fetch = FetchType.EAGER)
    private User createdBy;
    private Date createdOn;

    @Lob
    private byte[] image;

    @OneToMany
    private List<Lesson> lessons;

    public String parseImage() {
        return image != null ? Base64.getEncoder().encodeToString(image) : null;
    }
}
