package cinema.service.Ticket;

import cinema.modal.entity.Account;
import cinema.modal.entity.Seat;
import cinema.modal.entity.ShowTime;
import cinema.modal.entity.Ticket;
import cinema.modal.entity.constant.StatusTicket;
import cinema.modal.request.TicketRequest;
import cinema.repository.AccountRepository;
import cinema.repository.SeatRepository;
import cinema.repository.ShowTimeRepository;
import cinema.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private ShowTimeRepository showTimeRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<Ticket> findTickets() {
        return ticketRepository.findAll();
    }

    @Override
    public Ticket findById(int id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found with id: " + id));
    }

    @Override
    public Ticket createTicket(TicketRequest request) {
        Ticket ticket = new Ticket();
        populateTicket(ticket, request);
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket updateTicket(int id, TicketRequest request) {
        Ticket ticket = findById(id);
        populateTicket(ticket, request);
        return ticketRepository.save(ticket);
    }

    private void populateTicket(Ticket ticket, TicketRequest request) {
        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new EntityNotFoundException("Account not found with id: " + request.getAccountId()));
        Seat seat = seatRepository.findById(request.getSeatId())
                .orElseThrow(() -> new EntityNotFoundException("Seat not found with id: " + request.getSeatId()));
        ShowTime showTime = showTimeRepository.findById(request.getShowTimeId())
                .orElseThrow(() -> new EntityNotFoundException("ShowTime not found with id: " + request.getShowTimeId()));

        ticket.setAccount(account);
        ticket.setSeat(seat);
        ticket.setShowTime(showTime);
        ticket.setStatus(StatusTicket.UNPAID);
    }
}