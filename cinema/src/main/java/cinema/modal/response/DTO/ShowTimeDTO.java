package cinema.modal.response.DTO;

import cinema.modal.entity.ShowTime;
import lombok.Data;

@Data
public class ShowTimeDTO {
    private String show_time_id;
    private String movie;
    private String room;
    private String cinema;
    private String show_time_show_date;
    private String show_time_start_time;

    public ShowTimeDTO(ShowTime showTime) {
        this.show_time_id = String.valueOf(showTime.getId());

        // Kiểm tra nếu movie hoặc room là null để tránh NullPointerException
        this.movie = (showTime.getMovie() != null) ? String.valueOf(showTime.getMovie().getName()) : "Unknown Movie";
        this.room = (showTime.getRoom() != null) ? String.valueOf(showTime.getRoom().getName()) : "Unknown Room";
        this.cinema = (showTime.getRoom() != null && showTime.getRoom().getCinema() != null)
                ? String.valueOf(showTime.getRoom().getCinema().getName())
                : "Unknown Cinema";

        this.show_time_show_date = (showTime.getShowDate() != null) ? String.valueOf(showTime.getShowDate()) : "Unknown Date";
        this.show_time_start_time = (showTime.getStartTime() != null) ? String.valueOf(showTime.getStartTime()) : "Unknown Start Time";
    }

}
