package com.akiner.brokage_firm.service;

import com.akiner.brokage_firm.exception.CustomerNotFoundException;
import com.akiner.brokage_firm.model.Customer;
import com.akiner.brokage_firm.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {
    @Autowired private CustomerRepository customerRepository;

    public Customer getCustomerByCustomerId(Long customerId) {
        return customerRepository
                .findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
    }

    public void updateBalance(Long customerId, double amount) {
        Customer customer = getCustomerByCustomerId(customerId);
        customer.setBalance(customer.getBalance() + amount); // Balance can increase or decrease
        customerRepository.save(customer);
    }

    public Customer createCustomer(String name, String surname, double initialBalance) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setSurname(surname);
        customer.setBalance(initialBalance);
        return customerRepository.save(customer);
    }

    public void removeCustomer(Long customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isPresent()) {
            customerRepository.delete(customer.get());
        } else {
            throw new CustomerNotFoundException("Customer not found");
        }
    }
}
