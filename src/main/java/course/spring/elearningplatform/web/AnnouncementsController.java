package course.spring.elearningplatform.web;

import course.spring.elearningplatform.dto.AnnouncementDto;
import course.spring.elearningplatform.exception.MaximumAnnouncementsException;
import course.spring.elearningplatform.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/announcements")
public class AnnouncementsController {
    private final AnnouncementService announcementService;

    @Autowired
    public AnnouncementsController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @GetMapping
    public ResponseEntity<List<String>> getAllActiveAnnouncements() {
        return ResponseEntity.ok(announcementService.getAllActiveAnnouncementsAsStrings());
    }

    @GetMapping("/add-announcement")
    public String getAnnouncementForm(Model model) {
        model.addAttribute("announcement", new AnnouncementDto());
        model.addAttribute("requestURI", "/admin/announcements");
        return "add-announcement";
    }

    @PostMapping("/add-announcement")
    public String addAnnouncement(AnnouncementDto announcement) {
        announcementService.addAnnouncement(announcement);
        return "redirect:/admin/announcements";
    }

    @PostMapping("/delete-announcement/{id}")
    public String deleteAnnouncement(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        announcementService.deleteAnnouncement(id);
        redirectAttributes.addFlashAttribute("message", "Announcement successfully deleted!");
        return "redirect:/admin/announcements";
    }

    @ExceptionHandler(MaximumAnnouncementsException.class)
    public String handleMaximumAnnouncementsException(MaximumAnnouncementsException e, RedirectAttributes attributes) {
        attributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/announcements/add-announcement";
    }
}
