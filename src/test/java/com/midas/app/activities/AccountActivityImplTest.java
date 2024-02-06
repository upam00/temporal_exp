import static org.junit.jupiter.api.Assertions.assertEquals;

import com.midas.app.activities.AccountActivityImpl;
import com.midas.app.models.Account;
import com.midas.app.providers.*;
import com.midas.app.providers.payment.CreateAccount;
import com.midas.app.providers.payment.PaymentProvider;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class AccountActivityImplTest {

  @Test
  public void testSaveAccount() {
    // Create a sample account
    Account account =
        Account.builder()
            .id(UUID.randomUUID())
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@example.com")
            .createdAt(OffsetDateTime.now())
            .updatedAt(OffsetDateTime.now())
            .providerId(null)
            .providerType(null)
            .build();

    AccountActivityImpl accountActivity = new AccountActivityImpl(new MockPaymentProvider());
    Account savedAccount = accountActivity.saveAccount(account);
    assertEquals(account, savedAccount);
  }

  // MockPaymentProvider class to mock PaymentProvider dependency
  static class MockPaymentProvider implements PaymentProvider {
    @Override
    public String providerName() {
      return "MockProvider";
    }

    // Implement other methods if needed for the test case

    @Override
    public String createAccount(CreateAccount details) {
      return "SomeAccountId";
    }
  }
}
