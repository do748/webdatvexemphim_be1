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

    // Khai báo MockMvc để thực hiện các yêu cầu HTTP
    private MockMvc mockMvc;

    // Mock đối tượng CinemaService
    @Mock
    private CinemaService cinemaService;

    // Inject controller vào để kiểm tra logic của CinemaController
    @InjectMocks
    private CinemaController cinemaController;

    // ObjectMapper để chuyển đổi đối tượng thành JSON
    private ObjectMapper objectMapper = new ObjectMapper();

    // Thiết lập trước mỗi bài test
    @BeforeEach
    void setUp() {
        // Khởi tạo các mock và builder cho MockMvc
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cinemaController).build();
    }

    // Test lấy danh sách các cinema
    @Test
    void findCinemas_ShouldReturnCinemaList() throws Exception {
        // Tạo đối tượng Cinema giả và cho vào danh sách
        Cinema cinema = new Cinema();
        cinema.setId(1);
        cinema.setName("Cinema Test");
        List<Cinema> cinemas = Arrays.asList(cinema);

        // Khi gọi phương thức findCinemas, mock trả về danh sách cinema giả
        when(cinemaService.findCinemas()).thenReturn(cinemas);

        // Gửi request GET đến /cinema/find và kiểm tra kết quả trả về
        mockMvc.perform(get("/cinema/find"))
                .andExpect(status().isOk()) // Kiểm tra mã trạng thái HTTP là 200 OK
                .andExpect(jsonPath("$[0].cinema_name").value("Cinema Test")); // Kiểm tra cinema_name của phần tử đầu tiên trong danh sách
    }

    // Test lấy cinema theo ID
    @Test
    void findById_ShouldReturnCinema() throws Exception {
        // Tạo đối tượng Cinema giả với ID = 1
        Cinema cinema = new Cinema();
        cinema.setId(1);
        cinema.setName("Cinema Test");

        // Khi gọi phương thức findById với ID = 1, mock trả về cinema giả
        when(cinemaService.findById(1)).thenReturn(cinema);

        // Gửi request GET đến /cinema/findId/1 và kiểm tra kết quả trả về
        mockMvc.perform(get("/cinema/findId/1"))
                .andExpect(status().isOk()) // Kiểm tra mã trạng thái HTTP là 200 OK
                .andExpect(jsonPath("$.cinema_name").value("Cinema Test")); // Kiểm tra cinema_name trong kết quả trả về
    }

    // Test tạo mới cinema
    @Test
    void createCinema_ShouldReturnCreatedCinema() throws Exception {
        // Tạo đối tượng CinemaRequest để gửi trong yêu cầu tạo cinema mới
        CinemaRequest request = new CinemaRequest();
        request.setName("New Cinema");

        // Tạo đối tượng Cinema giả đã được tạo thành công
        Cinema cinema = new Cinema();
        cinema.setId(1);
        cinema.setName("New Cinema");

        // Khi gọi phương thức createCinema với CinemaRequest, mock trả về cinema đã tạo
        when(cinemaService.createCinema(any(CinemaRequest.class))).thenReturn(cinema);

        // Gửi request POST đến /cinema/create để tạo mới cinema
        mockMvc.perform(post("/cinema/create")
                        .contentType(MediaType.APPLICATION_JSON) // Xác định kiểu nội dung là JSON
                        .content(objectMapper.writeValueAsString(request))) // Chuyển CinemaRequest thành chuỗi JSON
                .andExpect(status().isCreated()) // Kiểm tra mã trạng thái trả về là 201 Created
                .andExpect(jsonPath("$.cinema_name").value("New Cinema")); // Kiểm tra cinema_name trong kết quả trả về
    }
}