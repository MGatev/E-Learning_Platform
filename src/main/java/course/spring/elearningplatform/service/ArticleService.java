package course.spring.elearningplatform.service;

import course.spring.elearningplatform.dto.ArticleDto;
import course.spring.elearningplatform.entity.Article;

import java.util.List;

public interface ArticleService {
  Article createArticle(Long groupId, ArticleDto article);
  List<Article> getAllArticlesForAGroup(Long groupId);
  Article deleteArticleById(Long id);
}
