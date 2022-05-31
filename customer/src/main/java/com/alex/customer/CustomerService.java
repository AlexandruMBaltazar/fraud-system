package com.alex.customer;
import com.alex.amqp.RabbitMQMessageProducer;
import com.alex.clients.fraud.FraudCheckResponse;
import com.alex.clients.fraud.FraudClient;
import com.alex.clients.notification.NotificationRequest;
import org.springframework.stereotype.Service;

@Service
public record CustomerService(
        CustomerRepository customerRepository,
        FraudClient fraudClient,
        RabbitMQMessageProducer rabbitMQMessageProducer
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

        NotificationRequest notificationRequest = new NotificationRequest(
                customer.getId(),
                customer.getEmail(),
                String.format("Hi, %s, welcome to the Fraud System...", customer.getFirstName())
        );

        rabbitMQMessageProducer.publish(
                notificationRequest,
                "internal.exchange",
                "internal.notification.routing-key"
        );
    }
}
