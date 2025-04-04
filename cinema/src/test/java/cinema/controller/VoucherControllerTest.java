package cinema.controller;

import cinema.modal.entity.Voucher;
import cinema.modal.entity.constant.StatusVoucher;
import cinema.modal.request.VoucherRequest;
import cinema.modal.response.DTO.VoucherDTO;
import cinema.service.Voucher.VoucherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VoucherControllerTest {

    @Mock
    private VoucherService voucherService;

    @InjectMocks
    private VoucherController voucherController;

    private Voucher voucher;
    private VoucherRequest voucherRequest;

    @BeforeEach
    public void setUp() {
        voucher = new Voucher();
        voucher.setId(1);
        voucher.setName("Discount Voucher");
        voucher.setDescription("10% off");
        voucher.setDiscount(10.0);
        voucher.setQuantity(100);
        voucher.setExpiry(LocalDate.now().plusDays(10));
        voucher.setStatus(StatusVoucher.EFFECTIVE);

        voucherRequest = new VoucherRequest();
        voucherRequest.setName("Discount Voucher");
        voucherRequest.setDescription("10% off");
        voucherRequest.setDiscount("10.0");
        voucherRequest.setQuantity("100");
        voucherRequest.setExpiry("2025-04-15");
        voucherRequest.setStatus("EFFECTIVE");
    }

    @Test
    public void testFindAllVouchers() {
        List<Voucher> vouchers = Arrays.asList(voucher);
        when(voucherService.findVoucher()).thenReturn(vouchers);

        ResponseEntity<?> response = voucherController.find();
        assertEquals(200, response.getStatusCodeValue());

        List<VoucherDTO> voucherDTOS = (List<VoucherDTO>) response.getBody();
        assertNotNull(voucherDTOS);
        assertEquals(1, voucherDTOS.size());
        assertEquals("1", voucherDTOS.get(0).getVoucher_id());
        assertEquals("Discount Voucher", voucherDTOS.get(0).getVoucher_name());
    }

    @Test
    public void testFindVoucherById() {
        when(voucherService.findVoucherById(1)).thenReturn(voucher);

        ResponseEntity<?> response = voucherController.findById(1);
        assertEquals(200, response.getStatusCodeValue());

        VoucherDTO voucherDTO = (VoucherDTO) response.getBody();
        assertNotNull(voucherDTO);
        assertEquals("1", voucherDTO.getVoucher_id());
        assertEquals("Discount Voucher", voucherDTO.getVoucher_name());
    }

    @Test
    public void testCreateVoucher() {
        when(voucherService.createVoucher(any(VoucherRequest.class))).thenReturn(voucher);

        ResponseEntity<?> response = voucherController.create(voucherRequest);
        assertEquals(200, response.getStatusCodeValue());

        VoucherDTO voucherDTO = (VoucherDTO) response.getBody();
        assertNotNull(voucherDTO);
        assertEquals("1", voucherDTO.getVoucher_id());
        assertEquals("Discount Voucher", voucherDTO.getVoucher_name());
    }

    @Test
    public void testUpdateVoucher() {
        when(voucherService.updateVoucher(eq(1), any(VoucherRequest.class))).thenReturn(voucher);

        ResponseEntity<?> response = voucherController.update(1, voucherRequest);
        assertEquals(200, response.getStatusCodeValue());

        VoucherDTO voucherDTO = (VoucherDTO) response.getBody();
        assertNotNull(voucherDTO);
        assertEquals("1", voucherDTO.getVoucher_id());
        assertEquals("Discount Voucher", voucherDTO.getVoucher_name());
    }

    @Test
    public void testFindEffectiveVouchers() {
        List<Voucher> vouchers = Arrays.asList(voucher);
        when(voucherService.findEffectiveVouchers()).thenReturn(vouchers);

        ResponseEntity<?> response = voucherController.findEffective();
        assertEquals(200, response.getStatusCodeValue());

        List<VoucherDTO> voucherDTOS = (List<VoucherDTO>) response.getBody();
        assertNotNull(voucherDTOS);
        assertEquals(1, voucherDTOS.size());
        assertEquals("1", voucherDTOS.get(0).getVoucher_id());
        assertEquals("Discount Voucher", voucherDTOS.get(0).getVoucher_name());
    }
}
