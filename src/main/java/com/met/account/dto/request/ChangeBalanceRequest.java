package com.met.account.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ChangeBalanceRequest {
    @NotNull
    private Long accountFrom;
    @NotNull
    private Long accountTo;
    @NotNull
    private Double amount;
}
