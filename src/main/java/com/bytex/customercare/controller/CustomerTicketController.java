package com.bytex.customercare.controller;

import com.bytex.customercare.model.Ticket;
import com.bytex.customercare.model.Response;
import com.bytex.customercare.model.User;
import com.bytex.customercare.service.TicketService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/customer/tickets")
public class CustomerTicketController {

    @Autowired
    private TicketService ticketService;

    // List all tickets
    @GetMapping("")
    public String listTickets(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRole().equals("Customer")) {
            return "redirect:/login";
        }

        List<Ticket> tickets = ticketService.getTicketsByCustomerId(user.getUserId());
        model.addAttribute("tickets", tickets);
        return "customer/tickets";
    }

    // Show form to create a new ticket
    @GetMapping("/create")
    public String showCreateForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRole().equals("Customer")) {
            return "redirect:/login";
        }

        model.addAttribute("ticket", new Ticket());
        return "customer/ticket-create";
    }

    // Process ticket creation
    @PostMapping("/create")
    public String createTicket(@ModelAttribute Ticket ticket, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRole().equals("Customer")) {
            return "redirect:/login";
        }

        ticket.setCustomerId(user.getUserId());
        ticketService.createTicket(ticket);
        return "redirect:/customer/tickets";
    }

    // View single ticket details
    @GetMapping("/{id}")
    public String viewTicket(@PathVariable("id") Integer id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRole().equals("Customer")) {
            return "redirect:/login";
        }

        Optional<Ticket> ticket = ticketService.getTicketById(id);
        if (ticket.isPresent() && ticket.get().getCustomerId().equals(user.getUserId())) {
            model.addAttribute("ticket", ticket.get());
            model.addAttribute("responses", ticketService.getResponsesByTicketId(id));
            model.addAttribute("newResponse", new Response());
            return "customer/ticket-detail";
        }
        return "redirect:/customer/tickets";
    }

    // Show form to edit a ticket
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable("id") Integer id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRole().equals("Customer")) {
            return "redirect:/login";
        }

        Optional<Ticket> ticket = ticketService.getTicketById(id);
        if (ticket.isPresent() && ticket.get().getCustomerId().equals(user.getUserId())) {
            model.addAttribute("ticket", ticket.get());
            return "customer/ticket-edit";
        }
        return "redirect:/customer/tickets";
    }

    // Process ticket update
    @PostMapping("/{id}/edit")
    public String updateTicket(@PathVariable("id") Integer id, @ModelAttribute Ticket ticket, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRole().equals("Customer")) {
            return "redirect:/login";
        }

        Optional<Ticket> existingTicket = ticketService.getTicketById(id);
        if (existingTicket.isPresent() && existingTicket.get().getCustomerId().equals(user.getUserId())) {
            ticket.setTicketId(id);
            ticket.setCustomerId(user.getUserId());
            // Keep the original status, assigned user, etc.
            ticket.setStatus(existingTicket.get().getStatus());
            ticket.setAssignedToId(existingTicket.get().getAssignedToId());
            ticket.setPriority(existingTicket.get().getPriority());
            ticket.setCreatedAt(existingTicket.get().getCreatedAt());

            ticketService.updateTicket(ticket);
        }
        return "redirect:/customer/tickets/" + id;
    }

    // Add response to ticket
    @PostMapping("/{id}/respond")
    public String addResponse(@PathVariable("id") Integer id, @ModelAttribute Response response, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRole().equals("Customer")) {
            return "redirect:/login";
        }

        Optional<Ticket> ticket = ticketService.getTicketById(id);
        if (ticket.isPresent() && ticket.get().getCustomerId().equals(user.getUserId())) {
            response.setTicketId(id);
            response.setUserId(user.getUserId());
            ticketService.addResponse(response);
        }
        return "redirect:/customer/tickets/" + id;
    }

    // Cancel (delete) a ticket
    @PostMapping("/{id}/cancel")
    public String cancelTicket(@PathVariable("id") Integer id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRole().equals("Customer")) {
            return "redirect:/login";
        }

        Optional<Ticket> ticket = ticketService.getTicketById(id);
        if (ticket.isPresent() && ticket.get().getCustomerId().equals(user.getUserId())) {
            ticketService.cancelTicket(id);
        }
        return "redirect:/customer/tickets";
    }
}