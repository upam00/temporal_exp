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
    // Save the account
    Account savedAccount = accountActivity.saveAccount(details);

    // Create a payment account
    Account paymentAccount = accountActivity.createPaymentAccount(savedAccount);

    return paymentAccount;
  }
}
