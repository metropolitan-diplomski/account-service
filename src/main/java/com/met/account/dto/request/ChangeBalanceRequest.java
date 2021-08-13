package com.met.account.dto.request;

import com.met.account.dto.ChangeBalanceType;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ChangeBalanceRequest {
    @NotNull
    private Double amount;
    @NotNull
    private ChangeBalanceType type;
}
