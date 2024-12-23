package course.spring.elearningplatform.service.impl;

import course.spring.elearningplatform.dto.EventDto;
import course.spring.elearningplatform.dto.ImageDto;
import course.spring.elearningplatform.entity.Event;
import course.spring.elearningplatform.entity.Group;
import course.spring.elearningplatform.entity.Image;
import course.spring.elearningplatform.entity.User;
import course.spring.elearningplatform.exception.DuplicatedEntityException;
import course.spring.elearningplatform.repository.EventRepository;
import course.spring.elearningplatform.service.EventService;
import course.spring.elearningplatform.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
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
        event.setImagePath(eventDto.getImagePath());

        return event;
    }


}
