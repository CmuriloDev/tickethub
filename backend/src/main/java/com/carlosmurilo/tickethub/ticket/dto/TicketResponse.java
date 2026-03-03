package com.carlosmurilo.tickethub.ticket.dto;

import com.carlosmurilo.tickethub.ticket.TicketPriority;
import com.carlosmurilo.tickethub.ticket.TicketStatus;

import java.time.Instant;
import java.util.UUID;

public class TicketResponse {
    private UUID id;
    private String title;
    private String description;
    private TicketStatus status;
    private TicketPriority priority;
    private Instant createdAt;
    private Instant updatedAt;

    public TicketResponse(UUID id, String title, String description,
                          TicketStatus status, TicketPriority priority,
                          Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public TicketStatus getStatus() { return status; }
    public TicketPriority getPriority() { return priority; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}