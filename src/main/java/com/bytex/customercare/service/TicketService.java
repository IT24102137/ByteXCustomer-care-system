package com.bytex.customercare.service;

import com.bytex.customercare.model.Ticket;
import com.bytex.customercare.model.Response;
import com.bytex.customercare.repository.TicketRepository;
import com.bytex.customercare.repository.ResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing customer support tickets
 * Updated: 2025-08-25 21:21:33
 * Developer: IT24102137
 */
@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ResponseRepository responseRepository;

    // Get all tickets for a customer
    public List<Ticket> getTicketsByCustomerId(Integer customerId) {
        return ticketRepository.findByCustomerId(customerId);
    }

    // Get a single ticket by ID
    public Optional<Ticket> getTicketById(Integer ticketId) {
        return ticketRepository.findById(ticketId);
    }

    // Create a new ticket - FIXED to preserve user-selected priority
    public Ticket createTicket(Ticket ticket) {
        ticket.setStatus("Open");

        // Only set default priority if none is provided
        if (ticket.getPriority() == null || ticket.getPriority().isEmpty()) {
            ticket.setPriority("Medium");
        }

        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setUpdatedAt(LocalDateTime.now());
        return ticketRepository.save(ticket);
    }

    // Update ticket information
    public Ticket updateTicket(Ticket ticket) {
        ticket.setUpdatedAt(LocalDateTime.now());
        return ticketRepository.save(ticket);
    }

    // Cancel (delete) a ticket - FIXED to handle foreign key constraints
    @Transactional
    public void cancelTicket(Integer ticketId) {
        // First delete all responses associated with this ticket
        List<Response> responses = responseRepository.findByTicketId(ticketId);
        if (!responses.isEmpty()) {
            responseRepository.deleteAll(responses);
        }

        // Then delete the ticket
        ticketRepository.deleteById(ticketId);
    }

    // Add response to a ticket
    public Response addResponse(Response response) {
        response.setCreatedAt(LocalDateTime.now());

        // Update the ticket's last update time
        Optional<Ticket> ticket = ticketRepository.findById(response.getTicketId());
        if (ticket.isPresent()) {
            Ticket updatedTicket = ticket.get();
            updatedTicket.setUpdatedAt(LocalDateTime.now());
            ticketRepository.save(updatedTicket);
        }

        return responseRepository.save(response);
    }

    // Get all responses for a ticket
    public List<Response> getResponsesByTicketId(Integer ticketId) {
        return responseRepository.findByTicketId(ticketId);
    }
}