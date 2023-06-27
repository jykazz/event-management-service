package ru.rsreu.lutikov.sber.services;

import org.springframework.stereotype.Service;
import ru.rsreu.lutikov.sber.name.Ticket;
import ru.rsreu.lutikov.sber.repositories.TicketRepository;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Ticket createTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id).orElse(null);
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Ticket updateTicket(Long id, Ticket ticket) {
        Ticket existingTicket = ticketRepository.findById(id).orElse(null);
        if (existingTicket != null) {
            existingTicket.setUser(ticket.getUser());
            existingTicket.setEvent(ticket.getEvent());
            return ticketRepository.save(existingTicket);
        }
        return null;
    }

    public boolean deleteTicket(Long id) {
        if (ticketRepository.existsById(id)) {
            ticketRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Другие методы

}

