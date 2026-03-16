package com.carlosmurilo.tickethub.ticket;

import com.carlosmurilo.tickethub.exception.ResourceNotFoundException;
import com.carlosmurilo.tickethub.ticket.dto.CreateTicketRequest;
import com.carlosmurilo.tickethub.ticket.dto.TicketResponse;
import com.carlosmurilo.tickethub.ticket.dto.UpdateTicketRequest;
import com.carlosmurilo.tickethub.user.User;
import com.carlosmurilo.tickethub.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public TicketService(TicketRepository ticketRepository, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    public TicketResponse create(UUID userId, CreateTicketRequest req) {
        User user = getUserOrThrow(userId);

        Ticket ticket = new Ticket();
        ticket.setId(UUID.randomUUID());
        ticket.setUser(user);
        ticket.setTitle(req.getTitle().trim());
        ticket.setDescription(req.getDescription());
        ticket.setStatus(req.getStatus());
        ticket.setPriority(req.getPriority());
        ticket.setCreatedAt(Instant.now());
        ticket.setUpdatedAt(Instant.now());

        Ticket saved = ticketRepository.save(ticket);
        return toResponse(saved);
    }

    public Page<TicketResponse> list(UUID userId, TicketStatus status, TicketPriority priority, Pageable pageable) {
        User user = getUserOrThrow(userId);

        Page<Ticket> page;
        if (status != null && priority != null) {
            page = ticketRepository.findAllByUserAndStatusAndPriority(user, status, priority, pageable);
        } else if (status != null) {
            page = ticketRepository.findAllByUserAndStatus(user, status, pageable);
        } else if (priority != null) {
            page = ticketRepository.findAllByUserAndPriority(user, priority, pageable);
        } else {
            page = ticketRepository.findAllByUser(user, pageable);
        }

        return page.map(this::toResponse);
    }

    public TicketResponse getById(UUID userId, UUID ticketId) {
        User user = getUserOrThrow(userId);
        Ticket ticket = ticketRepository.findByIdAndUser(ticketId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        return toResponse(ticket);
    }

    public TicketResponse update(UUID userId, UUID ticketId, UpdateTicketRequest req) {
        User user = getUserOrThrow(userId);
        Ticket ticket = ticketRepository.findByIdAndUser(ticketId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));

        ticket.setTitle(req.getTitle().trim());
        ticket.setDescription(req.getDescription());
        ticket.setStatus(req.getStatus());
        ticket.setPriority(req.getPriority());
        ticket.setUpdatedAt(Instant.now());

        Ticket saved = ticketRepository.save(ticket);
        return toResponse(saved);
    }

    public void delete(UUID userId, UUID ticketId) {
        User user = getUserOrThrow(userId);
        Ticket ticket = ticketRepository.findByIdAndUser(ticketId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        ticketRepository.delete(ticket);
    }

    private User getUserOrThrow(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private TicketResponse toResponse(Ticket t) {
        return new TicketResponse(
                t.getId(),
                t.getTitle(),
                t.getDescription(),
                t.getStatus(),
                t.getPriority(),
                t.getCreatedAt(),
                t.getUpdatedAt()
        );
    }
}