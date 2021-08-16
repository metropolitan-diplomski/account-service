package com.met.account.service;

import com.met.account.document.Account;
import com.met.account.dto.request.ChangeBalanceRequest;
import com.met.account.dto.request.CreateAccountRequest;
import com.met.account.dto.response.AccountResponse;
import com.met.account.dto.response.ExecuteTransactionResponse;

import java.util.List;

public interface AccountService {
    AccountResponse createAccount(CreateAccountRequest request);
    List<AccountResponse> getAllAccountsForUser(String userId, String token);
    AccountResponse getAccountByAccountNumber(Long accountNumber, String token);
    AccountResponse getAccountById(String id, String token);
    ExecuteTransactionResponse changeBalance(ChangeBalanceRequest request);

}
