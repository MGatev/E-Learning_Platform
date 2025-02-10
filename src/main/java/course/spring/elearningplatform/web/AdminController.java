package course.spring.elearningplatform.web;

import course.spring.elearningplatform.dto.UserDto;
import course.spring.elearningplatform.entity.Article;
import course.spring.elearningplatform.entity.Role;
import course.spring.elearningplatform.entity.User;
import course.spring.elearningplatform.exception.DuplicateEmailException;
import course.spring.elearningplatform.exception.DuplicateUsernameException;
import course.spring.elearningplatform.service.ArticleService;
import course.spring.elearningplatform.service.GroupService;
import course.spring.elearningplatform.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    private final UserService userService;
    private final GroupService groupService;
    private final ArticleService articleService;

    @Autowired
    public AdminController(UserService userService, GroupService groupService, ArticleService articleService) {
        this.userService = userService;
        this.groupService = groupService;
        this.articleService = articleService;
    }

    @GetMapping
    public String showAdminPage(@AuthenticationPrincipal UserDetails userDetails, Model model, HttpServletRequest request) {
        List<String> roles = Arrays.stream(Role.values())
                .map(Role::getDescription)
                .filter(description -> !description.equals(Role.UNREGISTERED.getDescription()))
                .toList();

        model.addAttribute("userRoles", roles);
        model.addAttribute("admin", userService.getUserByUsername(userDetails.getUsername()));
        model.addAttribute("user", new UserDto());
        model.addAttribute("requestURI", request.getRequestURI());

        return "redirect:/admin/users";
    }

    @GetMapping("/register")
    public String showRegisterPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<String> roles = Arrays.stream(Role.values())
                .map(Role::getDescription)
                .filter(description -> !description.equals(Role.UNREGISTERED.getDescription()))
                .toList();
        model.addAttribute("userRoles", roles);
        model.addAttribute("admin", userService.getUserByUsername(userDetails.getUsername()));
        model.addAttribute("user", new UserDto());
        return "admin-registration";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, Model model) {
        model.addAttribute("requestURI", "/admin/users");

        List<String> roles = Arrays.stream(Role.values())
                .map(Role::getDescription)
                .filter(description -> !description.equals(Role.UNREGISTERED.getDescription()))
                .toList();
        model.addAttribute("userRoles", roles);

        if (result.hasErrors()) {
            return "admin-registration";
        }

        try {
            userService.createUser(userDto);
        } catch (DuplicateUsernameException | DuplicateEmailException ex) {
            model.addAttribute("error", ex.getMessage());
            return "admin-registration";
        }

        return "redirect:/admin?success";
    }

    @GetMapping("/users")
    public String showUsersPage(Model model,
                                @RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "10") int size,
                                @RequestParam(value = "search", required = false) String searchQuery,
                                @AuthenticationPrincipal UserDetails userDetails,
                                HttpServletRequest request) {
        String loggedInUsername = userDetails.getUsername();
        Page<User> userPage;

        if (searchQuery != null && !searchQuery.isEmpty()) {
            userPage = userService.searchUsers(searchQuery, page, size, loggedInUsername);
        } else {
            userPage = userService.getAllUsers(page, size, loggedInUsername);
        }


        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", userPage.getNumber() + 1);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("requestURI", request.getRequestURI());
        model.addAttribute("searchQuery", searchQuery);
        model.addAttribute("startIndex", (page - 1) * size);

        List<Integer> pages = IntStream.rangeClosed(1, userPage.getTotalPages())
                .boxed()
                .toList();
        model.addAttribute("pages", pages);

        if (searchQuery != null && !searchQuery.isEmpty()) {
            model.addAttribute("searchParam", "&search=" + searchQuery);
        } else {
            model.addAttribute("searchParam", "");
        }

        return "admin-users";
    }

    @GetMapping("/studentGroups")
    public String showStudentGroupsPage(@AuthenticationPrincipal UserDetails userDetails, Model model, HttpServletRequest request) {
        model.addAttribute("requestURI", request.getRequestURI());
        model.addAttribute("groups", groupService.getAllGroups());
        return "admin-student-groups";
    }

    @GetMapping("/studentGroups/{id}")
    public String showStudentGroup(@PathVariable Long id, Model model) {
        model.addAttribute("group", groupService.getGroupById(id));
        List<Article> articles = groupService.getGroupById(id).getArticles();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy h:mm a");
        Map<Article, String> articleDateMap = new HashMap<>();

        for (Article article : articles) {
            String formattedDate = article.getCreatedAt().format(formatter);
            articleDateMap.put(article, formattedDate);
        }

        model.addAttribute("requestURI", "/admin/studentGroups");
        model.addAttribute("articleDateMap", articleDateMap);
        List<String> members = new java.util.ArrayList<>(groupService.getGroupById(id).getMembers().stream().map(User::getUsername).toList());
        members.add("deletedUser");
        model.addAttribute("allUsers", userService.getAllUsersExcept(members));
        return "admin-student-group";
    }

    @PostMapping("/studentGroups/{id}")
    public String deleteStudentGroup(@PathVariable Long id) {
        groupService.deleteGroup(id);
        return "redirect:/admin/studentGroups";
    }

    @PostMapping("/studentGroups/{groupId}/removeMember")
    public String removeUserFromGroup(@PathVariable Long groupId, @RequestParam Long userId, RedirectAttributes redirectAttributes) {
        groupService.removeMember(groupId, userService.getUserById(userId));
        redirectAttributes.addFlashAttribute("message",
                String.format("User %s removed successfully!", userService.getUserById(userId).getUsername()));
        return "redirect:/admin/studentGroups/" + groupId;
    }

    @PostMapping("/studentGroups/{groupId}/addMember")
    public String addUserToGroup(@PathVariable Long groupId, @RequestParam Long userId, RedirectAttributes redirectAttributes) {
        String username = userService.getUserById(userId).getUsername();
        groupService.addMember(groupId, username);
        redirectAttributes.addFlashAttribute("message", String.format("User %s successfully added to the group!", username));
        return "redirect:/admin/studentGroups/" + groupId;
    }

    @PostMapping("/articles/{id}/delete")
    public String deleteArticle(@PathVariable Long id, @RequestParam Long groupId, RedirectAttributes redirectAttributes) {
        articleService.deleteArticleById(id);
        redirectAttributes.addFlashAttribute("message","Article successfully deleted!");
        return "redirect:/admin/studentGroups/" + groupId;
    }
}
