package com.alex.customer;

public record CustomerRegistrationRequest(
        String firstName,
        String lastName,
        String email
) {
}
