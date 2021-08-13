package com.met.account.service.impl;

import com.met.account.api.AuthServiceApi;
import com.met.account.document.Account;
import com.met.account.dto.AccountMapper;
import com.met.account.dto.ChangeBalanceType;
import com.met.account.dto.request.ChangeBalanceRequest;
import com.met.account.dto.request.CreateAccountRequest;
import com.met.account.dto.response.AccountResponse;
import com.met.account.dto.response.UserResponse;
import com.met.account.exception.AccountServiceException;
import com.met.account.exception.ErrorCode;
import com.met.account.repository.AccountRepository;
import com.met.account.service.AccountService;
import org.apache.catalina.User;
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

        return AccountMapper.mapEntityToResponse(account);
    }

    @Override
    public List<AccountResponse> getAllAccountsForUser(String userId, String token) {
        List<Account> accounts = accountRepository.findByUserId(userId);
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
        UserResponse user = authServiceApi.getUserById(account.getId(), token);
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
        UserResponse user = authServiceApi.getUserById(account.getId(), token);
        AccountResponse accountResponse = AccountMapper.mapEntityToResponse(account);
        if (user != null) {
            accountResponse.setUserFullName(user.getFullName());
        }

        return accountResponse;
    }

    @Override
    public AccountResponse changeBalance(String id, ChangeBalanceRequest request) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountServiceException(ErrorCode.NOT_FOUND, "Account with that account number not found"));
        if (request.getType().equals(ChangeBalanceType.ADD)) {
            account.setBalance(account.getBalance() + request.getAmount());
        } else {
            Double result = account.getBalance() - request.getAmount();
            if (result < 0) {
                throw new AccountServiceException(ErrorCode.NOT_ENOUGH_FUNDS, "Not enough funds in account for this transaction");
            }
            account.setBalance(result);
        }

        Account updated = accountRepository.save(account);

        return AccountMapper.mapEntityToResponse(updated);
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
