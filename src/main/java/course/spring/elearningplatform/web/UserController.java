package course.spring.elearningplatform.web;

import course.spring.elearningplatform.entity.User;
import course.spring.elearningplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String showProfile(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User loggedInUser = userService.getUserByUsername(username);
        model.addAttribute("loggedInUser", loggedInUser);
        return "profile";
    }

    @GetMapping("/users/search")
    public ResponseEntity<List<String>> searchUsers(
            @RequestParam("query") String query,
            @RequestParam("loggedInUsername") String loggedInUsername) {

        List<String> allUsernames = userService.getAllUsers().stream()
                .map(User::getUsername)
                .filter(username -> !username.equals(loggedInUsername))
                .filter(username -> username.toLowerCase().contains(query.toLowerCase()))
                .toList();

        return ResponseEntity.ok(allUsernames);
    }
}
