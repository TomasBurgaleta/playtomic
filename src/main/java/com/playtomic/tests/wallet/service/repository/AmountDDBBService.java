package com.playtomic.tests.wallet.service.repository;

import com.playtomic.tests.wallet.enties.Amount;
import com.playtomic.tests.wallet.model.AmountResponse;
import com.playtomic.tests.wallet.model.ChargeRequest;
import com.playtomic.tests.wallet.model.RefundResponse;

import java.util.Optional;

public interface AmountDDBBService {

    Optional<Amount> findByIdCard(String card);
    Optional<Amount> findByPaymentId(String paymentId);

    void addAmount(ChargeRequest body, AmountResponse amount);

    void refund(RefundResponse body);
}

