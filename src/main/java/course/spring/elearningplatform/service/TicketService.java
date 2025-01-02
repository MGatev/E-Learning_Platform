package course.spring.elearningplatform.service;

import course.spring.elearningplatform.dto.TicketDto;
import course.spring.elearningplatform.entity.Ticket;

import java.util.List;

public interface TicketService {

  List<Ticket> getAllTickets();
  Ticket saveTicket(TicketDto ticketDto, Long courseId, String issuerName);
  Ticket resolveTicket(Long ticketId);
}
