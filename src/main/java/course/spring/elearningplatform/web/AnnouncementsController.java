package course.spring.elearningplatform.web;

import course.spring.elearningplatform.dto.AnnouncementDto;
import course.spring.elearningplatform.entity.CustomUserDetails;
import course.spring.elearningplatform.exception.MaximumAnnouncementsException;
import course.spring.elearningplatform.service.ActivityLogService;
import course.spring.elearningplatform.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/announcements")
public class AnnouncementsController {
    private final AnnouncementService announcementService;
    private final ActivityLogService activityLogService;

    @Autowired
    public AnnouncementsController(AnnouncementService announcementService, ActivityLogService activityLogService) {
        this.announcementService = announcementService;
        this.activityLogService = activityLogService;
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
    public String addAnnouncement(AnnouncementDto announcement, @AuthenticationPrincipal CustomUserDetails userDetails) {
        announcementService.addAnnouncement(announcement);
        activityLogService.logActivity("New announcement added", userDetails.getUsername());
        return "redirect:/admin/announcements";
    }

    @PostMapping("/delete-announcement/{id}")
    public String deleteAnnouncement(@PathVariable Long id, RedirectAttributes redirectAttributes, @AuthenticationPrincipal CustomUserDetails userDetails) {
        announcementService.deleteAnnouncement(id);
        redirectAttributes.addFlashAttribute("message", "Announcement successfully deleted!");
        activityLogService.logActivity("Deleted announcement", userDetails.getUsername());
        return "redirect:/admin/announcements";
    }

    @ExceptionHandler(MaximumAnnouncementsException.class)
    public String handleMaximumAnnouncementsException(MaximumAnnouncementsException e, RedirectAttributes attributes) {
        attributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/announcements/add-announcement";
    }
}
