package com.midas.app.activities;

import com.midas.app.models.Account;
import com.midas.app.models.ProviderType;
import com.midas.app.providers.payment.*;
import com.midas.app.providers.payment.CreateAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountActivityImpl implements AccountActivity {

  private final Logger logger = LoggerFactory.getLogger(AccountActivityImpl.class);

  private final PaymentProvider paymentProvider;

  public AccountActivityImpl(PaymentProvider paymentProvider) {
    this.paymentProvider = paymentProvider;
  }

  @Override
  public Account saveAccount(Account account) {
    // Logic to save the account in the data store

    // For demonstration purposes, let's just return the same account
    logger.info("Save account modified.");

    return account;
  }

  @Override
  public Account createPaymentAccount(Account account) {
    // Logic to create a payment account

    String providerType = paymentProvider.providerName();
    String providerId =
        paymentProvider.createAccount(
            CreateAccount.builder()
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .email(account.getEmail())
                .build());

    account.setProviderId(providerId);
    account.setProviderType(Enum.valueOf(ProviderType.class, providerType));

    logger.info("Account Id: " + account.getProviderId());
    return account;
  }
}
