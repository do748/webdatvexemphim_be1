package cinema.controller;

import cinema.modal.entity.Account;
import cinema.modal.entity.constant.Gender;
import cinema.modal.entity.constant.Role;
import cinema.modal.entity.constant.StatusAccount;
import cinema.modal.request.AccountRequest;
import cinema.modal.response.DTO.AccountDTO;
import cinema.service.Account.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Kích hoạt Mockito cho JUnit 5
class AccountControllerTest {

    @Mock // Tạo mock object cho AccountService
    private AccountService accountService;

    @InjectMocks // Inject mock vào AccountController
    private AccountController accountController;

    private Account account;
    private AccountRequest accountRequest;

    @BeforeEach // Chạy trước mỗi test case
    void setUp() {
        MockitoAnnotations.openMocks(this); // Khởi tạo mock object

        // Tạo đối tượng Account mẫu để kiểm tra
        account = new Account();
        account.setId(1);
        account.setUsername("johndoe");
        account.setFullName("John Doe");
        account.setBirthDate(LocalDate.of(1990, 1, 1));
        account.setEmail("johndoe@example.com");
        account.setPhoneNumber("123456789");
        account.setPassport("A1234567");
        account.setGender(Gender.MALE);
        account.setCity("New York");
        account.setDistrict("Brooklyn");
        account.setAddress("123 Street");
        account.setRole(Role.USER);
        account.setStatus(StatusAccount.ACTIVE);

        // Tạo request mẫu để test tạo và cập nhật tài khoản
        accountRequest = new AccountRequest();
        accountRequest.setUsername("janedoe");
        accountRequest.setFullName("Jane Doe");
        accountRequest.setEmail("janedoe@example.com");
    }

    @Test
    void testCreateAccount() { // Kiểm tra API tạo tài khoản
        try {
            // Giả lập hành vi của accountService.createAccount()
            when(accountService.createAccount(any(AccountRequest.class))).thenReturn(account);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage()); // Kiểm tra nếu có ngoại lệ không mong muốn
        }

        // Gọi API của controller
        ResponseEntity<?> response = accountController.createAccount(accountRequest);

        // Kiểm tra phản hồi HTTP status code
        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Account);

        // Chuyển đổi kết quả thành DTO để kiểm tra dữ liệu
        AccountDTO createdAccount = new AccountDTO((Account) response.getBody());
        assertEquals("1", createdAccount.getAccount_id()); // Kiểm tra ID tài khoản
    }


    @Test
    void testFindAccounts() { // Kiểm tra API lấy danh sách tài khoản
        // Giả lập danh sách tài khoản trả về từ service
        when(accountService.findAccounts()).thenReturn(Arrays.asList(account));

        // Gọi API của controller
        ResponseEntity<?> response = accountController.findAccounts();

        // Kiểm tra phản hồi
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        // Kiểm tra danh sách tài khoản trả về
        List<AccountDTO> accountDTOS = (List<AccountDTO>) response.getBody();
        assertEquals(1, accountDTOS.size());
        assertEquals("johndoe", accountDTOS.get(0).getUser_name());
    }

    @Test
    void testFindAccountByEmail() { // Kiểm tra API tìm tài khoản bằng email
        try {
            // Giả lập tìm tài khoản theo email
            when(accountService.findByEmail("johndoe@example.com")).thenReturn(account);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Gọi API
        ResponseEntity<?> response = accountController.findAccountsByEmail("johndoe@example.com");

        // Kiểm tra phản hồi
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        // Kiểm tra thông tin tài khoản trả về
        AccountDTO foundAccount = (AccountDTO) response.getBody();
        assertEquals("johndoe", foundAccount.getUser_name());
    }

    @Test
    void testUpdateAccount() { // Kiểm tra API cập nhật tài khoản
        try {
            // Giả lập cập nhật tài khoản
            when(accountService.updateAccount(eq(1), any(AccountRequest.class))).thenReturn(account);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Gọi API cập nhật tài khoản
        ResponseEntity<?> response = accountController.updateAccount(1, accountRequest);

        // Kiểm tra phản hồi
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        // Kiểm tra thông tin tài khoản sau cập nhật
        AccountDTO updatedAccount = (AccountDTO) response.getBody();
        assertEquals("johndoe", updatedAccount.getUser_name());
    }

    @Test
    void testChangeStatus() { // Kiểm tra API thay đổi trạng thái tài khoản
        try {
            // Giả lập cập nhật tài khoản
            when(accountService.changeStatus(eq(1), anyString())).thenReturn(account);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Gọi API cập nhật tài khoản
        ResponseEntity<?> response = accountController.changeStatus(1, "INACTIVE");

        // Kiểm tra phản hồi
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        // Kiểm tra tài khoản sau khi đổi trạng thái
        AccountDTO changedStatusAccount = (AccountDTO) response.getBody();
        assertEquals("johndoe", changedStatusAccount.getUser_name());
    }

    @Test
    void testFindById() {
        try {
            // Giả lập hành vi của accountService: khi gọi findById(1) thì trả về đối tượng account
            when(accountService.findById(eq(1))).thenReturn(account);
        } catch (Exception e) {
            // Nếu có lỗi xảy ra khi mock service, test sẽ thất bại với thông báo lỗi
            fail("Unexpected exception: " + e.getMessage());
        }

        // Gọi phương thức findById trong AccountController với ID là 1
        ResponseEntity<?> response = accountController.findById(1);

        // Kiểm tra mã trạng thái HTTP trả về có phải là 200 OK không
        assertEquals(200, response.getStatusCodeValue());

        // Đảm bảo body trong response không bị null
        assertNotNull(response.getBody());

        // Ép kiểu response body về kiểu AccountDTO để kiểm tra dữ liệu bên trong
        AccountDTO foundAccount = (AccountDTO) response.getBody();

        // Kiểm tra xem tên người dùng trong AccountDTO có đúng là "johndoe" không
        assertEquals("johndoe", foundAccount.getUser_name());
    }


    @Test
    void testCheckCoded() { // Kiểm tra khi xác nhận tài khoản bằng email và mã xác nhận
        try {
            // Giả lập hành vi của accountService.confirmAccount() trả về một đối tượng account nếu đúng email và code
            when(accountService.confirmAccount(eq("johndoe@example.com"), eq("123456"))).thenReturn(account);
        } catch (Exception e) {
            // Nếu có lỗi xảy ra trong khi mock, test sẽ thất bại
            fail("Unexpected exception: " + e.getMessage());
        }

        // Gọi phương thức checkCoded trong controller với email và mã xác thực
        ResponseEntity<?> response = accountController.checkCoded("johndoe@example.com", "123456");

        // Kiểm tra mã trạng thái trả về là 200 OK
        assertEquals(200, response.getStatusCodeValue());

        // Đảm bảo rằng phần body của response không bị null
        assertNotNull(response.getBody());

        // Ép kiểu kết quả trả về về AccountDTO
        AccountDTO verifiedAccount = (AccountDTO) response.getBody();

        // Kiểm tra xem tên người dùng có đúng không
        assertEquals("johndoe", verifiedAccount.getUser_name());
    }

    //  Kiểm tra chức năng đổi mật khẩu khi người dùng cung cấp thông tin hợp lệ
    @Test
    void testChangePassword() throws Exception {
        // Tạo một đối tượng Account giả để trả về sau khi đổi mật khẩu thành công
        Account mockAccount = new Account();
        mockAccount.setId(1);
        mockAccount.setEmail("johndoe@example.com");

        // Giả lập hành vi của service: đổi mật khẩu thành công thì trả về mockAccount
        when(accountService.changePassword("johndoe@example.com", "oldpassword", "newpassword"))
                .thenReturn(mockAccount); // Trả về tài khoản đã đổi mật khẩu

        // Gọi phương thức changePassword trong controller
        ResponseEntity<?> response = accountController.changePassword("johndoe@example.com", "oldpassword", "newpassword");

        // Kiểm tra mã trạng thái trả về là 200 OK
        assertEquals(200, response.getStatusCodeValue());

        // Đảm bảo response body không bị null
        assertNotNull(response.getBody());

        // Tạo AccountDTO từ Account để kiểm tra dữ liệu trả về
        AccountDTO responseBody = new AccountDTO((Account) response.getBody());

        // Kiểm tra xem email trong response có đúng không
        assertEquals("johndoe@example.com", responseBody.getEmail());
    }

    // Kiểm tra chức năng quên mật khẩu (forgotPassword) khi người dùng nhập email hợp lệ
    @Test
    void testForgotPassword() {
        try {
            // Giả lập hành vi của accountService.forgotPassword() trả về account khi gọi với email đúng
            when(accountService.forgotPassword("johndoe@example.com")).thenReturn(account);
        } catch (Exception e) {
            // Nếu xảy ra lỗi khi mock, test sẽ thất bại
            fail("Unexpected exception: " + e.getMessage());
        }

        // Gọi phương thức forgotPassword từ controller
        ResponseEntity<?> response = accountController.forgotPassword("johndoe@example.com");

        // Kiểm tra mã trạng thái trả về là 200 OK
        assertEquals(200, response.getStatusCodeValue());

        // Đảm bảo response body không null
        assertNotNull(response.getBody());

        // Ép kiểu kết quả trả về về AccountDTO
        AccountDTO resetAccount = (AccountDTO) response.getBody();

        // Kiểm tra xem tên người dùng có đúng không
        assertEquals("johndoe", resetAccount.getUser_name());
    }

}
