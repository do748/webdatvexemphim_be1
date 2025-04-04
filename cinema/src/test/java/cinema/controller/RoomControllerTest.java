package cinema.controller;

import cinema.modal.entity.Cinema;
import cinema.modal.entity.Room;
import cinema.modal.entity.constant.StatusRoom;
import cinema.modal.request.RoomRequest;
import cinema.service.Room.RoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class RoomControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RoomService roomService;

    @InjectMocks
    private RoomController roomController;

    private Room room;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(roomController).build();

        Cinema cinema = new Cinema();
        cinema.setId(1);
        cinema.setName("Cinema 1");

        room = new Room();
        room.setId(1);
        room.setName("Room A");
        room.setStatus(StatusRoom.AVAILABLE);
        room.setCinema(cinema); // Fix lỗi null cinema
    }

    @Test
    void testFindAllRooms() throws Exception {
        when(roomService.findRooms()).thenReturn(Collections.singletonList(room));

        mockMvc.perform(get("/room/find"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].room_id").value("1"));
    }

    @Test
    void testCreateRoom() throws Exception {
        RoomRequest request = new RoomRequest();
        request.setName("Room B");
        request.setCinemaId(1);
        request.setScreenType("IMAX");

        when(roomService.createRoom(any(RoomRequest.class))).thenReturn(room);

        mockMvc.perform(post("/room/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.room_name").value("Room A"));
    }

    @Test
    void testUpdateRoom() throws Exception {
        RoomRequest request = new RoomRequest();
        request.setName("Room C");
        request.setCinemaId(1);
        request.setScreenType("DOLBY");

        when(roomService.updateRoom(anyInt(), any(RoomRequest.class))).thenReturn(room);

        mockMvc.perform(put("/room/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.room_name").value("Room A"));
    }

    @Test
    void testChangeStatus() throws Exception {
        room.setStatus(StatusRoom.MAINTENANCE);
        when(roomService.changeStatus(anyInt(), any(String.class))).thenReturn(room);

        mockMvc.perform(post("/room/changeStatus/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("status", "MAINTENANCE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.room_status").value("MAINTENANCE"));
    }
}