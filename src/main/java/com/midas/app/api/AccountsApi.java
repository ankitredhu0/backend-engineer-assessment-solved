package com.midas.app.api;

import com.midas.app.models.AccountDto;
import com.midas.app.models.CreateAccountDto;
import com.midas.app.models.UpdateAccountRequest;
import com.midas.app.providers.payment.CreateAccount;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface AccountsApi {

  /**
   * POST /accounts : Create a new user account Creates a new user account with the given details
   * and attaches a supported payment provider such as 'stripe'.
   *
   * @param createAccountDto User account details (required)
   * @return User account created (status code 201)
   */
  @PostMapping("/accounts")
  ResponseEntity<AccountDto> createUserAccount(CreateAccountDto createAccountDto);

  @PostMapping("/provider/accounts")
  ResponseEntity<AccountDto> createProviderAccount(CreateAccount createAccountDto);

  @PatchMapping("/accounts/{accountId}")
  public ResponseEntity<AccountDto> updateAccount(
      @PathVariable("accountId") UUID accountId, @RequestBody UpdateAccountRequest updateRequest);

  /**
   * GET /accounts : Get list of user accounts Returns a list of user accounts.
   *
   * @return List of user accounts (status code 200)
   */
  @GetMapping("/accounts")
  ResponseEntity<List<AccountDto>> getUserAccounts();
}
