package com.midas.app.services;

import com.midas.app.models.Account;
import com.midas.app.models.UpdateAccountRequest;
import com.midas.app.repositories.AccountRepository;
import com.midas.app.workflows.CreateAccountWorkflow;
import com.midas.app.workflows.CreatePaymentAccountWorkflow;
import com.midas.app.workflows.UpdateAccountWorkflow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.workflow.Workflow;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
  private final Logger logger = Workflow.getLogger(AccountServiceImpl.class);

  private final WorkflowClient workflowClient;
  private final AccountRepository accountRepository;

  @Autowired
  public AccountServiceImpl(WorkflowClient workflowClient, AccountRepository accountRepository) {
    this.workflowClient = workflowClient;
    this.accountRepository = accountRepository;
  }

  /**
   * createAccount creates a new account in the system or provider.
   *
   * @param details is the details of the account to be created.
   * @return Account
   */
  @Override
  public Account createAccount(Account details) {
    var options =
        WorkflowOptions.newBuilder()
            .setTaskQueue(CreateAccountWorkflow.QUEUE_NAME)
            .setWorkflowId(details.getEmail())
            .build();

    logger.info("initiating workflow to create user account for email: {}", details.getEmail());
    var workflow = workflowClient.newWorkflowStub(CreateAccountWorkflow.class, options);

    return workflow.createAccount(details);
  }

  @Override
  public Account createProviderAccount(Account details) {

    var options =
        WorkflowOptions.newBuilder()
            .setTaskQueue(CreatePaymentAccountWorkflow.QUEUE_NAME)
            .setWorkflowId(String.valueOf(details.getProviderId()))
            .build();

    logger.info(
        "initiating workflow to create payment provider account: {}", details.getProviderId());
    var workflow = workflowClient.newWorkflowStub(CreatePaymentAccountWorkflow.class, options);
    return workflow.createPaymentAccount(details);
  }

  @Override
  public Account updateAccount(UUID accountId, UpdateAccountRequest updateRequest) {
    var options =
        WorkflowOptions.newBuilder()
            .setTaskQueue(UpdateAccountWorkflow.QUEUE_NAME)
            .setWorkflowId(String.valueOf(accountId))
            .build();

    logger.info("initiating workflow to update account: {}", accountId);
    var workflow = workflowClient.newWorkflowStub(UpdateAccountWorkflow.class, options);
    return workflow.updateAccount(accountId, updateRequest);
  }

  /**
   * getAccounts returns a list of accounts.
   *
   * @return List<Account>
   */
  @Override
  public List<Account> getAccounts() {
    return accountRepository.findAll();
  }
}
