package cinema.modal.response.DTO;

import cinema.modal.entity.Seat;
import lombok.Data;

@Data
public class SeatDTO {
    private String seat_id;
    private String seat_name;
    private String cinema;
    private String room;
    private String seat_type;
    private String seat_price;
    private String seat_status;

    public SeatDTO(Seat seat) {
        this.seat_id = String.valueOf(seat.getId());
        this.seat_name = seat.getName();

        // Kiểm tra null trước khi lấy room và cinema
        if (seat.getRoom() != null) {
            this.room = String.valueOf(new RoomDTO(seat.getRoom()));
            if (seat.getRoom().getCinema() != null) {
                this.cinema = String.valueOf(new CinemaDTO(seat.getRoom().getCinema()));
            } else {
                this.cinema = "Unknown Cinema";
            }
        } else {
            this.room = "Unknown Room";
            this.cinema = "Unknown Cinema";
        }

        this.seat_type = String.valueOf(seat.getType());
        this.seat_price = String.valueOf(seat.getPrice());
        this.seat_status = String.valueOf(seat.getStatus());
    }
}
