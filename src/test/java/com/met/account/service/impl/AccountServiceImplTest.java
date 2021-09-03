package com.met.account.service.impl;

import com.met.account.api.AuthServiceApi;
import com.met.account.document.Account;
import com.met.account.document.Currency;
import com.met.account.dto.AccountMapper;
import com.met.account.dto.request.CreateAccountRequest;
import com.met.account.dto.response.AccountResponse;
import com.met.account.dto.response.UserResponse;
import com.met.account.exception.AccountServiceException;
import com.met.account.repository.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;

import javax.security.auth.login.AccountException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AuthServiceApi authServiceApi;
    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    public void testCreteSuccess() {
        String userId = "12312312312";
        CreateAccountRequest request = new CreateAccountRequest();
        request.setName("Test account");
        request.setCurrency(Currency.RSD);
        request.setUserId(userId);
        UserResponse user = createFakeUser();

        Account account = new Account();
        account.setId("12312312");
        account.setName(request.getName());
        account.setUserId(userId);
        account.setCurrency(request.getCurrency());
        account.setBalance(0.0);

        Mockito.when(
                authServiceApi.getUserById(userId, "token"))
                .thenReturn(user);
        Mockito.when(
                accountRepository.findByAccountNumber(Mockito.any()))
                .thenReturn(Optional.empty());
        Mockito.when(
                accountRepository.save(Mockito.any()))
                .thenReturn(account);
        AccountResponse expectedResponse = AccountMapper.mapEntityToResponse(account);
        AccountResponse actualResponse = accountService.createAccount(request);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testGetAccountByIdFailed() {
        String id = "123123";
        Mockito.when(accountRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(AccountServiceException.class, () -> {
            accountService.getAccountById(id, "token");
        });
    }

    private UserResponse createFakeUser() {
        UserResponse userResponse = new UserResponse();
        userResponse.setId("12312312312");
        userResponse.setEmail("fake@user.com");
        userResponse.setFullName("Fake User");
        return userResponse;
    }
}

