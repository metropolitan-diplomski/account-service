package com.met.account.document;

import lombok.Data;
import nonapi.io.github.classgraph.json.Id;

import javax.validation.constraints.NotEmpty;

@Data
public class Account {
    @Id
    private String id;
    @NotEmpty
    private String name;
    @NotEmpty
    private Long accountNumber;
    @NotEmpty
    private String userId;
    @NotEmpty
    private Currency currency;
    private Double balance = 0.0;
}
