package com.midas.app.activities;

import com.midas.app.exceptions.*;
import com.midas.app.models.Account;
import com.midas.app.models.ProviderType;
import com.midas.app.models.UpdateAccountRequest;
import com.midas.app.repositories.AccountRepository;
import io.temporal.workflow.Workflow;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class AccountActivityImpl implements AccountActivity {

  private final Logger logger = Workflow.getLogger(AccountActivityImpl.class);

  private final AccountRepository accountRepository;

  @Autowired
  public AccountActivityImpl(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @Override
  public Account saveAccount(Account account) {
    try {
      // Save the created account using JPA repository
      accountRepository.save(account);

    } catch (ResourceAlreadyExistsException e) {
      logger.error("Resource already exists {} ", account.getId());
    }
    return account;
  }

  @Override
  public Account createPaymentAccount(Account details) {
    try {

      Account account = accountRepository.findById(details.getId()).orElse(null);

      if (account != null) {
        account.setProviderId(details.getProviderId());
        account.setProviderType(ProviderType.STRIPE);
        account.setUpdatedAt(OffsetDateTime.now());
        accountRepository.save(account);
      }

    } catch (ResourceAlreadyExistsException e) {
      logger.error("Resource already exists {} ", details.getProviderId());
    }
    return details;
  }

  @Override
  public Account updateAccount(UUID accountId, UpdateAccountRequest updateRequest) {
    try {
      Account account = accountRepository.findById(accountId).orElse(null);

      if (account == null) {
        throw new ResourceNotFoundException(); // Account not found
      }

      // Update the account details
      if (updateRequest.getFirstName() != null) {
        account.setFirstName(updateRequest.getFirstName());
      }
      if (updateRequest.getLastName() != null) {
        account.setLastName(updateRequest.getLastName());
      }
      if (updateRequest.getEmail() != null) {
        account.setEmail(updateRequest.getEmail());
      }
      return accountRepository.save(account);
    } catch (AccessDeniedException | UnauthorizedException e) {
      logger.error("Error in updating Account {} ", e.getMessage());
      return null;
    }
  }
}
