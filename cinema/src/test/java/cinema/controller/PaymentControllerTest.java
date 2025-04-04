package cinema.controller;

import cinema.modal.entity.Payment;
import cinema.modal.entity.constant.TypePayment;
import cinema.modal.request.PaymentRequest;
import cinema.service.Payment.PaymentService;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PaymentControllerTest {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    private Payment payment;

    // Khởi tạo dữ liệu giả lập cho các kiểm thử.
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();

        // Setup đối tượng Payment mẫu
        payment = new Payment();
        payment.setId(1);
        payment.setType(TypePayment.E_WALLET);
        payment.setAddress("123 Main St");
    }

    // Kiểm tra API để lấy danh sách các khoản thanh toán.
    @Test
    void testFindAllPayments() throws Exception {
        when(paymentService.findPayments()).thenReturn(Collections.singletonList(payment));

        mockMvc.perform(get("/payment/find"))
                .andExpect(status().isOk()); // Kiểm tra mã trạng thái trả về là OK
    }

    // Kiểm tra API để tìm một khoản thanh toán theo ID.
    @Test
    void testFindPaymentById() throws Exception {
        when(paymentService.findPaymentById(1)).thenReturn(payment);

        mockMvc.perform(get("/payment/findId/1"))
                .andExpect(status().isOk()); // Kiểm tra mã trạng thái trả về là OK
    }

    // Mục đích: Kiểm tra API để tạo mới một khoản thanh toán.
    @Test
    void testCreatePayment() throws Exception {
        PaymentRequest request = new PaymentRequest();
        request.setType("CASH");
        request.setAddress("123 Main St");

        when(paymentService.createPayment(any(PaymentRequest.class))).thenReturn(payment);

        mockMvc.perform(post("/payment/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()); // Kiểm tra mã trạng thái trả về là OK
    }

    // Kiểm tra API để cập nhật một khoản thanh toán.
    @Test
    void testUpdatePayment() throws Exception {
        PaymentRequest request = new PaymentRequest();
        request.setType("CASH");
        request.setAddress("456 Elm St");

        when(paymentService.updatePayment(anyInt(), any(PaymentRequest.class))).thenReturn(payment);

        mockMvc.perform(put("/payment/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()); // Kiểm tra mã trạng thái trả về là OK
    }
}