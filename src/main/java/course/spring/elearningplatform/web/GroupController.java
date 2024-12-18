package course.spring.elearningplatform.web;

import course.spring.elearningplatform.dto.GroupDto;
import course.spring.elearningplatform.service.ArticleService;
import course.spring.elearningplatform.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/groups")
public class GroupController {
    private final GroupService groupService;
    private final ArticleService articleService;

    @Autowired
    public GroupController(GroupService groupService, ArticleService articleService) {
        this.groupService = groupService;
        this.articleService = articleService;
    }

    @GetMapping
    public String getAllGroups(Model model) {
        model.addAttribute("groups", groupService.getAllGroups());
        return "groups";
    }

    @GetMapping("/{id}")
    public String getGroupById(@PathVariable Long id, Model model) {
        model.addAttribute("group", groupService.getGroupById(id));
        return "group";
    }

    @GetMapping("/create")
    public String showCreateGroupPage(Model model) {
        model.addAttribute("group", new GroupDto());
        return "create-group";
    }

    @PostMapping("/create")
    public String createGroup(@ModelAttribute GroupDto groupDto) {
        groupService.createGroup(groupDto);
        return "redirect:/groups";
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