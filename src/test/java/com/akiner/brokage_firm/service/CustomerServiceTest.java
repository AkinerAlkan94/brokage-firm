package com.akiner.brokage_firm.service;

import com.akiner.brokage_firm.exception.CustomerNotFoundException;
import com.akiner.brokage_firm.model.Customer;
import com.akiner.brokage_firm.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {

    @InjectMocks private CustomerService customerService;

    @Mock private CustomerRepository customerRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getCustomerByCustomerId_returnsCustomer_whenCustomerExists() {
        Customer customer = new Customer();
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));

        Customer result = customerService.getCustomerByCustomerId(1L);

        assertEquals(customer, result);
    }

    @Test
    public void getCustomerByCustomerId_throwsCustomerNotFoundException_whenCustomerDoesNotExist() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(
                CustomerNotFoundException.class, () -> customerService.getCustomerByCustomerId(1L));
    }

    @Test
    public void updateBalance_updatesBalance_whenCustomerExists() {
        Customer customer = new Customer();
        customer.setBalance(100);
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(customerRepository.save(any())).thenReturn(customer);

        customerService.updateBalance(1L, 50);

        assertEquals(150, customer.getBalance());
    }

    @Test
    public void createCustomer_returnsCreatedCustomer() {
        Customer customer = new Customer();
        when(customerRepository.save(any())).thenReturn(customer);

        Customer result = customerService.createCustomer("John", "Doe", 100);

        assertEquals(customer, result);
    }

    @Test
    public void removeCustomer_removesCustomer_whenCustomerExists() {
        Customer customer = new Customer();
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));

        customerService.removeCustomer(1L);

        verify(customerRepository, times(1)).delete(customer);
    }

    @Test
    public void removeCustomer_throwsCustomerNotFoundException_whenCustomerDoesNotExist() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.removeCustomer(1L));
    }
}
