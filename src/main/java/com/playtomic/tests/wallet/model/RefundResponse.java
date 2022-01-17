package com.playtomic.tests.wallet.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class RefundResponse {

    private UUID id;
    private UUID payment_id;
    private BigDecimal amount;


}
