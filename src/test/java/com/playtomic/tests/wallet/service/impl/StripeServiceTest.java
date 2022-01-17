package com.playtomic.tests.wallet.service.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.playtomic.tests.wallet.exception.StripeAmountTooSmallException;
import com.playtomic.tests.wallet.exception.StripeServiceException;
import com.playtomic.tests.wallet.model.AmountResponse;
import com.playtomic.tests.wallet.model.ChargeRequest;
import com.playtomic.tests.wallet.service.StripeService;

import com.github.jenspiegsa.wiremockextension.Managed;
import com.github.jenspiegsa.wiremockextension.WireMockExtension;
import com.playtomic.tests.wallet.service.repository.AmountDDBBService;
import org.junit.jupiter.api.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;


import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.util.UUID;

import static com.github.jenspiegsa.wiremockextension.ManagedWireMockServer.with;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * This test is failing with the current implementation.
 *
 * How would you test this?
 */
@ExtendWith(WireMockExtension.class)
@ExtendWith(MockitoExtension.class)
public class StripeServiceTest {

    private URI testUri = URI.create("http://localhost:9999/");

    @Managed
    WireMockServer wireMockServer = with(wireMockConfig().port(9999));

    @InjectMocks
    private StripeService stripeService =  new StripeService(testUri, testUri.toString(), new RestTemplateBuilder());

    @Mock
    private AmountDDBBService amountDDBBService;


    @Test
    public void charge_test_ok() throws StripeServiceException, JsonProcessingException {

        BigDecimal amount =  new BigDecimal(15);
        UUID uuid = UUID.randomUUID();
        String card = "4242 4242 4242 4242";

        ObjectMapper mapper = new ObjectMapper();
        AmountResponse amountResponse = new AmountResponse(uuid, amount);
        String amountResponseJson = mapper.writeValueAsString(amountResponse);

        wireMockServer.stubFor(post("/").willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(amountResponseJson)));

        ChargeRequest body = new ChargeRequest(card, amount);
        boolean result = stripeService.charge(body);
        assertThat(result).isTrue();
        verify(amountDDBBService, atMostOnce()).addAmount(body, amountResponse);
    }

    @Test
    public void charge_test_422_ko() throws StripeServiceException, JsonProcessingException {

        BigDecimal amount =  new BigDecimal(15);
        String card = "4242 4242 4242 4242";

        wireMockServer.stubFor(post("/").willReturn(aResponse()
                .withStatus(422)));

        ChargeRequest body = new ChargeRequest(card, amount);

        boolean result = stripeService.charge(body);
        assertThat(result).isFalse();
        verify(amountDDBBService, never()).addAmount(any(ChargeRequest.class), any(AmountResponse.class));

    }

    @Test
    public void charge_test_500_ko() throws StripeServiceException, JsonProcessingException {

        BigDecimal amount =  new BigDecimal(15);
        String card = "4242 4242 4242 4242";

        wireMockServer.stubFor(post("/").willReturn(aResponse()
                .withStatus(500)));

        ChargeRequest body = new ChargeRequest(card, amount);

        boolean result = stripeService.charge(body);
        assertThat(result).isFalse();
        verify(amountDDBBService, never()).addAmount(any(ChargeRequest.class), any(AmountResponse.class));

    }



}
