package com.carlosmurilo.tickethub.ticket;

import com.carlosmurilo.tickethub.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    Page<Ticket> findAllByCreatedBy(User user, Pageable pageable);
    Page<Ticket> findAllByCreatedByAndStatus(User user, TicketStatus status, Pageable pageable);
    Page<Ticket> findAllByCreatedByAndPriority(User user, TicketPriority priority, Pageable pageable);
    Page<Ticket> findAllByCreatedByAndStatusAndPriority(User user, TicketStatus status, TicketPriority priority, Pageable pageable);

    Page<Ticket> findAllByStatus(TicketStatus status, Pageable pageable);
    Page<Ticket> findAllByPriority(TicketPriority priority, Pageable pageable);
    Page<Ticket> findAllByStatusAndPriority(TicketStatus status, TicketPriority priority, Pageable pageable);

    // --- Stats ---
    @Query("SELECT t.status, COUNT(t) FROM Ticket t WHERE t.createdBy = :user GROUP BY t.status")
    List<Object[]> countTicketsByStatusAndUser(User user);

    @Query("SELECT t.status, COUNT(t) FROM Ticket t GROUP BY t.status")
    List<Object[]> countAllTicketsByStatus();
}