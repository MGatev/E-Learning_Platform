package course.spring.elearningplatform.web;

import course.spring.elearningplatform.dto.GroupDto;
import course.spring.elearningplatform.entity.Group;
import course.spring.elearningplatform.entity.User;
import course.spring.elearningplatform.exception.DuplicatedEntityException;
import course.spring.elearningplatform.service.ArticleService;
import course.spring.elearningplatform.service.GroupService;
import course.spring.elearningplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/groups")
public class GroupController {
    private final GroupService groupService;
    private final ArticleService articleService;
    private final UserService userService;

    @Autowired
    public GroupController(GroupService groupService, ArticleService articleService, UserService userService) {
        this.groupService = groupService;
        this.articleService = articleService;
        this.userService = userService;
    }

    @GetMapping
    public String getAllGroups(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User loggedUser = userService.getUserByUsername(userDetails.getUsername());
        model.addAttribute("groups", groupService.getAllGroups());
        model.addAttribute("loggedUser", loggedUser);
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
    public String createGroup(@ModelAttribute GroupDto groupDto, @AuthenticationPrincipal UserDetails userDetails) {
        Group createdGroup = groupService.createGroup(groupDto);
        return joinGroup(createdGroup.getId(), userDetails);
    }

    @PostMapping("/{id}/join")
    public String joinGroup(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        User loggedUser = userService.getUserByUsername(userDetails.getUsername());
        groupService.addMember(id, loggedUser);
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