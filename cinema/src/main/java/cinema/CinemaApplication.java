package cinema;

import cinema.modal.entity.Account;
import cinema.modal.entity.constant.Gender;
import cinema.modal.entity.constant.Role;
import cinema.modal.entity.constant.StatusAccount;
import cinema.repository.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDate;

@SpringBootApplication
@EnableScheduling
public class CinemaApplication {

    public static void main(String[] args) {
        SpringApplication.run(CinemaApplication.class, args);
    }

    @Bean
    CommandLineRunner initAdmin(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            Account admin = new Account();
            admin.setUsername("admin");
            admin.setFullName("Administrator");
            admin.setEmail("admin@cinema.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            admin.setStatus(StatusAccount.ACTIVE);
            admin.setGender(Gender.MALE);
            admin.setBirthDate(LocalDate.of(2000, 1, 1));
            admin.setPhoneNumber("0123456789");
            admin.setCity("Hanoi");
            admin.setDistrict("Ba Dinh");
            admin.setAddress("123 Main Street");

            accountRepository.save(admin);
            System.out.println("Admin account created: " + admin.getUsername());
        };
    }
}
