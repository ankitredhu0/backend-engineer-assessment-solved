package com.midas.app.providers.payment;

import com.midas.app.models.Account;
import com.midas.app.models.ProviderType;
import com.stripe.exception.StripeException;

public interface PaymentProvider {
  /** providerName is the name of the payment provider */
  ProviderType providerName();

  /**
   * createAccount creates a new account in the payment provider.
   *
   * @param details is the details of the account to be created.
   * @return Account
   */
  Account createProviderAccount(CreateAccount details) throws StripeException;
}
