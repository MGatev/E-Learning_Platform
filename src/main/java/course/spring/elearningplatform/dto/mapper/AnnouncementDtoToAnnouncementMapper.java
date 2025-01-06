package course.spring.elearningplatform.dto.mapper;

import course.spring.elearningplatform.dto.AnnouncementDto;
import course.spring.elearningplatform.entity.Announcement;

import java.time.LocalDateTime;

public class AnnouncementDtoToAnnouncementMapper {
  public static Announcement mapArticleDtoToArticle(AnnouncementDto announcementDto) {
    Announcement announcement = new Announcement();
    announcement.setTitle(announcementDto.getTitle());
    announcement.setContent(announcementDto.getContent());
    announcement.setCreatedAt(LocalDateTime.now());
    announcement.setExpiresAt(announcementDto.getExpiresAt());
    return announcement;
  }
}
