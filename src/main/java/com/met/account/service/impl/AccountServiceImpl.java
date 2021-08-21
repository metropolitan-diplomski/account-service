package com.met.account.service.impl;

import com.met.account.api.AuthServiceApi;
import com.met.account.document.Account;
import com.met.account.dto.AccountMapper;
import com.met.account.dto.request.ChangeBalanceRequest;
import com.met.account.dto.request.CreateAccountRequest;
import com.met.account.dto.response.AccountResponse;
import com.met.account.dto.response.ExecuteTransactionResponse;
import com.met.account.dto.response.UserResponse;
import com.met.account.exception.AccountServiceException;
import com.met.account.exception.ErrorCode;
import com.met.account.repository.AccountRepository;
import com.met.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AuthServiceApi authServiceApi;

    @Override
    public AccountResponse createAccount(CreateAccountRequest request) {
        boolean unique = false;
        Long accountNumber = null;
        while(!unique) {
            accountNumber = generateAccountNumber();
            if (accountRepository.findByAccountNumber(accountNumber).isEmpty()) {
                unique = true;
            }
        }

        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setBalance(0.0);
        account.setCurrency(request.getCurrency());
        account.setName(request.getName());
        account.setUserId(request.getUserId());



        return AccountMapper.mapEntityToResponse(accountRepository.save(account));
    }

    @Override
    public List<AccountResponse> getAllAccountsForUser(String userId, String token) {
        List<Account> accounts = accountRepository.findAllByUserId(userId);
        List<AccountResponse> accountResponses = new ArrayList<>();
        UserResponse user = authServiceApi.getUserById(userId, token);
        if (user != null) {
            for (Account account : accounts) {
                AccountResponse response = AccountMapper.mapEntityToResponse(account);
                response.setUserFullName(user.getFullName());
                accountResponses.add(response);
            }
        } else {
            accountResponses = accounts.stream()
                    .map(AccountMapper::mapEntityToResponse)
                    .collect(Collectors.toList());
        }

        return accountResponses;
    }

    @Override
    public AccountResponse getAccountByAccountNumber(Long accountNumber, String token) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountServiceException(ErrorCode.NOT_FOUND, "Account with that acccount number not found"));
        UserResponse user = authServiceApi.getUserById(account.getUserId(), token);
        AccountResponse accountResponse = AccountMapper.mapEntityToResponse(account);
        if (user != null) {
            accountResponse.setUserFullName(user.getFullName());
        }

        return accountResponse;
    }

    @Override
    public AccountResponse getAccountById(String id, String token) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountServiceException(ErrorCode.NOT_FOUND, "Account with that account number not found"));
        UserResponse user = authServiceApi.getUserById(account.getUserId(), token);
        AccountResponse accountResponse = AccountMapper.mapEntityToResponse(account);
        if (user != null) {
            accountResponse.setUserFullName(user.getFullName());
        }

        return accountResponse;
    }

    @Override
    public ExecuteTransactionResponse changeBalance(ChangeBalanceRequest request) {
        ExecuteTransactionResponse transactionResponse = new ExecuteTransactionResponse();
        transactionResponse.setSuccess(true);
        Account from = accountRepository.findByAccountNumber(request.getAccountFrom()).orElse(null);
        Account to = accountRepository.findByAccountNumber(request.getAccountTo()).orElse(null);

        // If add then to must have enough balance if subtract then to
        if (from != null && to != null) {
            transactionResponse.setFromId(from.getId());
            transactionResponse.setToId(to.getId());
            Double result = from.getBalance() - request.getAmount();
            if (result < 0) {
                transactionResponse.setSuccess(false);
                transactionResponse.setErrorMessage("Not enough funds in account for this transaction");
            } else {
                transactionResponse.setSuccess(true);
                from.setBalance(result);
                to.setBalance(to.getBalance() + request.getAmount());
            }
        } else if (from != null) {
            transactionResponse.setFromId(from.getUserId());
            Double result = from.getBalance() - request.getAmount();
            if (result < 0) {
                transactionResponse.setSuccess(false);
                transactionResponse.setErrorMessage("Not enough funds in account for this transaction");
            } else {
                transactionResponse.setSuccess(true);
                from.setBalance(result);
            }
        } else if (to != null) {
            transactionResponse.setToId(to.getUserId());
            to.setBalance(to.getBalance() + request.getAmount());
        }

        if (to != null) {
            accountRepository.save(to);
        }

        if (from != null) {
            accountRepository.save(from);
        }

        return transactionResponse;
    }

    @Override
    public void deleteAccount(String id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountServiceException(ErrorCode.NOT_FOUND, "Account with that account number not found"));

        accountRepository.delete(account);
    }

    private Long generateAccountNumber() {
        StringBuilder accountBuilder = new StringBuilder();
        accountBuilder.append("320");
        Random r = new Random();
        int low = 100000;
        int high = 999999;
        int middle = r.nextInt(high-low) + low;
        accountBuilder.append(middle);
        low = 100;
        high = 999;
        int end = r.nextInt(high-low) + low;
        accountBuilder.append(end);

        return Long.valueOf(accountBuilder.toString());
    }
}
