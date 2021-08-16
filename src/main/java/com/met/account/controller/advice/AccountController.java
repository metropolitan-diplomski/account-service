package com.met.account.controller.advice;

import com.met.account.dto.request.ChangeBalanceRequest;
import com.met.account.dto.request.CreateAccountRequest;
import com.met.account.dto.response.AccountResponse;
import com.met.account.dto.response.ExecuteTransactionResponse;
import com.met.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Value("${jwt.header}")
    private String tokenHeader;

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(HttpServletRequest request, @Valid @RequestBody CreateAccountRequest accountRequest) {
        String authToken = request.getHeader(this.tokenHeader);
        return ResponseEntity.ok(accountService.createAccount(accountRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getById(HttpServletRequest request, @PathVariable("id") String id) {
        String authToken = request.getHeader(this.tokenHeader);
        return ResponseEntity.ok(accountService.getAccountById(id, authToken));
    }

    @GetMapping("/accountNum/{num}")
    public ResponseEntity<AccountResponse> getByAccountNumber(HttpServletRequest request, @PathVariable("num") Long num) {
        String authToken = request.getHeader(this.tokenHeader);
        return ResponseEntity.ok(accountService.getAccountByAccountNumber(num, authToken));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<AccountResponse>> getByUserId(HttpServletRequest request, @PathVariable("id") String id) {
        String authToken = request.getHeader(this.tokenHeader);
        return ResponseEntity.ok(accountService.getAllAccountsForUser(id, authToken));
    }

    @PutMapping("/transaction/execute/")
    public ResponseEntity<ExecuteTransactionResponse> changeBalance(@Valid @RequestBody ChangeBalanceRequest request) {
        return ResponseEntity.ok(accountService.changeBalance(request));
    }
}
