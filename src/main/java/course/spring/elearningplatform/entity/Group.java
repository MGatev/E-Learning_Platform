package course.spring.elearningplatform.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Table(name = "learning_groups")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Group {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @EqualsAndHashCode.Include
  private String name;

  private String imageUrl;

  @ManyToMany(fetch = FetchType.EAGER)
  @CollectionTable(name = "group_members")
  private Set<User> members;

  @OneToMany(mappedBy = "group")
  private List<Article> articles;
}

