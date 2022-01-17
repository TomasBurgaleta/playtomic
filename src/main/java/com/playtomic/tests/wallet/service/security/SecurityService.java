package com.playtomic.tests.wallet.service.security;

import java.util.Map;

public interface SecurityService {

    boolean isValidToken(Map<String, String> headers);
}
