package com.midas.app.workflows.Impl;

import com.midas.app.activities.AccountActivity;
import com.midas.app.models.Account;
import com.midas.app.workflows.CreatePaymentAccountWorkflow;
import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import java.time.Duration;

public class CreatePaymentAccountWorkflowImpl implements CreatePaymentAccountWorkflow {

  ActivityOptions options =
      ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(60)).build();

  private final AccountActivity activity = Workflow.newActivityStub(AccountActivity.class, options);

  @Override
  public Account createPaymentAccount(Account details) {
    return activity.createPaymentAccount(details);
  }
}
