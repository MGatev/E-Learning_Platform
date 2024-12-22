package course.spring.elearningplatform.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @ManyToMany(cascade = CascadeType.ALL) // Ensures cascading behavior for highScores
    private List<StudentResult> highScores;
    @ManyToMany(cascade = CascadeType.ALL) // Ensures cascading behavior for questions
    private List<Question> questions;

}
