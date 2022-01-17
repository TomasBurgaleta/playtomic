package com.playtomic.tests.wallet.service.repository;

import com.playtomic.tests.wallet.enties.Amount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AmountRepository extends JpaRepository<Amount, String> {


    Optional<Amount> findByIdCard(String cardId);

    Optional<Amount> findByPaymentId(UUID paymentId);


}
