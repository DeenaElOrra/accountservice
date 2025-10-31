package store.account;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class AccountServiceDeleteTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.14")
            .withDatabaseName("storedb")
            .withUsername("postgres")
            .withPassword("postgres");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.flyway.schemas", () -> "account");
        registry.add("spring.flyway.create-schemas", () -> "true");
    }

    @Autowired
    private AccountService accountService;

    @Test
    public void testDelete() {
        // Create account
        Account account = new Account(null, "Test User", "test@test.com", "password123", null);
        Account created = accountService.create(account);

        assertNotNull(created.id());

        // Verify it exists
        Account found = accountService.findById(created.id());
        assertNotNull(found);

        // Delete it
        accountService.delete(created.id());

        // Verify it's deleted
        Account deleted = accountService.findById(created.id());
        assertNull(deleted);
    }
}
