package course.spring.elearningplatform.web;

import course.spring.elearningplatform.entity.User;
import course.spring.elearningplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String showProfile(Model model) {
        return "profile";
    }

    @GetMapping("/users/search")
    public ResponseEntity<List<String>> searchUsers(
            @RequestParam("query") String query,
            @RequestParam("loggedInUsername") String loggedInUsername) {

        List<String> allUsernames = userService.getAllUsers().stream()
                .map(User::getUsername)
                .filter(username -> !username.equals(loggedInUsername)) // Exclude logged-in user
                .filter(username -> username.toLowerCase().contains(query.toLowerCase()))
                .toList();

        return ResponseEntity.ok(allUsernames);
    }
}
