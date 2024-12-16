package course.spring.elearningplatform.repository;

import course.spring.elearningplatform.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
