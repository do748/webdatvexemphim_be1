package cinema.modal.response.DTO;

import cinema.modal.entity.Booking;
import lombok.Data;

import java.util.Collections;
import java.util.List;


@Data
public class BookingDTO {
    private String booking_id;
    private String account;
    private List<String> ticket;
    private String more_service;
    private String voucher;
    private String booking_price;
    private String booking_status;

    public BookingDTO(Booking booking) {
        this.booking_id = String.valueOf(booking.getId());
        this.account = (booking.getAccount() != null) ? String.valueOf(new AccountDTO(booking.getAccount())) : "N/A";
        this.ticket = Collections.singletonList(String.valueOf(booking.getTickets()));
        this.more_service = (booking.getMoreServices() != null) ? String.valueOf(new MoreServiceDTO(booking.getMoreServices())) : "N/A";
        this.voucher = (booking.getVouchers() != null) ? String.valueOf(new VoucherDTO(booking.getVouchers())) : "N/A";
        this.booking_price = String.valueOf(booking.getPrice());
        this.booking_status = String.valueOf(booking.getStatus());
    }
}
