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

    // Khai báo GlobalService là mock
    @Mock
    private GlobalService globalService;

    // Inject đối tượng globalController vào để kiểm thử các API trong controller
    @InjectMocks
    private GlobalController globalController;

    // Thiết lập các đối tượng cần thiết trước mỗi bài kiểm tra
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Mở mock để inject vào các đối tượng test
    }

    // Test cho việc đăng nhập thành công bằng email
    @Test
    void testLoginByEmail_Success() {
        // Given: Tạo request đăng nhập với email và mật khẩu hợp lệ
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");

        // Tạo đối tượng AuthResponse giả cho phản hồi hợp lệ
        AuthResponse authResponse = new AuthResponse(
                "mocked_token",
                "testUser",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        // Mock khi gọi globalService.loginByEmail với request, trả về authResponse
        when(globalService.loginByEmail(request)).thenReturn(authResponse);

        // When: Gửi request đăng nhập từ controller
        ResponseEntity<?> response = globalController.loginByEmail(request);

        // Then: Kiểm tra phản hồi trả về có mã trạng thái OK và dữ liệu trả về là đúng
        assertEquals(OK, response.getStatusCode());
        assertEquals(authResponse, response.getBody());
        verify(globalService, times(1)).loginByEmail(request); // Kiểm tra globalService đã được gọi 1 lần
    }


    // Test cho việc đăng nhập thất bại
    @Test
    void testLoginByEmail_Failure() {
        // Given: Tạo request đăng nhập với email và mật khẩu sai
        LoginRequest request = new LoginRequest();
        request.setEmail("wrong@example.com");
        request.setPassword("wrongpassword");

        // Mock khi gọi globalService.loginByEmail với request, sẽ ném ngoại lệ RuntimeException
        when(globalService.loginByEmail(request)).thenThrow(new RuntimeException());

        // When: Gửi request đăng nhập từ controller
        ResponseEntity<?> response = globalController.loginByEmail(request);

        // Then: Kiểm tra phản hồi trả về có mã trạng thái UNAUTHORIZED và thông báo lỗi
        assertEquals(UNAUTHORIZED, response.getStatusCode());
        assertEquals(Map.of("message", "Thông tin đăng nhập không hợp lệ"), response.getBody());
        verify(globalService, times(1)).loginByEmail(request); // Kiểm tra globalService đã được gọi 1 lần
    }

    // Test cho việc đăng ký tài khoản thành công
    @Test
    void testRegister_Success() {
        // Given: Tạo request đăng ký với thông tin tài khoản hợp lệ
        AccountRequest request = new AccountRequest();
        Account mockAccount = new Account(); // Tạo đối tượng Account giả
        AccountDTO accountDTO = new AccountDTO(mockAccount); // Chuyển đổi thành AccountDTO

        // Mock khi gọi globalService.register, trả về mockAccount
        when(globalService.register(request)).thenReturn(mockAccount);

        // When: Gửi request đăng ký từ controller
        ResponseEntity<?> response = globalController.register(request);

        // Then: Kiểm tra phản hồi trả về có mã trạng thái OK và dữ liệu trả về là AccountDTO
        assertEquals(OK, response.getStatusCode());
        assertInstanceOf(AccountDTO.class, response.getBody());
        assertEquals(accountDTO, response.getBody());
        verify(globalService, times(1)).register(request); // Kiểm tra globalService đã được gọi 1 lần
    }

    // Test cho việc đăng ký tài khoản thất bại
    @Test
    void testRegister_Failure() {
        // Given: Tạo request đăng ký với thông tin tài khoản hợp lệ
        AccountRequest request = new AccountRequest();

        // Mock khi gọi globalService.register, sẽ ném ngoại lệ RuntimeException
        when(globalService.register(request)).thenThrow(new RuntimeException());

        // When: Gửi request đăng ký từ controller
        ResponseEntity<?> response = globalController.register(request);

        // Then: Kiểm tra phản hồi trả về có mã trạng thái BAD_REQUEST và thông báo lỗi
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals(Map.of("message", "Error when register"), response.getBody());
        verify(globalService, times(1)).register(request); // Kiểm tra globalService đã được gọi 1 lần
    }

    // Test cho việc upload hình ảnh thành công
    @Test
    void testUploadImg_Success() throws IOException {
        // Given: Tạo file hình ảnh giả
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", new byte[]{1, 2, 3});

        // Mock khi gọi globalService.upload, trả về đường dẫn file đã upload
        when(globalService.upload(file)).thenReturn("uploaded_file_url");

        // When: Gửi request upload hình ảnh từ controller
        ResponseEntity<?> response = globalController.uploadImg(file);

        // Then: Kiểm tra phản hồi trả về có mã trạng thái OK và đường dẫn file đã upload
        assertEquals(OK, response.getStatusCode());
        assertEquals("uploaded_file_url", response.getBody());
        verify(globalService, times(1)).upload(file); // Kiểm tra globalService đã được gọi 1 lần
    }

    // Test cho việc upload hình ảnh thất bại
    @Test
    void testUploadImg_Failure() throws IOException {
        // Given: Tạo file hình ảnh giả
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", new byte[]{1, 2, 3});

        // Mock khi gọi globalService.upload, sẽ ném ngoại lệ IOException
        when(globalService.upload(file)).thenThrow(new IOException());

        // When: Gửi request upload hình ảnh từ controller
        ResponseEntity<?> response = globalController.uploadImg(file);

        // Then: Kiểm tra phản hồi trả về có mã trạng thái BAD_REQUEST và thông báo lỗi
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals(Map.of("message", "Error when upload image"), response.getBody());
        verify(globalService, times(1)).upload(file); // Kiểm tra globalService đã được gọi 1 lần
    }
}
