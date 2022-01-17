package com.playtomic.tests.wallet.enties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;
@Entity
@Table(name = "AMOUNT_TABLE")
public class Amount {

    @Id
    @Column(name = "ID_CARD")
    private String idCard;

    @Id
    @Column(name = "ID_PAYMENT")
    private UUID paymentId;

    @Column(name = "AMOUNT")
    private BigDecimal amount;


    public Amount() {
    }

    public Amount(String idCard, UUID paymentId, BigDecimal amount) {
        this.idCard = idCard;
        this.paymentId = paymentId;
        this.amount = amount;
    }


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public UUID getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(UUID paymentId) {
        this.paymentId = paymentId;
    }
}
