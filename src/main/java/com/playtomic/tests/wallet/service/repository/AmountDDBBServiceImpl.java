package com.playtomic.tests.wallet.service.repository;

import com.playtomic.tests.wallet.enties.Amount;
import com.playtomic.tests.wallet.model.AmountResponse;
import com.playtomic.tests.wallet.model.ChargeRequest;
import com.playtomic.tests.wallet.model.RefundResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AmountDDBBServiceImpl implements AmountDDBBService {


    @Autowired
    private AmountRepository amountRepository;


    @Override
    public Optional<Amount> findByIdCard(String card) {
        return  amountRepository.findByIdCard(card);
    }

    @Override
    public Optional<Amount> findByPaymentId(String paymentId) {
        return  amountRepository.findByPaymentId(UUID.fromString(paymentId));
    }

    @Override
    public void addAmount(ChargeRequest body, AmountResponse amount) {
        Amount amountEntity = new  Amount(body.getCreditCardNumber(), amount.getId(), body.getAmount());
        amountRepository.save(amountEntity);
    }

    @Override
    public void refund(RefundResponse body) {
        Optional<Amount> optionalAmount = amountRepository.findByPaymentId(body.getPayment_id());
        if(optionalAmount.isPresent()) {
            Amount amount = optionalAmount.get();
            amount.setPaymentId(body.getId());
            amountRepository.save(amount);
        }
    }


}
