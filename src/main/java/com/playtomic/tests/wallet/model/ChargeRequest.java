package com.playtomic.tests.wallet.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;


import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
public class ChargeRequest {


    @NonNull
    @JsonProperty("credit_card")
    String creditCardNumber;

    @NonNull
    @JsonProperty("amount")
    BigDecimal amount;

}
