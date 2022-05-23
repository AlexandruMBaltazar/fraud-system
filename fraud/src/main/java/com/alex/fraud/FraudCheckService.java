package com.alex.fraud;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public record FraudCheckService(FraudCheckHistoryRepository fraudCheckHistoryRepository) {
    public boolean isFraudulentCustomer(Long customerId) {
        /*
        If this was a real application here a 3rd party system would be useful
        At the moment let's say that nobody is a fraud
         */
        fraudCheckHistoryRepository.save(
                FraudCheckHistory
                        .builder()
                        .customerId(customerId)
                        .isFraudster(false)
                        .createdAt(LocalDateTime.now())
                        .build()
        );
        return false;
    }
}
