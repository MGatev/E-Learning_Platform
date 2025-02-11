package course.spring.elearningplatform.web;

import course.spring.elearningplatform.dto.FAQDto;
import course.spring.elearningplatform.service.FAQService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FAQController {

    private final FAQService faqService;

    public FAQController(FAQService faqService) {
        this.faqService = faqService;
    }

    @GetMapping("/help")
    public String getAllQuestions(Model model) {
        model.addAttribute("faqs", faqService.getAllQuestions());
        return "faq";
    }

    @PostMapping("/help")
    public String createQuestion(@ModelAttribute FAQDto faqDto) {
        faqService.addQuestion(faqDto);
        return "redirect:/admin/faq";
    }

    @PostMapping("/help/{id}")
    public String deleteQuestion(@PathVariable("id") Long id, Model model) {
        faqService.deleteQuestion(id);
        model.addAttribute("articles", faqService.getAllQuestions());
        return "redirect:/admin/faq";
    }
}
