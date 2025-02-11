package course.spring.elearningplatform.service;

import course.spring.elearningplatform.dto.EventDto;
import course.spring.elearningplatform.entity.Event;

import java.util.List;
import java.util.Optional;

public interface EventService {

    List<Event> getAllEvents();

    Optional<Event> getEventById(Long id);

    List<Event> getAllEventsByUser(String username);

    Event saveEvent(EventDto event);

    void deleteEvent(Long id);
}
