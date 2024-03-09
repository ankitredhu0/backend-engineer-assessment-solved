package com.midas.app.workflows;

import com.midas.app.models.Account;
import com.midas.app.models.UpdateAccountRequest;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import java.util.UUID;

@WorkflowInterface
public interface UpdateAccountWorkflow {
  String QUEUE_NAME = "update-account-workflow";

  @WorkflowMethod
  Account updateAccount(UUID accountId, UpdateAccountRequest updateRequest);
}
