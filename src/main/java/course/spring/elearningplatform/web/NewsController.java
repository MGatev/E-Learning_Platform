package course.spring.elearningplatform.web;

import course.spring.elearningplatform.entity.CustomUserDetails;
import course.spring.elearningplatform.entity.News;
import course.spring.elearningplatform.repository.NewsRepository;
import course.spring.elearningplatform.service.ActivityLogService;
import course.spring.elearningplatform.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/news")
public class NewsController {

    private final NewsRepository newsRepository;
    private final NewsService newsService;
    private final ActivityLogService activityLogService;

    @Autowired
    public NewsController(NewsService newsService, NewsRepository newsRepository, ActivityLogService activityLogService) {
        this.newsService = newsService;
        this.newsRepository = newsRepository;
        this.activityLogService = activityLogService;
    }

    @GetMapping
    public String getAllNews(Model model) {
        List<News> newsList = newsRepository.findAll();
        model.addAttribute("newsList", newsList);
        return "news";
    }

    @GetMapping("/add")
    public String addNewsForm(Model model) {
        model.addAttribute("news", new News());
        model.addAttribute("redirectURI", "/admin/news");
        return "news-form";
    }

    @PostMapping("/add")
    public String addNews(@ModelAttribute News news) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        news.setPublishedDate(LocalDateTime.parse(LocalDateTime.now().format(formatter)));
        newsRepository.save(news);
        activityLogService.logActivity("News added", news.getAuthor());
        return "redirect:/admin/news";
    }

    @GetMapping("/delete")
    public String showDeleteNewsPage(Model model) {
        List<News> newsList = newsService.getAllNews(); // Fetch all news
        model.addAttribute("newsList", newsList); // Add to model to show in the dropdown
        return "delete-news"; // The Thymeleaf template to show news list
    }

    @PostMapping("/delete/selected")
    public String deleteSelectedNews(@RequestParam("newsId") Long newsId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        newsService.deleteNews(newsId); // Call the service to delete the news
        activityLogService.logActivity("News deleted", customUserDetails.getUsername());
        return "redirect:/admin/news"; // Redirect to the news list after deletion
    }

    @GetMapping("/{id}")
    public String showNewsDetail(@PathVariable("id") Long id, Model model) {
        News news = newsService.getNewsById(id);
        model.addAttribute("news", news);
        return "news-detail"; // The page to display a single news item
    }

}