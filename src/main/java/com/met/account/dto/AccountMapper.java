package com.met.account.dto;

import com.met.account.document.Account;
import com.met.account.dto.response.AccountResponse;

public class AccountMapper {

    public static AccountResponse mapEntityToResponse(Account account) {
       AccountResponse accountResponse = new AccountResponse();
       accountResponse.setId(account.getId());
       accountResponse.setAccountNumber(account.getAccountNumber());
       accountResponse.setName(account.getName());
       accountResponse.setUserId(account.getUserId());
       accountResponse.setCurrency(account.getCurrency());
       accountResponse.setBalance(account.getBalance());

       return accountResponse;
    }
}
