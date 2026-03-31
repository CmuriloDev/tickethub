package com.carlosmurilo.tickethub.ticket.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class AssignTicketRequest {

    @NotNull
    private UUID assignedToId;

    public UUID getAssignedToId() { return assignedToId; }
    public void setAssignedToId(UUID assignedToId) { this.assignedToId = assignedToId; }
}