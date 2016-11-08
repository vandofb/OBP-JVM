/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport.spi;

import com.tesobe.obp.transport.Account;
import com.tesobe.obp.transport.Bank;
import com.tesobe.obp.transport.Connector;
import com.tesobe.obp.transport.Sender;
import com.tesobe.obp.transport.Token;
import com.tesobe.obp.transport.Transaction;
import com.tesobe.obp.transport.Transport;
import com.tesobe.obp.transport.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import static com.tesobe.obp.transport.spi.Network.Target.banks;

/**
 * Compatible to mid 2016 OBP-API.
 *
 * @since 2016.9
 */
@SuppressWarnings("WeakerAccess") public class ConnectorNov2016
  implements Connector
{
  public ConnectorNov2016(Transport.Version v, Encoder e, Decoder d, Sender s)
  {
    decoder = d;
    encoder = e;
    network = new Network(v, e, d, s); // todo mk param
    sender = s;
    version = v;
  }

  @Override public Optional<Account> getAccount(String bankId, String accountId,
    String userId) throws InterruptedException
  {
    Decoder.Response<Account> response = network
      .session()
      .get("getAccounts", Network.Target.account, Account.class, userId, bankId,
        accountId, null);
    Iterator<Account> account = response.data().iterator();

    return account.hasNext() ? Optional.of(account.next()) : Optional.empty();
  }

  @Override public Optional<Account> getAccount(String bankId, String accountId)
    throws InterruptedException
  {
    Decoder.Response<Account> response = network
      .session()
      .get("getAccounts", Network.Target.account, Account.class, null, bankId,
        accountId, null);
    Iterator<Account> account = response.data().iterator();

    return account.hasNext() ? Optional.of(account.next()) : Optional.empty();
  }

  @Override
  public Optional<Transaction> getTransaction(String bankId, String accountId,
    String transactionId, String userId) throws InterruptedException
  {
    Decoder.Response<Transaction> response = network
      .session()
      .get("getTransaction", Network.Target.transaction, Transaction.class,
        userId, bankId, accountId, transactionId);
    Iterator<Transaction> transaction = response.data().iterator();

    return transaction.hasNext()
      ? Optional.of(transaction.next())
      : Optional.empty();
  }

  @Override
  public Iterable<Transaction> getTransactions(String bankId, String accountId,
    String userId) throws InterruptedException
  {
    Decoder.Response<Transaction> response = network
      .session()
      .get("getTransactions", Network.Target.transactions, Transaction.class,
        userId, bankId, accountId, null);

    return response.data();

  }

  @Override public Iterable<Account> getAccounts(String bankId)
    throws InterruptedException
  {
    Decoder.Response<Account> response = network
      .session()
      .get("getAccounts", Network.Target.accounts, Account.class, null, bankId,
        null, null);

    return response.data();
  }


  @Override public Iterable<Account> getAccounts(String bankId, String userId)
    throws InterruptedException
  {
    Decoder.Response<Account> response = network
      .session()
      .get("getAccounts", Network.Target.accounts, Account.class, userId,
        bankId, null, null);

    return response.data();
  }

  @Override public Optional<Bank> getBank(String bankId)
    throws InterruptedException
  {
    Decoder.Response<Bank> response = network
      .session()
      .get("getBank", Network.Target.bank, Bank.class, null, bankId, null,
        null);
    Iterator<Bank> banks = response.data().iterator();

    return banks.hasNext() ? Optional.of(banks.next()) : Optional.empty();
  }

  @Override public Optional<Bank> getBank(String bankId, String userId)
    throws InterruptedException
  {
    Decoder.Response<Bank> response = network
      .session()
      .get("getBank", Network.Target.bank, Bank.class, userId, bankId, null,
        null);
    Iterator<Bank> banks = response.data().iterator();

    return banks.hasNext() ? Optional.of(banks.next()) : Optional.empty();
  }

  @Override public Iterable<Bank> getBanks() throws InterruptedException
  {
    Decoder.Response<Bank> response = network
      .session()
      .get("getBanks", banks, Bank.class, null, null, null, null);

    return response.data();
  }

  @Override public Iterable<Bank> getBanks(String userId)
    throws InterruptedException
  {
    Decoder.Response<Bank> response = network
      .session()
      .get("getBanks", banks, Bank.class, userId, null, null, null);

    return response.data();
  }

  @Override
  public Optional<Transaction> getTransaction(String bankId, String accountId,
    String transactionId) throws InterruptedException
  {
    Decoder.Response<Transaction> response = network
      .session()
      .get("getTransaction", Network.Target.transaction, Transaction.class,
        null, bankId, accountId, transactionId);
    Iterator<Transaction> transaction = response.data().iterator();

    return transaction.hasNext()
      ? Optional.of(transaction.next())
      : Optional.empty();
  }

  @Override
  public Iterable<Transaction> getTransactions(String bankId, String accountId)
    throws InterruptedException
  {
    Decoder.Response<Transaction> response = network
      .session()
      .get("getTransactions", Network.Target.transactions, Transaction.class,
        null, bankId, accountId, null);

    return response.data();
  }

  @Override public Optional<User> getUser(String userId)
    throws InterruptedException
  {
    Decoder.Response<User> response = network
      .session()
      .get("getUser", Network.Target.user, User.class, userId, null, null,
        null);
    Iterator<User> user = response.data().iterator();

    return user.hasNext() ? Optional.of(user.next()) : Optional.empty();
  }

  @Override public Iterable<User> getUsers() throws InterruptedException
  {
    Decoder.Response<User> response = network
      .session()
      .get("getUsers", Network.Target.users, User.class, null, null, null,
        null);

    return response.data();
  }

  @Override public Iterable<User> getUsers(String userId)
    throws InterruptedException
  {
    Decoder.Response<User> response = network
      .session()
      .get("getUsers", Network.Target.users, User.class, userId, null, null,
        null);

    return response.data();
  }

  @Override
  public Optional<String> saveTransaction(String userId, String accountId,
    String currency, String amount, String otherAccountId,
    String otherAccountCurrency, String transactionType)
    throws InterruptedException
  {
    Map<String, String> fields = new HashMap<>();

    fields.put("user", userId);
    fields.put("account", accountId);
    fields.put("currency", currency);
    fields.put("amount", amount);
    fields.put("otherId", otherAccountId);
    fields.put("otherCurrency", otherAccountCurrency);
    fields.put("transactionType", transactionType);

    Optional<Token> result = network
      .session()
      .put("saveTransaction", Network.Target.transaction, Token.class, fields);

    return result.map(Token::id);
  }

  protected static final Logger log = LoggerFactory.getLogger(
    ConnectorNov2016.class);
  protected final Decoder decoder;
  protected final Encoder encoder;
  protected final Network network;
  protected final Transport.Version version;
  protected final Sender sender;
}