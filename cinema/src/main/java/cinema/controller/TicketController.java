package cinema.controller;

import cinema.modal.entity.Ticket;
import cinema.modal.request.TicketRequest;
import cinema.modal.response.DTO.TicketDTO;
import cinema.service.Ticket.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
@RequestMapping("/ticket")
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @GetMapping("/find")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseEntity<?> findTicket() {
        try {
            List<Ticket> tickets = ticketService.findTickets();
            System.out.println("Tickets found: " + tickets);
            List<TicketDTO> ticketDTOs = tickets.stream()
                    .map(ticket -> {
                        System.out.println("Converting ticket: " + ticket);
                        return new TicketDTO(ticket);
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ticketDTOs);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống: " + e.getMessage());
        }
    }

    @GetMapping("/findId/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','USER')")
    public ResponseEntity<?> findById(@PathVariable int id) {
        try {
            Ticket ticket = ticketService.findById(id);
            if (ticket == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Không tìm thấy vé với ID: " + id);
            }
            return ResponseEntity.ok(new TicketDTO(ticket));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Không tìm thấy vé với ID: " + id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống: " + e.getMessage());
        }
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER','MANAGER')")
    public ResponseEntity<?> create(@RequestBody TicketRequest request) {
        try {
            Ticket newTicket = ticketService.createTicket(request);
            return ResponseEntity.ok(new TicketDTO(newTicket));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body("Dữ liệu không hợp lệ: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER','MANAGER')")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody TicketRequest request) {
        try {
            Ticket updatedTicket = ticketService.updateTicket(id, request);
            return ResponseEntity.ok(new TicketDTO(updatedTicket));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Không tìm thấy vé với ID: " + id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body("Dữ liệu không hợp lệ: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống: " + e.getMessage());
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Lỗi hệ thống: " + e.getMessage());
    }
}
