package course.spring.elearningplatform.service;

import course.spring.elearningplatform.dto.ArticleDto;
import course.spring.elearningplatform.entity.Article;

import java.util.List;

public interface ArticleService {
  Article createArticle(ArticleDto article);
  List<Article> getAllArticles();
  Article deleteArticleById(Long id);
}
