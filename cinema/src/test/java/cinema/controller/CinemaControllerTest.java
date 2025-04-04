package cinema.controller;

import cinema.modal.entity.Cinema;
import cinema.modal.request.CinemaRequest;
import cinema.modal.response.DTO.CinemaDTO;
import cinema.service.Cinema.CinemaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CinemaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CinemaService cinemaService;

    @InjectMocks
    private CinemaController cinemaController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cinemaController).build();
    }

    @Test
    void findCinemas_ShouldReturnCinemaList() throws Exception {
        Cinema cinema = new Cinema();
        cinema.setId(1);
        cinema.setName("Cinema Test");
        List<Cinema> cinemas = Arrays.asList(cinema);

        when(cinemaService.findCinemas()).thenReturn(cinemas);

        mockMvc.perform(get("/cinema/find"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cinema_name").value("Cinema Test"));
    }

    @Test
    void findById_ShouldReturnCinema() throws Exception {
        Cinema cinema = new Cinema();
        cinema.setId(1);
        cinema.setName("Cinema Test");

        when(cinemaService.findById(1)).thenReturn(cinema);

        mockMvc.perform(get("/cinema/findId/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cinema_name").value("Cinema Test"));
    }

    @Test
    void createCinema_ShouldReturnCreatedCinema() throws Exception {
        CinemaRequest request = new CinemaRequest();
        request.setName("New Cinema");

        Cinema cinema = new Cinema();
        cinema.setId(1);
        cinema.setName("New Cinema");

        when(cinemaService.createCinema(any(CinemaRequest.class))).thenReturn(cinema);

        mockMvc.perform(post("/cinema/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cinema_name").value("New Cinema"));
    }
}