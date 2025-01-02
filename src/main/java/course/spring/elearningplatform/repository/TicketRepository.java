package course.spring.elearningplatform.repository;

import course.spring.elearningplatform.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
