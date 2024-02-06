package com.midas.app.workflows;

import com.midas.app.activities.AccountActivity;
import com.midas.app.models.Account;
import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import java.time.Duration;

public class CreateAccountWorkflowImpl implements CreateAccountWorkflow {

  private final AccountActivity accountActivity =
      Workflow.newActivityStub(
          AccountActivity.class,
          ActivityOptions.newBuilder()
              .setStartToCloseTimeout(Duration.ofSeconds(60))
              .setTaskQueue(CreateAccountWorkflow.QUEUE_NAME)
              .build());

  @Override
  public Account createAccount(Account details) {

    // Create a payment account to get back the providerType and providerId
    Account paymentAccount = accountActivity.createPaymentAccount(details);

    // Save the account with providerType and providerId
    Account savedAccount = accountActivity.saveAccount(paymentAccount);

    return savedAccount;
  }
}
