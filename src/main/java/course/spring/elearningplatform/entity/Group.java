package course.spring.elearningplatform.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Base64;
import java.util.List;

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

  @Lob
  private byte[] image;

  private String description;

  @ManyToMany(fetch = FetchType.EAGER)
  @CollectionTable(name = "group_members")
  private List<User> members;

  @OneToMany(mappedBy = "group")
  private List<Article> articles;

  public String parseImage() {
    return image != null ? Base64.getEncoder().encodeToString(image) : null;
  }
}