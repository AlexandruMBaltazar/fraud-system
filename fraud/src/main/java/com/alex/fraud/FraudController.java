package com.alex.fraud;

import com.alex.clients.fraud.FraudCheckResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/fraud-check")
public record FraudController(FraudCheckService fraudCheckService) {
    @GetMapping(path = "{customerId}")
    public ResponseEntity<FraudCheckResponse> isFraudster(@PathVariable Long customerId) {
        boolean isFraudulentCustomer = fraudCheckService
                .isFraudulentCustomer(customerId);

        log.info("fraud check request for customer {}", customerId);

        return ResponseEntity.ok(new FraudCheckResponse(isFraudulentCustomer));
    }
}
