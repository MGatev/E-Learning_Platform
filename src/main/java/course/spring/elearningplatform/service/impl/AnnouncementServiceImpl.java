package course.spring.elearningplatform.service.impl;

import course.spring.elearningplatform.dto.AnnouncementDto;
import course.spring.elearningplatform.dto.mapper.AnnouncementDtoToAnnouncementMapper;
import course.spring.elearningplatform.entity.Announcement;
import course.spring.elearningplatform.exception.MaximumAnnouncementsException;
import course.spring.elearningplatform.repository.AnnouncementRepository;
import course.spring.elearningplatform.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {

  private static final int MAX_ANNOUNCEMENTS = 5;

  private final AnnouncementRepository announcementRepository;

  @Autowired
  public AnnouncementServiceImpl(AnnouncementRepository announcementRepository) {
    this.announcementRepository = announcementRepository;
  }


  @Override
  public List<String> getAllActiveAnnouncements() {
    List<Announcement> allAnnouncements = announcementRepository.findAll();
    List<Announcement> activeAnnouncements = allAnnouncements.stream()
            .filter(Announcement::isActive)
            .toList();

    allAnnouncements.removeAll(activeAnnouncements);
    announcementRepository.deleteAll(allAnnouncements);

    return activeAnnouncements.stream().map(announcement ->
            String.format("%s: %s! Available until: %s!", announcement.getTitle(), announcement.getContent(), announcement.getExpiresAt().toString()))
            .toList();
  }

  @Override
  public Announcement getAnnouncementById(Long id) {
    return null;
  }

  @Override
  public Announcement addAnnouncement(AnnouncementDto announcement) {
    List<Announcement> allAnnouncements = announcementRepository.findAll();
    List<Announcement> activeAnnouncements = allAnnouncements.stream()
            .filter(Announcement::isActive)
            .toList();

    allAnnouncements.removeAll(activeAnnouncements);
    announcementRepository.deleteAll(allAnnouncements);

    if (activeAnnouncements.size() >= MAX_ANNOUNCEMENTS) {
      throw new MaximumAnnouncementsException("Maximum announcements reached! You can add up to 5 announcements!");
    }

    Announcement announcementToAdd = AnnouncementDtoToAnnouncementMapper.mapArticleDtoToArticle(announcement);
    return announcementRepository.save(announcementToAdd);
  }
}
