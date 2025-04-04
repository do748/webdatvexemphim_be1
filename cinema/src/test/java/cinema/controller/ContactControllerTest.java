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

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ContactService contactService;

    @InjectMocks
    private ContactController contactController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(contactController).build();
        objectMapper = new ObjectMapper();
    }

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

    @Test
    void testFindContact() throws Exception {
        List<Contact> contacts = Collections.singletonList(mockContact());
        when(contactService.findContact()).thenReturn(contacts);

        mockMvc.perform(get("/contact/find")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testFindById() throws Exception {
        when(contactService.findById(anyInt())).thenReturn(mockContact());
        System.out.println("Mock contact: " + mockContact());

        mockMvc.perform(get("/contact/findId/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.full_name").value("John Doe"));
    }

    @Test
    void testCreateContact() throws Exception {
        when(contactService.createContact(any())).thenReturn(mockContact());

        mockMvc.perform(post("/contact/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockContactRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.full_name").value("John Doe"));
    }

    @Test
    void testUpdateContact() throws Exception {
        when(contactService.updateContact(any(), anyInt())).thenReturn(mockContact());

        mockMvc.perform(put("/contact/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockContactRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.full_name").value("John Doe"));
    }

    @Test
    void testChangeStatus() throws Exception {
        when(contactService.changeStatus(anyInt(), anyString())).thenReturn(mockContact());

        mockMvc.perform(post("/contact/changeStatus/1")
                        .param("status", "ACTIVE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.full_name").value("John Doe"));
    }
}
