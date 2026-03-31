package com.carlosmurilo.tickethub.admin;

import com.carlosmurilo.tickethub.ticket.TicketPriority;
import com.carlosmurilo.tickethub.ticket.TicketService;
import com.carlosmurilo.tickethub.ticket.TicketStatus;
import com.carlosmurilo.tickethub.ticket.dto.AssignTicketRequest;
import com.carlosmurilo.tickethub.ticket.dto.TicketResponse;
import com.carlosmurilo.tickethub.user.UserService;
import com.carlosmurilo.tickethub.user.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final TicketService ticketService;

    public AdminController(UserService userService, TicketService ticketService) {
        this.userService = userService;
        this.ticketService = ticketService;
    }

    @GetMapping("/users")
    public Page<UserResponse> listUsers(Pageable pageable) {
        return userService.listAll(pageable);
    }

    @GetMapping("/tickets")
    public Page<TicketResponse> listTickets(
            @RequestParam(required = false) TicketStatus status,
            @RequestParam(required = false) TicketPriority priority,
            Pageable pageable
    ) {
        return ticketService.listAll(status, priority, pageable);
    }

    @PatchMapping("/tickets/{id}/assign")
    public TicketResponse assignTicket(
            @PathVariable UUID id,
            @Valid @RequestBody AssignTicketRequest request
    ) {
        return ticketService.assignTicket(id, request.getAssignedToId());
    }
}