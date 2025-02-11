package course.spring.elearningplatform.web;

import course.spring.elearningplatform.entity.Role;
import course.spring.elearningplatform.dto.ImageDto;
import course.spring.elearningplatform.entity.User;
import course.spring.elearningplatform.service.ActivityLogService;
import course.spring.elearningplatform.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {

    private final UserService userService;
    private final ActivityLogService activityLogService;

    @Autowired
    public UserController(UserService userService, ActivityLogService activityLogService) {
        this.userService = userService;
        this.activityLogService = activityLogService;
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

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserDetails userDetails) {
        redirectAttributes.addFlashAttribute("deletedUserFullName", userService.getUserById(id).getFullName());
        userService.deleteUser(userService.getUserById(id));
        activityLogService.logActivity("User deleted", userDetails.getUsername());
        return "redirect:/admin/users";
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String showUser(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("requestURI", "/admin/users");
        return "user";
    }

    @GetMapping("/users/edit/{id}")
    public String showEditUser(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("allRoles", Arrays.stream(Role.values()).filter(role -> role != Role.UNREGISTERED).toList());
        return "edit-user";
    }

    @PostMapping("/users/edit/{id}")
    public String editUser(@PathVariable Long id, @Valid @ModelAttribute("user") User user, BindingResult result) {
        if (result.hasErrors()) {
            return "edit-user";
        }

        userService.updateUser(user);
        activityLogService.logActivity("User updated", user.getUsername());
        return "redirect:/admin/users";
    }

    @GetMapping("/users/{id}/certificates")
    public String showUserCertificates(@PathVariable("id") long userId, Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User loggedInUser = userService.getUserByUsername(username);
        model.addAttribute("loggedInUser", loggedInUser);
        return "certificates";
    }

    @PostMapping("/users/update-role")
    public ResponseEntity<String> updateUserRole(@RequestBody Map<String, String> payload) {
        Long id = Long.parseLong(payload.get("id"));
        String newRole = payload.get("role");
        userService.updateUserDetails(id, "role", newRole);
        return ResponseEntity.ok("Role updated successfully");
    }

    @PostMapping("/users/update-full-name")
    public ResponseEntity<String> updateUserFullName(@RequestBody Map<String, String> payload) {
        Long id = Long.parseLong(payload.get("id"));
        String newName = payload.get("full-name");
        userService.updateUserDetails(id, "name", newName);
        return ResponseEntity.ok("Full name updated successfully");
    }

    @PostMapping("/users/update-email")
    public ResponseEntity<String> updateUserEmail(@RequestBody Map<String, String> payload) {
        Long id = Long.parseLong(payload.get("id"));
        String newEmail = payload.get("email");
        userService.updateUserDetails(id, "email", newEmail);
        return ResponseEntity.ok("Email updated successfully");
    }

    @PostMapping("/users/update-username")
    public ResponseEntity<String> updateUsername(@RequestBody Map<String, String> payload) {
        Long id = Long.parseLong(payload.get("id"));
        String newUsername = payload.get("username");
        userService.updateUserDetails(id, "username", newUsername);
        return ResponseEntity.ok("Username updated successfully");
    }

    @PostMapping("/users/update-profile-picture")
    public ResponseEntity<String> updateProfilePicture(@RequestParam("profilePicture") MultipartFile file,
                                                       @RequestParam("id") Long userId) {
        ImageDto imageDto = new ImageDto(file);
        userService.updateUserDetails(userId, "profilePicture", imageDto);
        return ResponseEntity.ok("Profile picture updated successfully");
    }
}
