package com.midas.app.providers.external.stripe;

import com.midas.app.providers.payment.CreateAccount;
import com.midas.app.providers.payment.PaymentProvider;
import com.stripe.Stripe;
import com.stripe.model.Customer;
import com.stripe.param.CustomerCreateParams;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Getter
public class StripePaymentProvider implements PaymentProvider {
  private final Logger logger = LoggerFactory.getLogger(StripePaymentProvider.class);

  private final StripeConfiguration configuration;

  /** providerName is the name of the payment provider */
  @Override
  public String providerName() {
    return "stripe";
  }

  /**
   * createAccount creates a new account in the payment provider.
   *
   * @param details is the details of the account to be created.
   * @return Account
   */
  @Override
  public String createAccount(CreateAccount details) {
    Stripe.apiKey = configuration.getApiKey();
    try {
      Customer customer =
          Customer.create(
              new CustomerCreateParams.Builder()
                  .setEmail(details.getEmail())
                  .setName(details.getFirstName() + details.getLastName())
                  .build());
      // Retrieve the customer ID
      return customer.getId();
    } catch (Exception e) {
      e.printStackTrace();
      throw new UnsupportedOperationException("Not implemented");
    }
  }
}
