package course.spring.elearningplatform.dto.mapper;

import course.spring.elearningplatform.dto.ArticleDto;
import course.spring.elearningplatform.entity.Article;
import course.spring.elearningplatform.entity.Group;
import course.spring.elearningplatform.entity.User;

import java.time.LocalDateTime;

public class ArticleDtoToArticleMapper {
    public static Article mapArticleDtoToArticle(ArticleDto articleDto, User user, Group group) {
        Article article = new Article();
        article.setCreatedAt(LocalDateTime.now());
        article.setContent(articleDto.getContent());
        article.setGroup(group);
        article.setAuthor(user);
        return article;
    }
}
