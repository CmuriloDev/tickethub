package com.carlosmurilo.tickethub.ticket;

import com.carlosmurilo.tickethub.ticket.dto.CreateTicketRequest;
import com.carlosmurilo.tickethub.ticket.dto.TicketResponse;
import com.carlosmurilo.tickethub.ticket.dto.UpdateTicketRequest;
import com.carlosmurilo.tickethub.user.User;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TicketResponse create(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreateTicketRequest request
    ) {
        return ticketService.create(user, request);
    }

    @GetMapping
    public Page<TicketResponse> list(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) TicketStatus status,
            @RequestParam(required = false) TicketPriority priority,
            Pageable pageable
    ) {
        return ticketService.list(user, status, priority, pageable);
    }

    @GetMapping("/{id}")
    public TicketResponse getById(
            @AuthenticationPrincipal User user,
            @PathVariable UUID id
    ) {
        return ticketService.getById(user, id);
    }

    @PutMapping("/{id}")
    public TicketResponse update(
            @AuthenticationPrincipal User user,
            @PathVariable UUID id,
            @Valid @RequestBody UpdateTicketRequest request
    ) {
        return ticketService.update(user, id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @AuthenticationPrincipal User user,
            @PathVariable UUID id
    ) {
        ticketService.delete(user, id);
    }

    @GetMapping("/stats")
    public Map<TicketStatus, Long> stats(
            @AuthenticationPrincipal User user
    ) {
        return ticketService.getStats(user);
    }
}