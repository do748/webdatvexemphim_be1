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

@WebMvcTest(value = BookingController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtRequestFilter.class)
})
public  class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    @Mock
    private Booking booking;

    private BookingDTO bookingDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();

        // Tạo dữ liệu giả để tránh null
        Account account = new Account();
        account.setId(1);

        MoreService moreService = new MoreService();
        moreService.setId(1);

        Voucher voucher = new Voucher();
        voucher.setId(1);

        booking = new Booking();
        booking.setAccount(account);
        booking.setMoreServices(moreService);
        booking.setVouchers(voucher);

        bookingDTO = new BookingDTO(booking);
    }



    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testFindAll() throws Exception {
        when(bookingService.findBookings()).thenReturn(Collections.singletonList(booking));

        mockMvc.perform(get("/booking/find")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].booking_id").value(bookingDTO.getBooking_id()));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void testFindById() throws Exception {
        when(bookingService.findById(anyInt())).thenReturn(booking);

        mockMvc.perform(get("/booking/findId/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.booking_id").value(bookingDTO.getBooking_id()));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void testCreateBooking() throws Exception {
        BookingRequest request = new BookingRequest();
        when(bookingService.createBooking(any(BookingRequest.class))).thenReturn(booking);

        mockMvc.perform(post("/booking/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.booking_id").value(bookingDTO.getBooking_id()));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void testUpdateBooking() throws Exception {
        BookingRequest request = new BookingRequest();
        when(bookingService.updateBooking(anyInt(), any(BookingRequest.class))).thenReturn(booking);

        mockMvc.perform(put("/booking/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.booking_id").value(bookingDTO.getBooking_id()));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testChangeStatus() throws Exception {
        when(bookingService.changeStatus(anyInt(), any())).thenReturn(booking);

        mockMvc.perform(post("/booking/changeStatus/1")
                        .param("status", "CONFIRMED")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.booking_id").value(bookingDTO.getBooking_id()));
    }
}