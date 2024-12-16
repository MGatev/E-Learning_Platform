package course.spring.elearningplatform.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/groups")
public class GroupController {
    @GetMapping("/{id}/articles")
    private String getAllArticles() {
        return "articles";
    }

    @DeleteMapping("/{id}/articles/{articleId}")
    private String deleteArticle() {
        return "groups";
    }

    @PostMapping("/{id}/articles/create")
    private String createArticle() {
        return "groups";
    }
}
