package com.midas.app.providers.external.stripe;

import com.midas.app.models.Account;
import com.midas.app.models.ProviderType;
import com.midas.app.providers.payment.CreateAccount;
import com.midas.app.providers.payment.PaymentProvider;
import com.midas.app.services.AccountService;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.param.CustomerCreateParams;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
public class StripePaymentProvider implements PaymentProvider {
  private final Logger logger = LoggerFactory.getLogger(StripePaymentProvider.class);

  private final StripeConfiguration configuration;
  private final AccountService accountService;

  @Autowired
  public StripePaymentProvider(StripeConfiguration configuration, AccountService accountService) {
    this.configuration = configuration;
    this.accountService = accountService;
  }

  /** providerName is the name of the payment provider */
  @Override
  public ProviderType providerName() {
    return ProviderType.STRIPE;
  }

  /**
   * createAccount creates a new account in the payment provider.
   *
   * @param details is the details of the account to be created.
   * @return Account
   */
  @Override
  public Account createProviderAccount(CreateAccount details) throws StripeException {

    // use stripe create customer API
    Customer customer =
        Customer.create(
            CustomerCreateParams.builder()
                .setEmail(details.getEmail())
                .setName(details.getFirstName() + " " + details.getLastName())
                .build());

    logger.info("Customer is created with Id : {}", customer.getId());

    return accountService.createProviderAccount(
        Account.builder()
            .id(details.getUserId())
            .providerId(customer.getId())
            .providerType(providerName())
            .build());
  }
}
