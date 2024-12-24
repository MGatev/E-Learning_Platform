package course.spring.elearningplatform.web;

import course.spring.elearningplatform.dto.UserDto;
import course.spring.elearningplatform.entity.Course;
import course.spring.elearningplatform.exception.DuplicateEmailException;
import course.spring.elearningplatform.exception.DuplicateUsernameException;
import course.spring.elearningplatform.service.CourseService;
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

import java.util.List;
import java.util.Map;

@Controller
public class AuthController {
    private final UserService userService;
    private final CourseService courseService;

    @Autowired
    public AuthController(UserService userService, CourseService courseService) {
        this.userService = userService;
        this.courseService = courseService;
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new UserDto());
        return "register";
    }

    /*@GetMapping("/home")
    public String home(Model model) {
        Map<String, List<Course>> coursesByCategory = courseService.getCoursesGroupedByCategory();
        model.addAttribute("coursesByCategory", coursesByCategory);
        return "home";
    }*/

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            return "register";
        }

        try {
            userService.createUser(userDto);
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
