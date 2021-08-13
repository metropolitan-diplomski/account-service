package com.met.account.repository;

import com.met.account.document.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository  extends MongoRepository<Account, String> {
    Optional<Account> findByAccountNumber(Long accountNumber);
    List<Account> findByUserId(String userId);
}
