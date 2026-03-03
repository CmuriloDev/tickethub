package com.carlosmurilo.tickethub.ticket;

import com.carlosmurilo.tickethub.ticket.dto.CreateTicketRequest;
import com.carlosmurilo.tickethub.ticket.dto.TicketResponse;
import com.carlosmurilo.tickethub.ticket.dto.UpdateTicketRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
            @RequestHeader("X-User-Id") UUID userId,
            @Valid @RequestBody CreateTicketRequest request
    ) {
        return ticketService.create(userId, request);
    }

    @GetMapping
    public Page<TicketResponse> list(
            @RequestHeader("X-User-Id") UUID userId,
            @RequestParam(required = false) TicketStatus status,
            @RequestParam(required = false) TicketPriority priority,
            Pageable pageable
    ) {
        return ticketService.list(userId, status, priority, pageable);
    }

    @GetMapping("/{id}")
    public TicketResponse getById(
            @RequestHeader("X-User-Id") UUID userId,
            @PathVariable UUID id
    ) {
        return ticketService.getById(userId, id);
    }

    @PutMapping("/{id}")
    public TicketResponse update(
            @RequestHeader("X-User-Id") UUID userId,
            @PathVariable UUID id,
            @Valid @RequestBody UpdateTicketRequest request
    ) {
        return ticketService.update(userId, id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @RequestHeader("X-User-Id") UUID userId,
            @PathVariable UUID id
    ) {
        ticketService.delete(userId, id);
    }
}