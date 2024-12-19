package course.spring.elearningplatform.service.impl;

import course.spring.elearningplatform.dto.ArticleDto;
import course.spring.elearningplatform.dto.mapper.ArticleDtoToArticleMapper;
import course.spring.elearningplatform.entity.Article;
import course.spring.elearningplatform.entity.Group;
import course.spring.elearningplatform.entity.User;
import course.spring.elearningplatform.exception.EntityNotFoundException;
import course.spring.elearningplatform.repository.ArticleRepository;
import course.spring.elearningplatform.service.ArticleService;
import course.spring.elearningplatform.service.GroupService;
import course.spring.elearningplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    private final GroupService groupService;
    private final UserService userService;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository, GroupService groupService, UserService userService) {
        this.articleRepository = articleRepository;
        this.groupService = groupService;
        this.userService = userService;
    }

    @Override
    public Article createArticle(Long groupId, ArticleDto articleDto) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User user = userService.getUserByUsername(username);
        Group group = groupService.getGroupById(groupId);
        Article article = ArticleDtoToArticleMapper.mapArticleDtoToArticle(articleDto, user, group);
        return articleRepository.save(article);
    }

    @Override
    public List<Article> getAllArticlesForAGroup(Long groupId) {
        return articleRepository.findByGroupId(groupId) != null ? articleRepository.findByGroupId(groupId) : List.of();
    }

    @Override
    public Article deleteArticleById(Long id) {
        Article article = articleRepository.findById(id).orElse(null);
        if (article == null) {
            throw new EntityNotFoundException(String.format("Article with id=%s not found", id), "redirect:/groups");
        }
        articleRepository.deleteById(id);
        return article;
    }
}
