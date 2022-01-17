package com.playtomic.tests.wallet.service.authorization;

import java.util.Map;

public interface AuthorizationService {

    boolean isValidRoll(Map<String, String> headers);
}
