package course.spring.elearningplatform.web;

import course.spring.elearningplatform.dto.TicketDto;
import course.spring.elearningplatform.entity.CustomUserDetails;
import course.spring.elearningplatform.entity.Ticket;
import course.spring.elearningplatform.entity.User;
import course.spring.elearningplatform.service.ActivityLogService;
import course.spring.elearningplatform.service.TicketService;
import course.spring.elearningplatform.service.UserService;
import course.spring.elearningplatform.service.impl.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("tickets")
public class TicketController {

  private final TicketService ticketService;
  private final UserService userService;
  private final ActivityLogService activityLogService;

  @Autowired
  public TicketController(TicketService ticketService, UserService userService, ActivityLogService activityLogService) {
    this.ticketService = ticketService;
    this.userService = userService;
      this.activityLogService = activityLogService;
  }

  @GetMapping
  public String showTicketsPage(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
    User user = userService.getUserByUsername(customUserDetails.getUsername());
    model.addAttribute("user", user);
    model.addAttribute("tickets", ticketService.getAllTickets());
    return "tickets";
  }

  @GetMapping("/open")
  public String openTicketForm(@RequestParam("courseId") Long courseId, Model model) {
    model.addAttribute("ticket", new TicketDto());
    model.addAttribute("courseId", courseId);
    return "ticket-form";
  }

  @PostMapping("/open")
  public String openTicket(@RequestParam("courseId") Long courseId, TicketDto ticketDto, @AuthenticationPrincipal UserDetails userDetails) {
    ticketService.saveTicket(ticketDto, courseId, userDetails.getUsername());
    activityLogService.logActivity("Ticket opened", userDetails.getUsername());
    return "redirect:/courses/" + courseId;
  }

  @PostMapping("/resolve")
  public String resolveTicket(@RequestParam Long courseId, @RequestParam Long ticketId, Model model, @AuthenticationPrincipal UserDetails userDetails) {
    Ticket resolvedTicket = ticketService.resolveTicket(ticketId);
    model.addAttribute("ticket", resolvedTicket);
    activityLogService.logActivity("Ticket resolved", userDetails.getUsername());
    return "redirect:/courses/" + courseId;
  }
}
