package com.midas.app.activities;

import com.midas.app.models.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountActivityImpl implements AccountActivity {

  private final Logger logger = LoggerFactory.getLogger(AccountActivityImpl.class);

  @Override
  public Account saveAccount(Account account) {
    // Logic to save the account in the data store
    // For demonstration purposes, let's just return the same account
    logger.info("Save account");
    return account;
  }

  @Override
  public Account createPaymentAccount(Account account) {
    // Logic to create a payment account
    // For demonstration purposes, let's just return the same account
    logger.info("patment account");
    return account;
  }
}
