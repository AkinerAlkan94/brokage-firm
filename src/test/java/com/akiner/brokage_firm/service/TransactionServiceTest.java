package com.akiner.brokage_firm.service;

import com.akiner.brokage_firm.exception.IBANIsNotGivenException;
import com.akiner.brokage_firm.exception.InsufficientBalanceException;
import com.akiner.brokage_firm.exception.InvalidIBANIsGivenException;
import com.akiner.brokage_firm.model.Customer;
import com.akiner.brokage_firm.model.Transaction;
import com.akiner.brokage_firm.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    @InjectMocks private TransactionService transactionService;

    @Mock private TransactionRepository transactionRepository;

    @Mock private CustomerService customerService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void deposit_increasesBalanceAndCreatesTransaction() {
        Customer customer = new Customer();
        customer.setBalance(1000);
        when(customerService.getCustomerByCustomerId(anyLong())).thenReturn(customer);

        transactionService.deposit(1L, 500);

        verify(customerService, times(1)).updateBalance(1L, 500);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    public void withdraw_decreasesBalanceAndCreatesTransaction_whenSufficientBalanceAndValidIban() {
        Customer customer = new Customer();
        customer.setBalance(1000);
        when(customerService.getCustomerByCustomerId(anyLong())).thenReturn(customer);

        transactionService.withdraw(1L, 500, "TG53TG0090604310346500400070");

        verify(customerService, times(1)).updateBalance(1L, -500);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    public void withdraw_throwsIBANIsNotGivenException_whenIbanIsNull() {
        assertThrows(
                IBANIsNotGivenException.class, () -> transactionService.withdraw(1L, 500, null));
    }

    @Test
    public void withdraw_throwsInvalidIBANIsGivenException_whenIbanIsInvalid() {
        assertThrows(
                InvalidIBANIsGivenException.class,
                () -> transactionService.withdraw(1L, 500, "invalid iban"));
    }

    @Test
    public void withdraw_throwsInsufficientBalanceException_whenInsufficientBalance() {
        Customer customer = new Customer();
        customer.setBalance(100);
        when(customerService.getCustomerByCustomerId(anyLong())).thenReturn(customer);

        assertThrows(
                InsufficientBalanceException.class,
                () -> transactionService.withdraw(1L, 500, "TG53TG0090604310346500400070"));
    }
}
