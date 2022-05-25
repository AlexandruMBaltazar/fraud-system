package com.alex.customer;
import com.alex.clients.fraud.FraudCheckResponse;
import com.alex.clients.fraud.FraudClient;
import com.alex.clients.notification.NotificationClient;
import com.alex.clients.notification.NotificationRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public record CustomerService(
        CustomerRepository customerRepository,
        RestTemplate restTemplate,
        FraudClient fraudClient,
        NotificationClient notificationClient
) {

    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();

        customerRepository().saveAndFlush(customer);

        FraudCheckResponse fraudCheckResponse = fraudClient.isFraudster(customer.getId());

        if (fraudCheckResponse.isFraudster()) {
            throw new IllegalStateException("fraudster");
        }

        // todo: make it async, add it to a queue
        notificationClient.sendNotification(
                new NotificationRequest(
                        customer.getId(),
                        customer.getEmail(),
                        String.format("Hi, %s, welcome to the Fraud System...", customer.getFirstName())
                )
        );
    }
}
