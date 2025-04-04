package cinema.controller;

import cinema.modal.entity.Receipt;
import cinema.modal.entity.constant.StatusReceipt;
import cinema.modal.entity.constant.TypeReceipt;
import cinema.modal.request.ReceiptRequest;
import cinema.service.Receipt.ReceiptService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class ReceiptControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReceiptService receiptService;

    @InjectMocks
    private ReceiptController receiptController;

    private Receipt receipt;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(receiptController).build();

        receipt = new Receipt();
        receipt.setId(1);
        receipt.setType(TypeReceipt.INCOME);
        receipt.setReason("Test Receipt");
        receipt.setAmount(100.0);
        receipt.setStatus(StatusReceipt.PROCESSED);
    }

    @Test
    void testFindAllReceipts() throws Exception {
        when(receiptService.findReceipts()).thenReturn(Collections.singletonList(receipt));

        mockMvc.perform(get("/receipt/find"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void testFindReceiptById() throws Exception {
        when(receiptService.findReceiptById(anyInt())).thenReturn(receipt);

        mockMvc.perform(get("/receipt/findId/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testFindIncome() throws Exception {
        when(receiptService.findIncome()).thenReturn(Collections.singletonList(receipt));

        mockMvc.perform(get("/receipt/findIncome"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").value("INCOME"));
    }

    @Test
    void testFindSpending() throws Exception {
        when(receiptService.findSpending()).thenReturn(Collections.singletonList(receipt));

        mockMvc.perform(get("/receipt/findSpending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").value("INCOME"));
    }

    @Test
    void testCreateReceipt() throws Exception {
        ReceiptRequest request = new ReceiptRequest();
        request.setType("INCOME");
        request.setAccount(1);
        request.setBooking(1);
        request.setReason("New Receipt");
        request.setAmount("150.0");

        when(receiptService.ceateReceipt(any(ReceiptRequest.class))).thenReturn(receipt);

        mockMvc.perform(post("/receipt/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reason").value("Test Receipt"));
    }

    @Test
    void testUpdateReceipt() throws Exception {
        ReceiptRequest request = new ReceiptRequest();
        request.setType("INCOME");
        request.setAccount(1);
        request.setBooking(1);
        request.setReason("Updated Receipt");
        request.setAmount("200.0");

        when(receiptService.updateReceipt(anyInt(), any(ReceiptRequest.class))).thenReturn(receipt);

        mockMvc.perform(put("/receipt/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reason").value("Test Receipt"));
    }

    @Test
    void testChangeStatus() throws Exception {
        receipt.setStatus(StatusReceipt.PROCESSED); // Đảm bảo status đúng
        when(receiptService.changeStatus(anyInt(), any(String.class))).thenReturn(receipt);

        mockMvc.perform(put("/receipt/changeStatus/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"PROCESSED\"")) // Đổi từ "APPROVED" thành "PROCESSED"
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PROCESSED"));
    }
}