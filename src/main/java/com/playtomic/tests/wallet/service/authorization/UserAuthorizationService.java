package com.playtomic.tests.wallet.service.authorization;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserAuthorizationService implements AuthorizationService {
    @Override
    public boolean isValidRoll(Map<String, String> headers) {
        return false;
    }
}
