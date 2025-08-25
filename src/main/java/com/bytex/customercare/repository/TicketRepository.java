package com.bytex.customercare.repository;

import com.bytex.customercare.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    List<Ticket> findByCustomerId(Integer customerId);
    List<Ticket> findByCustomerIdAndStatus(Integer customerId, String status);
}