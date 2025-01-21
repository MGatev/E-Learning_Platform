package course.spring.elearningplatform.repository;

import course.spring.elearningplatform.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
  List<Article> findByGroupId(Long groupId);
}
