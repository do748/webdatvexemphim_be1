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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Mock  //Sẽ chỉ thực hiện giả lập, không lưu trực tiê vào db
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private Account account;
    private AccountRequest accountRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

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

        accountRequest = new AccountRequest();
        accountRequest.setUsername("janedoe");
        accountRequest.setFullName("Jane Doe");
        accountRequest.setEmail("janedoe@example.com");
    }

    @Test
    void testCreateAccount() {
        try {
            // Giả lập `accountService.createAccount()` trả về một `Account`, không phải `AccountDTO`
            when(accountService.createAccount(any(AccountRequest.class))).thenReturn(account);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Gọi API của controller
        ResponseEntity<?> response = accountController.createAccount(accountRequest);

        // Kiểm tra mã phản hồi HTTP
        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        // Kiểm tra kiểu dữ liệu trước khi ép kiểu
        assertTrue(response.getBody() instanceof Account);

        // Chuyển từ Account sang AccountDTO để kiểm tra
        AccountDTO createdAccount = new AccountDTO((Account) response.getBody());

        // Kiểm tra dữ liệu có đúng không
        assertEquals("1", createdAccount.getAccount_id());
    }


    @Test
    void testFindAccounts() {
        when(accountService.findAccounts()).thenReturn(Arrays.asList(account));

        ResponseEntity<?> response = accountController.findAccounts();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        List<AccountDTO> accountDTOS = (List<AccountDTO>) response.getBody();
        assertEquals(1, accountDTOS.size());
        assertEquals("johndoe", accountDTOS.get(0).getUser_name());
    }

    @Test
    void testFindAccountByEmail() {
        try {
            when(accountService.findByEmail("johndoe@example.com")).thenReturn(account);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        ResponseEntity<?> response = accountController.findAccountsByEmail("johndoe@example.com");

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        AccountDTO foundAccount = (AccountDTO) response.getBody();
        assertEquals("johndoe", foundAccount.getUser_name());
    }

    @Test
    void testUpdateAccount() {
        try {
            when(accountService.updateAccount(eq(1), any(AccountRequest.class))).thenReturn(account);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        ResponseEntity<?> response = accountController.updateAccount(1, accountRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        AccountDTO updatedAccount = (AccountDTO) response.getBody();
        assertEquals("johndoe", updatedAccount.getUser_name());
    }

    @Test
    void testChangeStatus() {
        try {
            when(accountService.changeStatus(eq(1), anyString())).thenReturn(account);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        ResponseEntity<?> response = accountController.changeStatus(1, "INACTIVE");

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        AccountDTO changedStatusAccount = (AccountDTO) response.getBody();
        assertEquals("johndoe", changedStatusAccount.getUser_name());
    }

    @Test
    void testFindById() {
        try {
            when(accountService.findById(eq(1))).thenReturn(account);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        ResponseEntity<?> response = accountController.findById(1);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        AccountDTO foundAccount = (AccountDTO) response.getBody();
        assertEquals("johndoe", foundAccount.getUser_name());
    }

    @Test
    void testCheckCoded() {
        try {
            when(accountService.confirmAccount(eq("johndoe@example.com"), eq("123456"))).thenReturn(account);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        ResponseEntity<?> response = accountController.checkCoded("johndoe@example.com", "123456");

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        AccountDTO verifiedAccount = (AccountDTO) response.getBody();
        assertEquals("johndoe", verifiedAccount.getUser_name());
    }

    @Test
    void testChangePassword() throws Exception {
        Account mockAccount = new Account();
        mockAccount.setId(1);
        mockAccount.setEmail("johndoe@example.com");

        when(accountService.changePassword("johndoe@example.com", "oldpassword", "newpassword"))
                .thenReturn(mockAccount); // Mock Account đúng cách

        ResponseEntity<?> response = accountController.changePassword("johndoe@example.com", "oldpassword", "newpassword");

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        // Không ép kiểu trực tiếp, mà tạo AccountDTO từ Account
        AccountDTO responseBody = new AccountDTO((Account) response.getBody());
        assertEquals("johndoe@example.com", responseBody.getEmail());
    }

    @Test
    void testForgotPassword() {
        try {
            when(accountService.forgotPassword("johndoe@example.com")).thenReturn(account);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        ResponseEntity<?> response = accountController.forgotPassword("johndoe@example.com");

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        AccountDTO resetAccount = (AccountDTO) response.getBody();
        assertEquals("johndoe", resetAccount.getUser_name());
    }
}
