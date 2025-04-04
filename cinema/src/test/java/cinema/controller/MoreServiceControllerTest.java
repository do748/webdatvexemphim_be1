package cinema.controller;

import cinema.modal.entity.MoreService;
import cinema.modal.request.MoreServiceRequest;
import cinema.modal.response.DTO.MoreServiceDTO;
import cinema.service.MoreService.MoreServiceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.TestingAuthenticationToken;

@SpringBootTest
@AutoConfigureMockMvc
class MoreServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MoreServiceService moreServiceService;

    @Autowired
    private ObjectMapper objectMapper;

    private MoreService moreService;
    private MoreServiceDTO moreServiceDTO;
    private MoreServiceRequest moreServiceRequest;

    // Khởi tạo dữ liệu giả lập và cấu hình user có quyền ADMIN cho các test.
    @BeforeEach
    void setUp() {
        moreService = new MoreService();
        moreService.setId(1);
        moreService.setName("Popcorn");
        moreService.setImage("popcorn.jpg");
        moreService.setDescription("Delicious popcorn");
        moreService.setPrice(50.0);
        moreService.setStatus(cinema.modal.entity.constant.StatusService.ACTIVE);

        moreServiceDTO = new MoreServiceDTO(moreService);

        moreServiceRequest = new MoreServiceRequest();
        moreServiceRequest.setName("Popcorn");
        moreServiceRequest.setImage("popcorn.jpg");
        moreServiceRequest.setDescription("Delicious popcorn");
        moreServiceRequest.setPrice("50.0");

        // Giả lập user có quyền ADMIN
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new TestingAuthenticationToken("admin", "password", "ADMIN"));
        SecurityContextHolder.setContext(securityContext);
    }

    // Kiểm tra chức năng tìm kiếm tất cả dịch vụ.
    @Test
    void testFindServices() throws Exception {
        List<MoreServiceDTO> services = Arrays.asList(moreServiceDTO);
        when(moreServiceService.findServices()).thenReturn(List.of(moreService));

        mockMvc.perform(get("/moreService/find")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())  // Kiểm tra mã trạng thái trả về là OK
                .andExpect(jsonPath("$.size()").value(1)) // Kiểm tra kích thước danh sách trả về là 1
                .andExpect(jsonPath("$[0].service_name").value("1"));  // Kiểm tra tên dịch vụ trong phản hồi
    }

    // Kiểm tra chức năng tìm kiếm dịch vụ theo ID.
    @Test
    void testFindServiceById() throws Exception {
        when(moreServiceService.findById(1)).thenReturn(moreService);

        mockMvc.perform(get("/moreService/findId/1"))
                .andExpect(status().isOk())  // Kiểm tra mã trạng thái trả về là OK
                .andExpect(jsonPath("$.service_name").value("1")); // Kiểm tra tên dịch vụ trong phản hồi
    }

    // Mục đích: Kiểm tra chức năng tạo dịch vụ mới.
    @Test
    void testCreateService() throws Exception {
        when(moreServiceService.createService(Mockito.any(MoreServiceRequest.class))).thenReturn(moreService);

        mockMvc.perform(post("/moreService/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(moreServiceRequest)))
                .andExpect(status().isCreated())  // Kiểm tra mã trạng thái trả về là Created
                .andExpect(jsonPath("$.service_name").value("1"));  // Kiểm tra tên dịch vụ trong phản hồi
    }

    //  Kiểm tra chức năng cập nhật dịch vụ.
    @Test
    void testUpdateService() throws Exception {
        when(moreServiceService.updateService(Mockito.eq(1), Mockito.any(MoreServiceRequest.class))).thenReturn(moreService);

        mockMvc.perform(put("/moreService/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(moreServiceRequest)))
                .andExpect(status().isAccepted())  // Kiểm tra mã trạng thái trả về là Accepted
                .andExpect(jsonPath("$.service_name").value("1"));  // Kiểm tra tên dịch vụ trong phản hồi
    }

    // Kiểm tra chức năng thay đổi trạng thái dịch vụ.
    @Test
    void testChangeStatus() throws Exception {
        when(moreServiceService.changeStatus(1, "INACTIVE")).thenReturn(moreService);

        mockMvc.perform(post("/moreService/changeStatus/1")
                        .param("status", "INACTIVE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())  // Kiểm tra mã trạng thái trả về là Accepted
                .andExpect(jsonPath("$.service_name").value("1"));  // Kiểm tra tên dịch vụ trong phản hồi
    }

    // Kiểm tra chức năng tìm các dịch vụ có trạng thái ACTIVE.
    @Test
    void testFindActiveServices() throws Exception {
        when(moreServiceService.findStatusActive()).thenReturn(List.of(moreService));

        mockMvc.perform(get("/moreService/findActive"))
                .andExpect(status().isOk())  // Kiểm tra mã trạng thái trả về là OK
                .andExpect(jsonPath("$.size()").value(1))  // Kiểm tra kích thước danh sách trả về là 1
                .andExpect(jsonPath("$[0].service_name").value("1"));  // Kiểm tra tên dịch vụ trong phản hồi
    }
}
