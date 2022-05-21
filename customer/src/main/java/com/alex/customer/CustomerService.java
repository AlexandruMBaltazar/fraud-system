package com.alex.customer;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public record CustomerService(ModelMapper modelMapper) {

    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = modelMapper.map(request, Customer.class);

    }
}
