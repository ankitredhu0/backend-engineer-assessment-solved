package com.midas.app.workflows.Impl;

import com.midas.app.activities.AccountActivity;
import com.midas.app.models.Account;
import com.midas.app.workflows.CreateAccountWorkflow;
import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import java.time.Duration;

public class CreateAccountWorkflowImpl implements CreateAccountWorkflow {

  ActivityOptions options =
      ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(60)).build();

  private final AccountActivity activity = Workflow.newActivityStub(AccountActivity.class, options);

  @Override
  public Account createAccount(Account details) {
    return activity.saveAccount(details);
  }
}
