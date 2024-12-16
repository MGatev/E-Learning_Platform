package course.spring.elearningplatform.web;

import course.spring.elearningplatform.service.ArticleService;
import course.spring.elearningplatform.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/groups")
public class GroupController {
    private GroupService groupService;
    private ArticleService articleService;

    @Autowired
    public GroupController(GroupService groupService, ArticleService articleService) {
        this.groupService = groupService;
        this.articleService = articleService;
    }

    @GetMapping("/{id}/articles")
    private String getAllArticles() {
        return "articles";
    }

    @DeleteMapping("/{id}/articles/{articleId}")
    private String deleteArticle(@PathVariable Long id, @PathVariable Long articleId) {
        articleService.deleteArticleById(articleId);
        return "redirect:/groups/" + id + "/articles";
    }

    @PostMapping("/{id}/articles/create")
    private String createArticle() {
        return "groups";
    }
}
