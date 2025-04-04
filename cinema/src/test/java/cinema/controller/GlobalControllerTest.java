package cinema.controller;

import cinema.modal.entity.Account;
import cinema.modal.request.AccountRequest;
import cinema.modal.request.LoginRequest;
import cinema.modal.response.AuthResponse;
import cinema.modal.response.DTO.AccountDTO;
import cinema.service.Global.GlobalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.List;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

class GlobalControllerTest {

    @Mock
    private GlobalService globalService;

    @InjectMocks
    private GlobalController globalController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoginByEmail_Success() {
        // Given
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");

        AuthResponse authResponse = new AuthResponse(
                "mocked_token",
                "testUser",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        when(globalService.loginByEmail(request)).thenReturn(authResponse);

        // When
        ResponseEntity<?> response = globalController.loginByEmail(request);

        // Then
        assertEquals(OK, response.getStatusCode());
        assertEquals(authResponse, response.getBody());
        verify(globalService, times(1)).loginByEmail(request);
    }


    @Test
    void testLoginByEmail_Failure() {
        // Given
        LoginRequest request = new LoginRequest();
        request.setEmail("wrong@example.com");
        request.setPassword("wrongpassword");

        when(globalService.loginByEmail(request)).thenThrow(new RuntimeException());

        // When
        ResponseEntity<?> response = globalController.loginByEmail(request);

        // Then
        assertEquals(UNAUTHORIZED, response.getStatusCode());
        assertEquals(Map.of("message", "Thông tin đăng nhập không hợp lệ"), response.getBody());
        verify(globalService, times(1)).loginByEmail(request);
    }

    @Test
    void testRegister_Success() {
        // Given
        AccountRequest request = new AccountRequest();
        Account mockAccount = new Account();
        AccountDTO accountDTO = new AccountDTO(mockAccount);

        when(globalService.register(request)).thenReturn(mockAccount);

        // When
        ResponseEntity<?> response = globalController.register(request);

        // Then
        assertEquals(OK, response.getStatusCode());
        assertInstanceOf(AccountDTO.class, response.getBody());
        assertEquals(accountDTO, response.getBody());
        verify(globalService, times(1)).register(request);
    }

    @Test
    void testRegister_Failure() {
        // Given
        AccountRequest request = new AccountRequest();
        when(globalService.register(request)).thenThrow(new RuntimeException());

        // When
        ResponseEntity<?> response = globalController.register(request);

        // Then
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals(Map.of("message", "Error when register"), response.getBody());
        verify(globalService, times(1)).register(request);
    }

    @Test
    void testUploadImg_Success() throws IOException {
        // Given
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", new byte[]{1, 2, 3});
        when(globalService.upload(file)).thenReturn("uploaded_file_url");

        // When
        ResponseEntity<?> response = globalController.uploadImg(file);

        // Then
        assertEquals(OK, response.getStatusCode());
        assertEquals("uploaded_file_url", response.getBody());
        verify(globalService, times(1)).upload(file);
    }

    @Test
    void testUploadImg_Failure() throws IOException {
        // Given
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", new byte[]{1, 2, 3});
        when(globalService.upload(file)).thenThrow(new IOException());

        // When
        ResponseEntity<?> response = globalController.uploadImg(file);

        // Then
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals(Map.of("message", "Error when upload image"), response.getBody());
        verify(globalService, times(1)).upload(file);
    }
}
