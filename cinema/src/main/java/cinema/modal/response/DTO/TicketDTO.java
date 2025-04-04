package cinema.modal.response.DTO;

import cinema.modal.entity.Ticket;
import lombok.Data;

@Data
public class TicketDTO {
    private String ticket_id;
    private String account;
    private String seat;
    private String show_time;
    private String ticket_price;
    private String ticket_status;

    public TicketDTO(Ticket ticket) {
        if (ticket == null) {
            this.ticket_id = "N/A";
            this.account = "N/A";
            this.seat = "N/A";
            this.show_time = "N/A";
            this.ticket_price = "0"; // Giá trị mặc định
            this.ticket_status = "UNKNOWN";
        } else {
            this.ticket_id = String.valueOf(ticket.getId());
            this.account = (ticket.getAccount() != null) ? new AccountDTO(ticket.getAccount()).toString() : "N/A";
            this.seat = (ticket.getSeat() != null) ? new SeatDTO(ticket.getSeat()).toString() : "N/A";
            this.show_time = (ticket.getShowTime() != null) ? new ShowTimeDTO(ticket.getShowTime()).toString() : "N/A";

            // Kiểm tra kiểu dữ liệu của getPrice()
            Double price = ticket.getPrice(); // Nếu kiểu double, không cần kiểm tra null
            this.ticket_price = (price != null) ? String.valueOf(price) : "0";

            this.ticket_status = (ticket.getStatus() != null) ? String.valueOf(ticket.getStatus()) : "UNKNOWN";
        }
    }
}
