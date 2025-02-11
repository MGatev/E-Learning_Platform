package course.spring.elearningplatform.web;

import course.spring.elearningplatform.entity.News;
import course.spring.elearningplatform.repository.NewsRepository;
import course.spring.elearningplatform.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public NewsController(NewsService newsService, NewsRepository newsRepository) {
        this.newsService = newsService;
        this.newsRepository = newsRepository;
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
        return "redirect:/admin/news";
    }

    @GetMapping("/delete")
    public String showDeleteNewsPage(Model model) {
        List<News> newsList = newsService.getAllNews(); // Fetch all news
        model.addAttribute("newsList", newsList); // Add to model to show in the dropdown
        return "delete-news"; // The Thymeleaf template to show news list
    }

    @PostMapping("/delete/selected")
    public String deleteSelectedNews(@RequestParam("newsId") Long newsId) {
        newsService.deleteNews(newsId); // Call the service to delete the news
        return "redirect:/admin/news"; // Redirect to the news list after deletion
    }

    @GetMapping("/{id}")
    public String showNewsDetail(@PathVariable("id") Long id, Model model) {
        News news = newsService.getNewsById(id);
        model.addAttribute("news", news);
        return "news-detail"; // The page to display a single news item
    }

}