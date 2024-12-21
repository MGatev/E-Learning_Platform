package course.spring.elearningplatform.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
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

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "image_id")
  private Image image;

  @Transient
  private String imageBase64;

  private String description;

  @ManyToMany(fetch = FetchType.EAGER)
  @CollectionTable(name = "group_members")
  private Set<User> members;

  @OneToMany(mappedBy = "group")
  private List<Article> articles;

  public void addMember(User user) {
    if (user == null) {
      return;
    }
    if (members == null) {
      members = new HashSet<>();
    } else if (members.contains(user)) {
      return;
    }
    members.add(user);
  }

  public void removeMember(User user) {
    if (user == null || members == null) {
      return;
    }
    members.remove(user);
  }
}