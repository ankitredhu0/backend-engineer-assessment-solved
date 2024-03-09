package com.midas.app.controllers;

import com.midas.app.api.AccountsApi;
import com.midas.app.exceptions.AccessDeniedException;
import com.midas.app.exceptions.InvalidRequestException;
import com.midas.app.exceptions.ResourceAlreadyExistsException;
import com.midas.app.exceptions.UnauthorizedException;
import com.midas.app.mappers.Mapper;
import com.midas.app.models.*;
import com.midas.app.providers.payment.CreateAccount;
import com.midas.app.providers.payment.PaymentProvider;
import com.midas.app.services.AccountService;
import com.stripe.exception.StripeException;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class AccountController implements AccountsApi {
  private final AccountService accountService;

  private final PaymentProvider paymentProvider;
  private final Logger logger = LoggerFactory.getLogger(AccountController.class);

  @Autowired
  public AccountController(AccountService accountService, PaymentProvider paymentProvider) {
    this.accountService = accountService;
    this.paymentProvider = paymentProvider;
  }

  /**
   * POST /accounts : Create a new user account Creates a new user account with the given details
   * and attaches a supported payment provider such as &#39;stripe&#39;.
   *
   * @param createAccountDto User account details (required)
   * @return User account created (status code 201)
   */
  @Override
  public ResponseEntity<AccountDto> createUserAccount(
      @RequestBody CreateAccountDto createAccountDto) {
    logger.info("Creating account for user with email: {}", createAccountDto.getEmail());
    logger.info("Coming request for Account creation is : {}", createAccountDto);

    try {
      var account =
          accountService.createAccount(
              Account.builder()
                  .firstName(createAccountDto.getFirstName())
                  .lastName(createAccountDto.getLastName())
                  .email(createAccountDto.getEmail())
                  .providerType(ProviderType.STRIPE)
                  .build());

      return new ResponseEntity<>(Mapper.toAccountDto(account), HttpStatus.CREATED);
    } catch (AccessDeniedException e) {
      logger.error("Getting Error in creating Account {} ", e.getMessage(), e);
      return new ResponseEntity<>(
          Mapper.toAccountDto(
              Account.builder()
                  .firstName(createAccountDto.getFirstName())
                  .lastName(createAccountDto.getLastName())
                  .email(createAccountDto.getEmail())
                  .build()),
          HttpStatus.BAD_REQUEST);
    } catch (InvalidRequestException e) {
      logger.error("Invalid request {} ", e.getMessage(), e);
      return new ResponseEntity<>(
          Mapper.toAccountDto(
              Account.builder()
                  .firstName(createAccountDto.getFirstName())
                  .lastName(createAccountDto.getLastName())
                  .email(createAccountDto.getEmail())
                  .build()),
          HttpStatus.BAD_REQUEST);
    }
  }

  @Override
  public ResponseEntity<AccountDto> createProviderAccount(CreateAccount createAccountDto) {
    logger.info("Creating Provider account for user with userId: {}", createAccountDto.getUserId());

    try {
      var account = paymentProvider.createProviderAccount(createAccountDto);

      return new ResponseEntity<>(Mapper.toAccountDto(account), HttpStatus.CREATED);
    } catch (AccessDeniedException | StripeException e) {
      logger.error("Getting Error in creating Account {} ", e.getMessage(), e);
      return new ResponseEntity<>(
          Mapper.toAccountDto(
              Account.builder()
                  .firstName(createAccountDto.getFirstName())
                  .lastName(createAccountDto.getLastName())
                  .email(createAccountDto.getEmail())
                  .build()),
          HttpStatus.BAD_REQUEST);
    } catch (InvalidRequestException | ResourceAlreadyExistsException e) {
      logger.error("Invalid request {} ", e.getMessage(), e);
      return new ResponseEntity<>(
          Mapper.toAccountDto(
              Account.builder()
                  .firstName(createAccountDto.getFirstName())
                  .lastName(createAccountDto.getLastName())
                  .email(createAccountDto.getEmail())
                  .build()),
          HttpStatus.BAD_REQUEST);
    }
  }

  @Override
  public ResponseEntity<AccountDto> updateAccount(
      @PathVariable("accountId") UUID accountId, @RequestBody UpdateAccountRequest updateRequest) {
    Account updatedAccount = accountService.updateAccount(accountId, updateRequest);
    if (updatedAccount != null) {
      AccountDto accountDto = Mapper.toAccountDto(updatedAccount);
      return ResponseEntity.ok(accountDto);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * GET /accounts : Get list of user accounts Returns a list of user accounts.
   *
   * @return List of user accounts (status code 200)
   */
  @Override
  public ResponseEntity<List<AccountDto>> getUserAccounts() {
    logger.info("Retrieving all accounts");
    try {
      var accounts = accountService.getAccounts();
      var accountsDto = accounts.stream().map(Mapper::toAccountDto).toList();
      return new ResponseEntity<>(accountsDto, HttpStatus.OK);
    } catch (UnauthorizedException e) {
      logger.error("Error while fetching Accounts {} ", e.getMessage(), e);
      return null;
    }
  }
}
