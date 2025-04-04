package cinema.controller;

import cinema.modal.entity.Cinema;
import cinema.modal.entity.Room;
import cinema.modal.entity.Seat;
import cinema.modal.entity.constant.TypeSeat;
import cinema.modal.entity.constant.StatusSeat;
import cinema.modal.request.SeatRequest;
import cinema.service.Seat.SeatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class SeatControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SeatService seatService;

    @InjectMocks
    private SeatController seatController;

    private Seat seat;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(seatController).build();

        // 🔥 Mock Cinema
        Cinema cinema = new Cinema();
        cinema.setId(1);
        cinema.setName("Cinema A");

        // 🔥 Mock Room
        Room room = new Room();
        room.setId(1);
        room.setName("Room A");
        room.setCinema(cinema); // ✅ Đảm bảo có Cinema

        // 🔥 Mock Seat
        seat = new Seat();
        seat.setId(1);
        seat.setName("A1");
        seat.setRoom(room); // ✅ Đảm bảo có Room
        seat.setType(TypeSeat.VIP);
        seat.setPrice(100);
        seat.setStatus(StatusSeat.AVAILABLE);
    }



    @Test
    void testFindAllSeats() throws Exception {
        when(seatService.findSeats()).thenReturn(Collections.singletonList(seat));

        mockMvc.perform(get("/seat/find"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].seat_name").value("A1"));
    }

    @Test
    void testFindSeatById() throws Exception {
        when(seatService.findById(1)).thenReturn(seat);

        mockMvc.perform(get("/seat/findId/{id}", 1))
                .andExpect(status().isOk()) //  Kiểm tra status 200
                .andExpect(jsonPath("$.seat_id").value("1")) // Kiểm tra seat_id trả về đúng
                .andExpect(jsonPath("$.seat_name").value("A1")) // ✅ Kiểm tra seat_name
                .andExpect(jsonPath("$.seat_type").value("VIP")) // ✅ Kiểm tra seat_type
                .andExpect(jsonPath("$.seat_price").value("100.0")) // ✅ Kiểm tra giá tiền
                .andExpect(jsonPath("$.seat_status").value("AVAILABLE")); // ✅ Kiểm tra trạng thái
    }


    @Test
    void testCreateSeat() throws Exception {
        SeatRequest request = new SeatRequest();
        request.setRow(1);
        request.setSeatPerRow(10);
        request.setRoomId(1);
        request.setType("STANDARD");

        when(seatService.createSeat(any(SeatRequest.class))).thenReturn(Collections.singletonList(seat));

        mockMvc.perform(post("/seat/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].seat_name").value("A1"));
    }
}