package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.exception.StripeNotServiceException;
import com.playtomic.tests.wallet.exception.StripeServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;

public class StripeRestTemplateResponseErrorHandler extends DefaultResponseErrorHandler {

    @Override
    protected void handleError(ClientHttpResponse response, HttpStatus statusCode) throws IOException {
        if (statusCode == HttpStatus.UNPROCESSABLE_ENTITY) {
            throw new StripeNotServiceException();
        } else if (statusCode.is5xxServerError() ) {
            throw new StripeServiceException();
        }

        super.handleError(response, statusCode);
    }
}
