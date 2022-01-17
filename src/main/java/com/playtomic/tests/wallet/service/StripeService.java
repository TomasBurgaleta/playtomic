package com.playtomic.tests.wallet.service;


import com.playtomic.tests.wallet.exception.StripeNotServiceException;
import com.playtomic.tests.wallet.exception.StripeServiceException;
import com.playtomic.tests.wallet.model.AmountResponse;
import com.playtomic.tests.wallet.model.ChargeRequest;
import com.playtomic.tests.wallet.model.RefundResponse;
import com.playtomic.tests.wallet.service.repository.AmountDDBBService;
import com.sun.istack.NotNull;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;


/**
 * Handles the communication with Stripe.
 *
 * A real implementation would call to String using their API/SDK.
 * This dummy implementation throws an error when trying to charge less than 10€.
 */
@Service
public class StripeService {

    private Logger log = LoggerFactory.getLogger(StripeService.class);

    @Autowired
    private AmountDDBBService amountDDBBService;

    @NonNull
    private URI chargesUri;

    @NonNull
    private String refundsUri;

    @NonNull
    private RestTemplate restTemplate;


    public StripeService(@Value("${stripe.simulator.charges-uri}") @NonNull URI chargesUri,
                         @Value("${stripe.simulator.refunds-uri}") @NonNull String refundsUri,
                         @NotNull RestTemplateBuilder restTemplateBuilder) {
        this.chargesUri = chargesUri;
        this.refundsUri = refundsUri;
        this.restTemplate =
                restTemplateBuilder
                .errorHandler(new StripeRestTemplateResponseErrorHandler())
                .build();
    }

    /**
     * Charges money in the credit card.
     *
     * Ignore the fact that no CVC or expiration date are provided.
     *
     * @param ChargeRequest The number of the credit card
     *
     * @throws StripeServiceException
     */
    public boolean charge(ChargeRequest body) throws StripeServiceException {
        boolean isCorrectSend = false;

        // business error here
        //is simply version, we can add more information like user, change idCard by "card name user"
        // add a kpi card service......
        try {
            ResponseEntity<AmountResponse> response = restTemplate.postForEntity(chargesUri, body, AmountResponse.class);
            // return OK
            if(response.getStatusCode().is2xxSuccessful()) {
                amountDDBBService.addAmount(body, response.getBody());
                isCorrectSend = true;
            }
        } catch (StripeNotServiceException e) {
            log.error("error 422");
        } catch (StripeServiceException e) {
            log.error("otro error");
        }

        return isCorrectSend;
    }



    /**
     * Refunds the specified payment.
     */
    public boolean refund(@NonNull String paymentId) throws StripeServiceException {
        boolean isCorrectSend = false;
        try {
            String finalUrl = createUrlById(refundsUri, paymentId);
            ResponseEntity<RefundResponse> result = restTemplate.postForEntity(finalUrl, null, RefundResponse.class);
            if (result.getStatusCode().is2xxSuccessful()) {
                amountDDBBService.refund(result.getBody());
                isCorrectSend = true;
            }
        } catch (StripeNotServiceException e) {
            log.error("error 422");
        } catch (StripeServiceException e) {
            log.error("otro error");
        }
        return isCorrectSend;
    }

    private String createUrlById(String originalUri, String paymentId) {
        Map<String, String> urlParams = Map.of("payment_id", paymentId);
        return UriComponentsBuilder.fromUriString(originalUri).buildAndExpand(urlParams).toUriString();
    }

}
