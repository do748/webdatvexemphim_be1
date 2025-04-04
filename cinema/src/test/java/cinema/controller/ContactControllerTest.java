package cinema.controller;

import cinema.modal.entity.Cinema;
import cinema.modal.entity.Contact;
import cinema.modal.entity.constant.City;
import cinema.modal.request.ContactRequest;
import cinema.modal.response.DTO.ContactDTO;
import cinema.service.Contact.ContactService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import cinema.modal.entity.constant.ServiceContact;
import cinema.modal.entity.constant.ContactStatus;


import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ContactControllerTest {

    // Khai báo MockMvc để thực hiện các yêu cầu HTTP
    @Autowired
    private MockMvc mockMvc;

    // Mock đối tượng ContactService
    @Mock
    private ContactService contactService;

    // Inject controller vào để kiểm tra logic của ContactController
    @InjectMocks
    private ContactController contactController;

    // ObjectMapper để chuyển đổi đối tượng thành JSON
    @Autowired
    private ObjectMapper objectMapper;

    // Thiết lập trước mỗi bài test
    @BeforeEach
    void setup() {
        // Khởi tạo các mock và builder cho MockMvc
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(contactController).build();
        objectMapper = new ObjectMapper();
    }

    // Tạo một đối tượng Contact giả
    private Contact mockContact() {
        Contact contact = new Contact();
        contact.setId(1);
        contact.setFullName("John Doe");
        contact.setEmail("john.doe@example.com");
        contact.setPhoneNumber("0123456789");
        contact.setDetails("This is a test contact.");
        contact.setCity(City.HANOI); // Đổi thành giá trị hợp lệ
        contact.setServiceContact(ServiceContact.BUY_GROUP_TICKET);
        contact.setContactStatus(ContactStatus.SUCCESS);
        contact.setCinema(new Cinema()); // Tránh lỗi null
        return contact;
    }

    // Tạo một đối tượng ContactRequest giả
    private ContactRequest mockContactRequest() {
        ContactRequest request = new ContactRequest();
        request.setFullname("John Doe");
        request.setEmail("john.doe@example.com");
        request.setPhoneNumber("0123456789");
        request.setCinemaId(1);
        request.setCity("HANOI"); // Giá trị hợp lệ
        request.setDetails("This is a test contact.");
        request.setServiceContact("BOOKING"); // Giá trị hợp lệ
        return request;
    }

    // Test lấy danh sách các contact
    @Test
    void testFindContact() throws Exception {
        // Khi gọi phương thức findContact, mock trả về một danh sách contact giả
        List<Contact> contacts = Collections.singletonList(mockContact());
        when(contactService.findContact()).thenReturn(contacts);

        // Gửi request GET đến /contact/find và kiểm tra kết quả trả về
        mockMvc.perform(get("/contact/find")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Kiểm tra mã trạng thái HTTP là 200 OK
                .andExpect(jsonPath("$.length()").value(1)); // Kiểm tra độ dài của danh sách trả về là 1
    }

    // Test lấy contact theo ID
    @Test
    void testFindById() throws Exception {
        // Khi gọi phương thức findById với ID bất kỳ, mock trả về contact giả
        when(contactService.findById(anyInt())).thenReturn(mockContact());
        System.out.println("Mock contact: " + mockContact()); // In ra thông tin contact mock (chỉ để tham khảo)

        // Gửi request GET đến /contact/findId/1 và kiểm tra kết quả trả về
        mockMvc.perform(get("/contact/findId/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Kiểm tra mã trạng thái HTTP là 200 OK
                .andExpect(jsonPath("$.full_name").value("John Doe")); // Kiểm tra full_name trong kết quả trả về
    }

    // Test tạo mới contact
    @Test
    void testCreateContact() throws Exception {
        // Khi gọi phương thức createContact với một ContactRequest bất kỳ, mock trả về contact đã tạo
        when(contactService.createContact(any())).thenReturn(mockContact());

        // Gửi request POST đến /contact/create để tạo mới contact
        mockMvc.perform(post("/contact/create")
                        .contentType(MediaType.APPLICATION_JSON) // Xác định kiểu nội dung là JSON
                        .content(objectMapper.writeValueAsString(mockContactRequest()))) // Chuyển ContactRequest thành chuỗi JSON
                .andExpect(status().isOk()) // Kiểm tra mã trạng thái trả về là 200 OK
                .andExpect(jsonPath("$.full_name").value("John Doe")); // Kiểm tra full_name trong kết quả trả về
    }

    // Test cập nhật contact
    @Test
    void testUpdateContact() throws Exception {
        // Khi gọi phương thức updateContact với một ContactRequest bất kỳ và ID contact, mock trả về contact đã cập nhật
        when(contactService.updateContact(any(), anyInt())).thenReturn(mockContact());

        // Gửi request PUT đến /contact/update/1 để cập nhật contact
        mockMvc.perform(put("/contact/update/1")
                        .contentType(MediaType.APPLICATION_JSON) // Xác định kiểu nội dung là JSON
                        .content(objectMapper.writeValueAsString(mockContactRequest()))) // Chuyển ContactRequest thành chuỗi JSON
                .andExpect(status().isOk()) // Kiểm tra mã trạng thái trả về là 200 OK
                .andExpect(jsonPath("$.full_name").value("John Doe")); // Kiểm tra full_name trong kết quả trả về
    }

    // Test thay đổi trạng thái của contact
    @Test
    void testChangeStatus() throws Exception {
        // Khi gọi phương thức changeStatus với ID contact và trạng thái bất kỳ, mock trả về contact đã thay đổi trạng thái
        when(contactService.changeStatus(anyInt(), anyString())).thenReturn(mockContact());

        // Gửi request POST đến /contact/changeStatus/1 để thay đổi trạng thái của contact
        mockMvc.perform(post("/contact/changeStatus/1")
                        .param("status", "ACTIVE") // Truyền tham số "status" với giá trị "ACTIVE"
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Kiểm tra mã trạng thái trả về là 200 OK
                .andExpect(jsonPath("$.full_name").value("John Doe")); // Kiểm tra full_name trong kết quả trả về
    }
}
