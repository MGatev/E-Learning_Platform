package course.spring.elearningplatform.service;

import course.spring.elearningplatform.dto.AnnouncementDto;
import course.spring.elearningplatform.entity.Announcement;

import java.util.List;

public interface AnnouncementService {
  List<String> getAllActiveAnnouncements();
  Announcement getAnnouncementById(Long id);
  Announcement addAnnouncement(AnnouncementDto announcement);
}
