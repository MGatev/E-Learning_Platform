package course.spring.elearningplatform.service.impl;

import course.spring.elearningplatform.dto.TicketDto;
import course.spring.elearningplatform.entity.Course;
import course.spring.elearningplatform.entity.Ticket;
import course.spring.elearningplatform.entity.User;
import course.spring.elearningplatform.exception.EntityNotFoundException;
import course.spring.elearningplatform.repository.TicketRepository;
import course.spring.elearningplatform.service.CourseService;
import course.spring.elearningplatform.service.TicketService;
import course.spring.elearningplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {

  private final TicketRepository ticketRepository;
  private final CourseService courseService;
  private final UserService userService;

  @Autowired
  public TicketServiceImpl(TicketRepository ticketRepository, CourseService courseService, UserService userService) {
    this.ticketRepository = ticketRepository;
    this.courseService = courseService;
    this.userService = userService;
  }

  @Override
  public List<Ticket> getAllTickets() {
    return ticketRepository.findAll();
  }

  @Override
  public Ticket saveTicket(TicketDto ticketDto, Long courseId, String issuerName) {
    Course courseForTicket = courseService.getCourseById(courseId);
    User issuer = userService.getUserByUsername(issuerName);
    Ticket ticketForSave = buildTicket(ticketDto, courseForTicket, issuer);
    Ticket savedTicket = ticketRepository.save(ticketForSave);
    courseForTicket.addTicket(savedTicket);
    issuer.addTicket(savedTicket);
    userService.save(issuer);
    courseService.save(courseForTicket);

    return savedTicket;
  }

  @Override
  public Ticket resolveTicket(Long ticketId) {
    Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new EntityNotFoundException("Ticket", "/courses"));
    ticket.setResolved(true);
    return ticketRepository.save(ticket);
  }

  private Ticket buildTicket(TicketDto ticketDto, Course courseForTicket, User issuer) {
    Ticket ticket = new Ticket();
    ticket.setContent(ticketDto.getContent());
    ticket.setForCourse(courseForTicket);
    ticket.setIssuer(issuer);
    ticket.setResolved(false);
    ticket.setCreatedOn(Date.from(Instant.now()));
    return ticket;
  }
}
