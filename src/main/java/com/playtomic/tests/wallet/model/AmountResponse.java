package com.playtomic.tests.wallet.model;

import com.fasterxml.jackson.annotation.JsonProperty;


import java.math.BigDecimal;
import java.util.UUID;


public class AmountResponse {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("amount")
    private BigDecimal amount;

    public AmountResponse(UUID id, BigDecimal amount) {
        this.id = id;
        this.amount = amount;
    }

    public AmountResponse() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
