package com.carlosmurilo.tickethub.ticket;

import com.carlosmurilo.tickethub.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    Page<Ticket> findAllByUser(User user, Pageable pageable);
    Optional<Ticket> findByIdAndUser(UUID id, User user);
}