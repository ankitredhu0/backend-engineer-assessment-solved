package com.midas.app.workflows;

import com.midas.app.models.Account;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface CreatePaymentAccountWorkflow {
  String QUEUE_NAME = "create-payment-account-workflow";

  @WorkflowMethod
  Account createPaymentAccount(Account details);
}
