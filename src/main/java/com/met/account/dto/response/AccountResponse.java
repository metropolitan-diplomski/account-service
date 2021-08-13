package com.met.account.dto.response;

import com.met.account.document.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nonapi.io.github.classgraph.json.Id;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {
    private String id;
    private String name;
    private Long accountNumber;
    private String userId;
    private String userFullName;
    private Currency currency;
    private Double balance = 0.0;
}
