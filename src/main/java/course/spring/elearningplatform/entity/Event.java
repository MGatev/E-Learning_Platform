package course.spring.elearningplatform.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    private String instructor;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "image_id")
    private Image image;

    @Transient
    private String imageBase64;
}

