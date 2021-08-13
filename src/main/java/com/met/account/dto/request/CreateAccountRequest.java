package com.met.account.dto.request;

import com.met.account.document.Currency;
import lombok.Data;

@Data
public class CreateAccountRequest {
    private String userId;
    private String name;
    private Currency currency;
}
