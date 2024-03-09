package com.midas.app.workflows.Impl;

import com.midas.app.activities.AccountActivity;
import com.midas.app.models.Account;
import com.midas.app.models.UpdateAccountRequest;
import com.midas.app.workflows.UpdateAccountWorkflow;
import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import java.time.Duration;
import java.util.UUID;

public class UpdateAccountWorkflowImpl implements UpdateAccountWorkflow {

  ActivityOptions options =
      ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(60)).build();

  private final AccountActivity activity = Workflow.newActivityStub(AccountActivity.class, options);

  @Override
  public Account updateAccount(UUID accountId, UpdateAccountRequest updateRequest) {
    return activity.updateAccount(accountId, updateRequest);
  }
}
