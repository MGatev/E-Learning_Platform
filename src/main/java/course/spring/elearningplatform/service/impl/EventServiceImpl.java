package course.spring.elearningplatform.service.impl;

import course.spring.elearningplatform.dto.EventDto;
import course.spring.elearningplatform.dto.ImageDto;
import course.spring.elearningplatform.entity.Event;
import course.spring.elearningplatform.entity.Image;
import course.spring.elearningplatform.exception.DuplicatedEntityException;
import course.spring.elearningplatform.repository.EventRepository;
import course.spring.elearningplatform.service.EventService;
import course.spring.elearningplatform.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final ImageService imageService;

    @Autowired
    EventServiceImpl(EventRepository eventRepository, ImageService imageService) {
        this.imageService = imageService;
        this.eventRepository = eventRepository;
    }

    @Transactional
    @Override
    public List<Event> getAllEvents() {
        List<Event> allEvents = eventRepository.findAll();
        return allEvents.stream().peek(group -> {
                    Image image = group.getImage();
                    if (image != null) {
                        group.setImageBase64(image.parseImage());
                    }
                })
                .toList();
    }

    @Transactional
    @Override
    public Optional<Event> getEventById(Long id) {
        Optional<Event> eventOptional = eventRepository.findById(id);
        eventOptional.ifPresent(event -> {
            Image image = event.getImage();
            if (image != null) {
                event.setImageBase64(image.parseImage());
            }
        });
        return eventOptional;
    }

    @Override
    public List<Event> getAllEventsByUser(String username) {
        return eventRepository.findByInstructor(username);
    }


    @Override
    public Event saveEvent(EventDto eventDto) {
        Event eventForCreate = buildEvent(eventDto);
        if (eventRepository.existsByTitle(eventForCreate.getTitle())) {
            throw new DuplicatedEntityException(String.format("Event with title %s already exists", eventForCreate.getTitle()));
        }
        return eventRepository.save(eventForCreate);
    }

    @Override
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    private Event buildEvent(EventDto eventDto) {
        Event event = new Event();

        event.setTitle(eventDto.getTitle());
        event.setDescription(eventDto.getDescription());
        event.setStartTime(eventDto.getStartTime());
        event.setEndTime(eventDto.getEndTime());
        event.setInstructor(eventDto.getInstructor());

        ImageDto imageDto = eventDto.getImage();
        if (imageDto != null) {
            Image savedImage = imageService.createImage(imageDto);
            event.setImage(savedImage);
        }

        return event;
    }


}
