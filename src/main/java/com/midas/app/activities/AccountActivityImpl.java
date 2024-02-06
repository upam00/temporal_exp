package com.midas.app.activities;

import com.midas.app.models.Account;
import com.midas.app.models.ProviderType;
import com.midas.app.providers.payment.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountActivityImpl implements AccountActivity {

  private final Logger logger = LoggerFactory.getLogger(AccountActivityImpl.class);

  private final PaymentProvider paymentProvider;
  private static final String DB_PROPERTIES_FILE = "application.properties";
  private Properties properties;

  public AccountActivityImpl(PaymentProvider paymentProvider) {
    this.paymentProvider = paymentProvider;
    properties = new Properties();
    try (InputStream inputStream =
        getClass().getClassLoader().getResourceAsStream(DB_PROPERTIES_FILE)) {
      properties.load(inputStream);
    } catch (IOException e) {
      throw new RuntimeException("Failed to load database properties", e);
    }
  }

  // Remove these credentials and take it from application.properties
  // private static final String DB_URL = "jdbc:postgresql://localhost:5432/mydatabase";
  // private static final String DB_USERNAME = "";
  // private static final String DB_PASSWORD = "password"

  private Connection getConnection() throws SQLException {
    return DriverManager.getConnection(
        properties.getProperty("db.url"),
        properties.getProperty("db.username"),
        properties.getProperty("db.password"));
  }

  // // JDBC connection
  // private Connection getConnection() throws SQLException {
  //     return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
  // }

  @Override
  public Account saveAccount(Account account) {
    // Logic to save the account in the data store
    try (Connection conn = getConnection()) {
      String sql =
          "INSERT INTO accounts (id, first_name, last_name, email, created_at, updated_at, provider_id, provider_type) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
      try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        // pstmt.setObject(1, account.getId());
        UUID uuid = UUID.randomUUID();
        account.setId(uuid);
        pstmt.setObject(1, uuid);
        pstmt.setString(2, account.getFirstName());
        pstmt.setString(3, account.getLastName());
        pstmt.setString(4, account.getEmail());
        pstmt.setObject(5, account.getCreatedAt());
        pstmt.setObject(6, account.getUpdatedAt());
        pstmt.setString(7, account.getProviderId());
        pstmt.setString(8, account.getProviderType().toString());
        pstmt.executeUpdate();
      }
    } catch (SQLException e) {
      throw new RuntimeException("Failed to store account in the database", e);
    }
    logger.info("Account save in db.");
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
