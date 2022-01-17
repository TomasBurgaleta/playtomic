package com.playtomic.tests.wallet.service.send;

import com.playtomic.tests.wallet.enties.Amount;
import com.playtomic.tests.wallet.exception.StripeAmountTooSmallException;
import com.playtomic.tests.wallet.exception.StripeNotExistException;
import com.playtomic.tests.wallet.model.ChargeRequest;
import com.playtomic.tests.wallet.service.StripeService;
import com.playtomic.tests.wallet.service.repository.AmountDDBBService;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@Service
public class ContingencySendService {

    private Logger log = LoggerFactory.getLogger(ContingencySendService.class);
    private static final BigDecimal MINIMUN_VALUE = new BigDecimal(5);
    private static final int MAX_ATTEMPTS = 3;


    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    private StripeService stripeService;

    @Autowired
    private AmountDDBBService amountDDBBService;


    public void chargeService(@NonNull String creditCardNumber, @NonNull BigDecimal amount) {
        ChargeRequest body = new ChargeRequest(creditCardNumber, amount);
        // business error here
        if(MINIMUN_VALUE.compareTo(amount) >= 0) {
            log.warn("the amount isn't enough");
            throw new StripeAmountTooSmallException();
        }

        sendAsyncCharge(body);
    }


    public void refundService(String paymentId) {

        Optional<Amount> amountEntity = amountDDBBService.findByPaymentId(paymentId);
        if(amountEntity.isPresent()) {
            sendAsyncRefund(paymentId);
        }
        throw new StripeNotExistException();

    }

    private void sendAsyncCharge(ChargeRequest chargeRequest) {
        threadPoolExecutor.submit(() ->
            this.sendChargeTaskWithAttempts(chargeRequest)
        );
    }

    private void sendChargeTaskWithAttempts(ChargeRequest chargeRequest) {
        int attempts = 0;
        while (attempts < MAX_ATTEMPTS)  {
            if(!stripeService.charge(chargeRequest)) {
                attempts ++;
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private void sendAsyncRefund(String paymentId) {
        threadPoolExecutor.submit(() ->
            this.sendRefundTaskWithAttempts(paymentId)
        );
    }

    private void sendRefundTaskWithAttempts(String paymentId) {
        int attempts = 0;
        while (attempts < MAX_ATTEMPTS)  {
            if(!stripeService.refund(paymentId)) {
                attempts ++;
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

}
