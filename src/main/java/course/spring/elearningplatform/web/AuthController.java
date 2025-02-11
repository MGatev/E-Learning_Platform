package course.spring.elearningplatform.web;

import course.spring.elearningplatform.dto.UserDto;
import course.spring.elearningplatform.exception.DuplicateEmailException;
import course.spring.elearningplatform.exception.DuplicateUsernameException;
import course.spring.elearningplatform.service.ActivityLogService;
import course.spring.elearningplatform.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {
    private final UserService userService;
    private final ActivityLogService activityLogService;

    @Autowired
    public AuthController(UserService userService, ActivityLogService activityLogService) {
        this.userService = userService;
        this.activityLogService = activityLogService;
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new UserDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            return "register";
        }

        try {
            userService.createUser(userDto);
            activityLogService.logActivity("New user registered", userDto.getUsername());
        } catch (DuplicateUsernameException | DuplicateEmailException ex) {
            model.addAttribute("error", ex.getMessage());
            return "register";
        }

        return "redirect:/login?success";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }
}
