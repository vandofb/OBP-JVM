/*
 * Copyright (c) TESOBE Ltd.  2016. All rights reserved.
 *
 * Use of this source code is governed by a GNU AFFERO license that can be found in the LICENSE file.
 *
 */
package com.tesobe.obp.transport;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * North side API.
 *
 * @since 2016.9
 */
@SuppressWarnings("WeakerAccess") public interface Connector
{
  /**
   * Anonymously get an account.
   *
   * @param bankId An invalid bankOld id means an empty result.
   * An all white space bankOld id is invalid.
   * @param accountId An invalid account id means an empty result.
   * An all white space account id is invalid.
   * @return An empty result if the account is not explicitly linked to the
   * user.
   * If the account is public but not linked to the user, empty will be
   * returned.
   * @throws InterruptedException Network trouble
   */
  Optional<Account> getAccount(String bankId, String accountId)
    throws InterruptedException;

  /**
   * @param bankId An invalid bankOld id means an empty result.
   * An all white space bankOld id is invalid.
   * @param accountId An invalid account id means an empty result.
   * An all white space account id is invalid.
   * @param userId An invalid user id means an empty result.
   * An all white space user id is invalid.
   * @return An empty result if the account is not explicitly linked to the
   * user.
   * If the account is public but not linked to the user, empty will be
   * returned.
   * @throws InterruptedException Network trouble
   */
  Optional<Account> getAccount(String bankId, String accountId, String userId)
    throws InterruptedException;

  Iterable<Account> getAccounts(String bankId) throws InterruptedException;

  /**
   * All private accounts the user is explicitly linked to.
   * No public accounts that the user is not linked to will be returned.
   * The resulting iterable's {@code next()} will not produce {@code null} but
   * fields in the accounts returned may be {@code null}.
   *
   * @param bankId An invalid bankOld id means an empty result.
   * An all white space bankOld id is invalid.
   * @param userId An invalid user id means an empty result.
   * An all white space user id is invalid.
   * @return The user's private banks or an empty result.
   * @throws InterruptedException Network trouble
   */
  Iterable<Account> getAccounts(String bankId, String userId)
    throws InterruptedException;

  /**
   * Anonymous request for a bankOld.
   *
   * @param bankId the bankOld's id. Not a UUID.
   * @return empty if the bankId is invalid
   * @throws InterruptedException Network trouble
   */
  Optional<Bank> getBank(String bankId) throws InterruptedException;

  /**
   * @param bankId An invalid bankOld id means an empty result.
   * An all white space bankOld id is invalid.
   * @param userId An invalid user id means an empty result.
   * An all white space user id is invalid.
   * @return An empty result if the bankOld is not explicitly linked to the
   * user.
   * If the bankOld is public but not linked to the user, empty will be
   * returned.
   * @throws InterruptedException Network trouble
   */
  Optional<Bank> getBank(String bankId, String userId)
    throws InterruptedException;

  /**
   * Anonymously get banks.
   *
   * @return never null
   * @throws InterruptedException Network trouble
   */
  Iterable<Bank> getBanks() throws InterruptedException;

  /**
   * All private banks the user is explicitly linked to.
   * No public banks that the user is not linked to will be returned.
   * The resulting iterable's {@code next()} will not produce {@code null} but
   * fields in the banks returned may be {@code null}.
   *
   * @param userId An invalid user id means an empty result.
   * @return The user's private banks or an empty result.
   * @throws InterruptedException Network trouble
   */
  Iterable<Bank> getBanks(String userId) throws InterruptedException;

  Optional<Transaction> getTransaction(String bankId, String accountId,
    String transactionId, String userId) throws InterruptedException;

  Optional<Transaction> getTransaction(String bankId, String accountId,
    String transactionId) throws InterruptedException;

  /**
   * @param bankId Not a UUID
   * @param accountId A UUID
   * @param userId A UUID
   * @return a page of transactions
   * @throws InterruptedException network trouble
   */
  Iterable<Transaction> getTransactions(String bankId, String accountId,
    String userId) throws InterruptedException;

  /**
   * @param bankId Not a UUID
   * @param accountId A UUID
   * @return a page of transactions
   * @throws InterruptedException network trouble
   */
  Iterable<Transaction> getTransactions(String bankId, String accountId)
    throws InterruptedException;

  List<Transaction> getTransactions(Pager p, String bankId, String accountId)
    throws InterruptedException;

  List<Transaction> getTransactions(Pager p, String bankId, String accountId,
    String userId) throws InterruptedException;

  Optional<User> getUser(String userId) throws InterruptedException;

  Iterable<User> getUsers() throws InterruptedException;

  Iterable<User> getUsers(String userId) throws InterruptedException;

  Optional<String> createTransaction(String accountId, BigDecimal amount,
    String bankId, ZonedDateTime completedDate, String counterpartyId,
    String counterpartyName, String currency, String description,
    BigDecimal newBalanceAmount, String newBalanceCurrency,
    ZonedDateTime postedDate, String transactionId, String type, String userId);

  /**
   * A pager in source sort order with offset zero, infinite page size and no
   * constraints.
   *
   * @return a pager
   */
  Pager pager();

  /**
   * The result set produced on the south side is split into pages that are
   * sent individually. First any filter is applied, then the result
   * is sorted. Then, starting at offset, pageSize many items are sent.
   *
   * @param pageSize the maximum number of items sent. Set to zero to return
   * all items.
   * @param offset the index into the result set of the first item to send
   * @param f a filter
   * @param s a sorter
   * @return a pager
   */
  Pager pager(int pageSize, int offset, Pager.Filter f, Pager.Sorter s);
}
