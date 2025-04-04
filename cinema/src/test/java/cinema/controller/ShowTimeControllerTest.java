package cinema.controller;

import cinema.modal.entity.Cinema;
import cinema.modal.entity.Movie;
import cinema.modal.entity.Room;
import cinema.modal.entity.ShowTime;
import cinema.modal.request.ShowTimeRequest;
import cinema.modal.response.DTO.ShowTimeDTO;
import cinema.service.ShowTime.ShowTimeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ShowTimeControllerTest {
    private MockMvc mockMvc;

    @Mock
    private ShowTimeService showTimeService;

    @InjectMocks
    private ShowTimeController showTimeController;

    private ShowTime showTime;
    private ShowTimeRequest showTimeRequest;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(showTimeController).build();

        // Setup Cinema
        Cinema cinema = new Cinema();
        cinema.setId(1);
        cinema.setName("Cinema A");

        // Setup Room
        Room room = new Room();
        room.setId(1);
        room.setName("Room 1");
        room.setCinema(cinema);

        // Setup Movie
        Movie movie = new Movie();
        movie.setId(1);
        movie.setName("Movie A");

        // Setup ShowTime
        showTime = new ShowTime();
        showTime.setId(1);
        showTime.setMovie(movie);
        showTime.setRoom(room);
        showTime.setCinema(cinema);
        showTime.setShowDate(LocalDate.of(2024, 4, 1));
        showTime.setStartTime(Collections.singletonList(LocalTime.of(18, 30)));

        // Setup ShowTimeRequest
        showTimeRequest = new ShowTimeRequest();
        showTimeRequest.setMovieId(1);
        showTimeRequest.setRoomId(1);
        showTimeRequest.setShowDate("2024-04-01");
        showTimeRequest.setStartTime(Collections.singletonList("18:30"));
    }

    @Test
    void testFindById() throws Exception {
        when(showTimeService.findByID(1)).thenReturn(showTime);

        mockMvc.perform(get("/showTime/findId/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.show_time_id").value("1"))
                .andExpect(jsonPath("$.movie").value("Movie A"))
                .andExpect(jsonPath("$.room").value("Room 1"))
                .andExpect(jsonPath("$.cinema").value("Cinema A"))
                .andExpect(jsonPath("$.show_time_show_date").value("2024-04-01"))
                .andExpect(jsonPath("$.show_time_start_time").value("[18:30]"));
    }

    @Test
    void testFindById_NotFound() throws Exception {
        when(showTimeService.findByID(99)).thenReturn(null);

        mockMvc.perform(get("/showTime/findId/{id}", 99))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error: Suất chiếu không tồn tại"));
    }

    // ✅ Test tạo suất chiếu thành công
    @Test
    void testCreateShowTime() throws Exception {
        when(showTimeService.createShowTime(showTimeRequest)).thenReturn(showTime);

        mockMvc.perform(post("/showTime/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(showTimeRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.show_time_id").value("1"))
                .andExpect(jsonPath("$.movie").value("Movie A"))
                .andExpect(jsonPath("$.show_time_show_date").value("2024-04-01"))
                .andExpect(jsonPath("$.show_time_start_time").value("[18:30]"));
    }

    // ✅ Test cập nhật suất chiếu
    @Test
    void testUpdateShowTime() throws Exception {
        when(showTimeService.updateShowTime(1, showTimeRequest)).thenReturn(showTime);

        mockMvc.perform(put("/showTime/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(showTimeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.show_time_id").value("1"))
                .andExpect(jsonPath("$.movie").value("Movie A"))
                .andExpect(jsonPath("$.show_time_show_date").value("2024-04-01"));
    }

    // ✅ Test tìm suất chiếu theo phim và ngày
    @Test
    void testFindMovieAndShowDate() throws Exception {
        when(showTimeService.findByMovie(1, "2024-04-01"))
                .thenReturn(Arrays.asList(LocalTime.of(18, 30), LocalTime.of(20, 30)));

        mockMvc.perform(get("/showTime/findMovieAndShowDate/{movieId}", 1)
                        .param("date", "2024-04-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("18:30"))
                .andExpect(jsonPath("$[1]").value("20:30"));
    }

    // ✅ Test tìm ghế theo phim và giờ chiếu
    @Test
    void testFindSeatRoomByMovieAndStartTime() throws Exception {
        mockMvc.perform(get("/showTime/findSeatRoomByMovieAndStartTime/{movieId}/{startTime}", 1, "18:30"))
                .andExpect(status().isOk());
    }
}
