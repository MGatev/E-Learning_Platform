package course.spring.elearningplatform.service;

import course.spring.elearningplatform.dto.AnnouncementDto;
import course.spring.elearningplatform.entity.Announcement;

import java.util.List;

public interface AnnouncementService {
  List<String> getAllActiveAnnouncementsAsStrings();
  List<Announcement> getAllActiveAnnouncements();
  Announcement addAnnouncement(AnnouncementDto announcement);
  void deleteAnnouncement(Long id);
}
