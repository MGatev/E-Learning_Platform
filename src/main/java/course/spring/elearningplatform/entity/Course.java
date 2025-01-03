package course.spring.elearningplatform.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

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

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "image_id")
    private Image image;

    @Transient
    private String imageBase64;

    @OneToMany
    private List<Lesson> lessons;

    @OneToMany
    private List<Question> questions;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Quiz quiz;

    @OneToMany(mappedBy = "forCourse")
    private List<Ticket> tickets;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Assignment> assignments = new ArrayList<>();


    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
    }
}
