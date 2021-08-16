package com.met.account.dto.response;

import lombok.Data;

@Data
public class ExecuteTransactionResponse {
    private String fromId;
    private String toId;
    private boolean success;
    private String errorMessage;
}
