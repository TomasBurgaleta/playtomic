package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.model.ChargeRequest;
import com.playtomic.tests.wallet.service.authorization.AuthorizationService;
import com.playtomic.tests.wallet.service.security.SecurityService;
import com.playtomic.tests.wallet.service.send.ContingencySendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;



@RestController()
public class WalletController {


    @Autowired
    private SecurityService securityService;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private ContingencySendService contingencySendService;


    private Logger log = LoggerFactory.getLogger(WalletController.class);

    @RequestMapping("/")
    public void log() {
        log.info("Logging from /");
    }

    @PostMapping("/charge")
    public HttpStatus charge(@RequestBody ChargeRequest chargeRequest, @RequestHeader Map<String, String> headers) {
        // security, authorization, net error here
        if(!securityService.isValidToken(headers)) {
            return HttpStatus.FORBIDDEN;
        }

        if(!authorizationService.isValidRoll(headers)) {
            return HttpStatus.UNAUTHORIZED;
        }

        // all business in this service
        contingencySendService.chargeService(chargeRequest.getCreditCardNumber(), chargeRequest.getAmount());
        log.info("charge {} for user", chargeRequest.getAmount());
        return HttpStatus.OK;
    }

    @GetMapping("/refund/{paymentId}")
    public HttpStatus refund(@PathVariable String paymentId, @RequestHeader Map<String, String> headers) {

        if(!securityService.isValidToken(headers)) {
            return HttpStatus.FORBIDDEN;
        }

        if(!authorizationService.isValidRoll(headers)) {
            return HttpStatus.UNAUTHORIZED;
        }

        contingencySendService.refundService(paymentId);

        log.info("refund");

        return HttpStatus.OK;
    }

}
