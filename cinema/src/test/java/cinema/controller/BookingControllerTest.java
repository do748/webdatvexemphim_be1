package cinema.controller;

import cinema.modal.entity.Account;
import cinema.config.jwt.JwtRequestFilter;
import cinema.modal.entity.Booking;
import cinema.modal.entity.MoreService;
import cinema.modal.entity.Voucher;
import cinema.modal.request.BookingRequest;
import cinema.modal.response.DTO.BookingDTO;
import cinema.service.Booking.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Chỉ test BookingController, loại bỏ JwtRequestFilter để không bị lỗi auth
@WebMvcTest(value = BookingController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtRequestFilter.class)
})
public class BookingControllerTest {

    // Dùng để giả lập các request HTTP
    @Autowired
    private MockMvc mockMvc;

    // Mock service để không gọi thật đến logic bên trong
    @MockBean
    private BookingService bookingService;

    // Inject controller vào test
    @InjectMocks
    private BookingController bookingController;

    // Mock một booking và DTO tương ứng
    @Mock
    private Booking booking;

    private BookingDTO bookingDTO;

    // Thiết lập dữ liệu dùng chung trước mỗi test
    @BeforeEach
    void setUp() {
        // Khởi tạo các mock
        MockitoAnnotations.openMocks(this);

        // Gắn controller vào mockMvc để test độc lập
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();

        // Tạo dữ liệu giả: tài khoản, dịch vụ phụ, voucher
        Account account = new Account();
        account.setId(1);

        MoreService moreService = new MoreService();
        moreService.setId(1);

        Voucher voucher = new Voucher();
        voucher.setId(1);

        // Gán dữ liệu vào booking
        booking = new Booking();
        booking.setAccount(account);
        booking.setMoreServices(moreService);
        booking.setVouchers(voucher);

        // Tạo DTO từ booking
        bookingDTO = new BookingDTO(booking);
    }

    // Test API lấy tất cả booking (ROLE: ADMIN)
    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testFindAll() throws Exception {
        // Khi gọi service thì trả về danh sách chứa 1 booking
        when(bookingService.findBookings()).thenReturn(Collections.singletonList(booking));

        // Gửi request GET đến /booking/find
        mockMvc.perform(get("/booking/find")
                        .contentType(MediaType.APPLICATION_JSON))
                // Kiểm tra trả về mã 200 OK
                .andExpect(status().isOk())
                // Kiểm tra phần tử đầu tiên có đúng booking_id không
                .andExpect(jsonPath("$[0].booking_id").value(bookingDTO.getBooking_id()));
    }

    // Test API tìm booking theo ID (ROLE: USER)
    @Test
    @WithMockUser(roles = {"USER"})
    void testFindById() throws Exception {
        // Khi gọi service với ID bất kỳ thì trả về booking giả
        when(bookingService.findById(anyInt())).thenReturn(booking);

        // Gửi request GET đến /booking/findId/1
        mockMvc.perform(get("/booking/findId/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.booking_id").value(bookingDTO.getBooking_id()));
    }

    // Test API tạo mới booking (ROLE: USER)
    @Test
    @WithMockUser(roles = {"USER"})
    void testCreateBooking() throws Exception {
        // Tạo request giả
        BookingRequest request = new BookingRequest();

        // Khi gọi createBooking thì trả về booking giả
        when(bookingService.createBooking(any(BookingRequest.class))).thenReturn(booking);

        // Gửi request POST đến /booking/create
        mockMvc.perform(post("/booking/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        // Chuyển request thành JSON
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.booking_id").value(bookingDTO.getBooking_id()));
    }

    // Test API cập nhật booking (ROLE: USER)
    @Test
    @WithMockUser(roles = {"USER"})
    void testUpdateBooking() throws Exception {
        BookingRequest request = new BookingRequest();

        // Giả lập service update trả về booking
        when(bookingService.updateBooking(anyInt(), any(BookingRequest.class))).thenReturn(booking);

        // Gửi request PUT đến /booking/update/1
        mockMvc.perform(put("/booking/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.booking_id").value(bookingDTO.getBooking_id()));
    }

    // Test API đổi trạng thái booking (ROLE: ADMIN)
    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testChangeStatus() throws Exception {
        // Khi gọi changeStatus thì trả về booking giả
        when(bookingService.changeStatus(anyInt(), any())).thenReturn(booking);

        // Gửi request POST với tham số status
        mockMvc.perform(post("/booking/changeStatus/1")
                        .param("status", "CONFIRMED")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.booking_id").value(bookingDTO.getBooking_id()));
    }
}