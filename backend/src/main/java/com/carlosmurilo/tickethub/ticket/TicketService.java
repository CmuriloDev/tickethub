package com.carlosmurilo.tickethub.ticket;

import com.carlosmurilo.tickethub.exception.ResourceNotFoundException;
import com.carlosmurilo.tickethub.ticket.dto.CreateTicketRequest;
import com.carlosmurilo.tickethub.ticket.dto.TicketResponse;
import com.carlosmurilo.tickethub.ticket.dto.UpdateTicketRequest;
import com.carlosmurilo.tickethub.user.User;
import com.carlosmurilo.tickethub.user.UserRepository;
import com.carlosmurilo.tickethub.user.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public TicketService(TicketRepository ticketRepository,  UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    public TicketResponse create(User user, CreateTicketRequest req) {
        Ticket ticket = new Ticket();
        ticket.setCreatedBy(user);
        ticket.setTitle(req.getTitle().trim());
        ticket.setDescription(req.getDescription());
        ticket.setStatus(req.getStatus() != null ? req.getStatus() : TicketStatus.OPEN);
        ticket.setPriority(req.getPriority() != null ? req.getPriority() : TicketPriority.MEDIUM);

        return toResponse(ticketRepository.save(ticket));
    }

    public Page<TicketResponse> list(User user, TicketStatus status, TicketPriority priority, Pageable pageable) {
        boolean isAdmin = user.getRole() == UserRole.ADMIN;

        Page<Ticket> page;

        if (isAdmin) {
            if (status != null && priority != null)
                page = ticketRepository.findAllByStatusAndPriority(status, priority, pageable);
            else if (status != null)
                page = ticketRepository.findAllByStatus(status, pageable);
            else if (priority != null)
                page = ticketRepository.findAllByPriority(priority, pageable);
            else
                page = ticketRepository.findAll(pageable);
        } else {
            if (status != null && priority != null)
                page = ticketRepository.findAllByCreatedByAndStatusAndPriority(user, status, priority, pageable);
            else if (status != null)
                page = ticketRepository.findAllByCreatedByAndStatus(user, status, pageable);
            else if (priority != null)
                page = ticketRepository.findAllByCreatedByAndPriority(user, priority, pageable);
            else
                page = ticketRepository.findAllByCreatedBy(user, pageable);
        }

        return page.map(this::toResponse);
    }

    public TicketResponse getById(User user, UUID ticketId) {
        Ticket ticket = findTicketWithAccess(user, ticketId);
        return toResponse(ticket);
    }

    public TicketResponse update(User user, UUID ticketId, UpdateTicketRequest req) {
        Ticket ticket = findTicketWithAccess(user, ticketId);

        ticket.setTitle(req.getTitle().trim());
        ticket.setDescription(req.getDescription());
        ticket.setStatus(req.getStatus());
        ticket.setPriority(req.getPriority());

        return toResponse(ticketRepository.save(ticket));
    }

    public void delete(User user, UUID ticketId) {
        Ticket ticket = findTicketWithAccess(user, ticketId);
        ticketRepository.delete(ticket);
    }

    public Map<TicketStatus, Long> getStats(User user) {
        boolean isAdmin = user.getRole() == UserRole.ADMIN;

        List<Object[]> results = isAdmin
                ? ticketRepository.countAllTicketsByStatus()
                : ticketRepository.countTicketsByStatusAndUser(user);

        Map<TicketStatus, Long> stats = new EnumMap<>(TicketStatus.class);
        for (TicketStatus s : TicketStatus.values()) stats.put(s, 0L);
        for (Object[] row : results) stats.put((TicketStatus) row[0], (Long) row[1]);

        return stats;
    }

    public Page<TicketResponse> listAll(TicketStatus status, TicketPriority priority, Pageable pageable) {
        Page<Ticket> page;
        if (status != null && priority != null)
            page = ticketRepository.findAllByStatusAndPriority(status, priority, pageable);
        else if (status != null)
            page = ticketRepository.findAllByStatus(status, pageable);
        else if (priority != null)
            page = ticketRepository.findAllByPriority(priority, pageable);
        else
            page = ticketRepository.findAll(pageable);

        return page.map(this::toResponse);
    }

    public TicketResponse assignTicket(UUID ticketId, UUID adminId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));

        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        ticket.setAssignedTo(admin);
        return toResponse(ticketRepository.save(ticket));
    }

    // --- helpers ---

    private Ticket findTicketWithAccess(User user, UUID ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));

        if (user.getRole() != UserRole.ADMIN && !ticket.getCreatedBy().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Ticket not found");
        }

        return ticket;
    }

    private TicketResponse toResponse(Ticket t) {
        return new TicketResponse(
                t.getId(),
                t.getCreatedBy().getId(),
                t.getTitle(),
                t.getDescription(),
                t.getStatus(),
                t.getPriority(),
                t.getCreatedAt(),
                t.getUpdatedAt()
        );
    }
}