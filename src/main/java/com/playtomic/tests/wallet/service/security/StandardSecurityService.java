package com.playtomic.tests.wallet.service.security;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class StandardSecurityService implements SecurityService {

    @Override
    public boolean isValidToken(Map<String, String> headers) {
        // verificacion de token por ejemplo
        return true;
    }
}
