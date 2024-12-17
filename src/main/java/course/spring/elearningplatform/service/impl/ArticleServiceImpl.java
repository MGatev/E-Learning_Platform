package course.spring.elearningplatform.service.impl;

import course.spring.elearningplatform.dto.ArticleDto;
import course.spring.elearningplatform.dto.mapper.EntityMapper;
import course.spring.elearningplatform.entity.Article;
import course.spring.elearningplatform.exception.EntityNotFoundException;
import course.spring.elearningplatform.repository.ArticleRepository;
import course.spring.elearningplatform.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public Article createArticle(ArticleDto articleDto) {
        Article article = EntityMapper.mapCreateDtoToEntity(articleDto, Article.class);
        return articleRepository.save(article);
    }

    @Override
    public List<Article> getAllArticlesForAGroup(Long groupId) {
        return articleRepository.findAllByGroupId(groupId).orElseThrow(() -> new EntityNotFoundException(String.format("Articles for group with id=%s not found", groupId)));
    }

    @Override
    public Article deleteArticleById(Long id) {
        Article article = articleRepository.findById(id).orElse(null);
        if (article == null) {
            throw new EntityNotFoundException(String.format("Article with id=%s not found", id));
        }
        articleRepository.deleteById(id);
        return article;
    }
}
