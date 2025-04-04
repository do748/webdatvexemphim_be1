package cinema.controller;

import cinema.modal.entity.*;
import cinema.modal.request.TicketRequest;
import cinema.modal.response.DTO.TicketDTO;
import cinema.repository.AccountRepository;
import cinema.repository.SeatRepository;
import cinema.repository.ShowTimeRepository;
import cinema.service.Ticket.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketControllerTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private ShowTimeRepository showTimeRepository;

    @InjectMocks
    private TicketController ticketController;

    @Mock
    private TicketService ticketService;

    private Ticket ticket;
    private TicketRequest ticketRequest;

    @Mock
    private Account account;

    @Mock
    private Seat seat;

    @Mock
    private ShowTime showTime;

    @Mock
    private Room room;


    @BeforeEach
    void setUp() {
        ticketRequest = new TicketRequest();
        ticketRequest.setAccountId(1);
        ticketRequest.setSeatId(2);
        ticketRequest.setShowTimeId(3);

        Movie movie = new Movie();
        movie.setId(1);
        movie.setName("Avengers");

        showTime = new ShowTime();
        showTime.setId(3);
        showTime.setMovie(movie);

        ticket = new Ticket();
        ticket.setId(1);
        ticket.setPrice(100.0);
        ticket.setAccount(account);
        ticket.setSeat(seat);
        ticket.setShowTime(showTime);

        lenient().when(seat.getRoom()).thenReturn(room);
    }

    @Test
    void testFindTicket() {
        when(ticketService.findTickets()).thenReturn(Arrays.asList(ticket));

        ResponseEntity<?> response = ticketController.findTicket();
        System.out.println("Response status: " + response.getStatusCodeValue());
        System.out.println("Response body: " + response.getBody()); // Debug lỗi nếu có

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof List);
    }

    @Test
    void testFindById() {
        when(ticketService.findById(1)).thenReturn(ticket);

        ResponseEntity<?> response = ticketController.findById(1);
        System.out.println("Response status: " + response.getStatusCodeValue());
        System.out.println("Response body: " + response.getBody()); // Debug lỗi nếu có

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof TicketDTO);
    }


    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testCreateTicket() {
        TicketRequest request = new TicketRequest();
        request.setAccountId(1);
        request.setSeatId(1);
        request.setShowTimeId(1);

        when(ticketService.createTicket(any(TicketRequest.class))).thenReturn(new Ticket());

        ResponseEntity<?> response = ticketController.create(request);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testUpdateTicket() {
        when(ticketService.updateTicket(1, ticketRequest)).thenReturn(ticket);

        ResponseEntity<?> response = ticketController.update(1, ticketRequest);
        System.out.println("Response status: " + response.getStatusCodeValue());
        System.out.println("Response body: " + response.getBody()); // Debug lỗi nếu có

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof TicketDTO);
    }

}