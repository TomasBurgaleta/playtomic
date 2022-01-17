package com.playtomic.tests.wallet.api;

import com.github.javafaker.Faker;
import com.playtomic.tests.wallet.model.ChargeRequest;
import com.playtomic.tests.wallet.service.authorization.AuthorizationService;
import com.playtomic.tests.wallet.service.security.SecurityService;
import com.playtomic.tests.wallet.service.send.ContingencySendService;
import org.h2.mvstore.MVMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;


import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;


import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletControllerTest {


    @Mock
    private SecurityService securityService;

    @Mock
    private AuthorizationService authorizationService;

    @Mock
    private ContingencySendService contingencySendService;

    @InjectMocks
    private WalletController walletController;

    private Faker faker = new Faker();

    @Test
    public void wallet_charge_happy_path_test() {
        // Given
        String creditCard = faker.random().hex(15);
        BigDecimal amount = new BigDecimal(faker.random().nextInt(6,20));
        ChargeRequest chargeRequest = new ChargeRequest(creditCard, amount);
        Map<String, String> headers = Collections.EMPTY_MAP;

        // when
        when(authorizationService.isValidRoll(headers)).thenReturn(true);
        when(securityService.isValidToken(headers)).thenReturn(true);
        HttpStatus httpStatus = walletController.charge(chargeRequest, headers);

        // Then
        verify(contingencySendService, atMostOnce()).chargeService(creditCard, amount);
        assertThat(httpStatus).isNotNull().isEqualTo(HttpStatus.OK);

    }

    @Test
    public void wallet_refund_happy_path_test() {
        // Given
        UUID paymentId = UUID.randomUUID();
        Map<String, String> headers = Collections.EMPTY_MAP;

        // when
        when(authorizationService.isValidRoll(headers)).thenReturn(true);
        when(securityService.isValidToken(headers)).thenReturn(true);

        // Then
        HttpStatus httpStatus = walletController.refund(paymentId.toString(), headers);

        verify(contingencySendService, atMostOnce()).refundService(paymentId.toString());
        assertThat(httpStatus).isNotNull().isEqualTo(HttpStatus.OK);
    }
}