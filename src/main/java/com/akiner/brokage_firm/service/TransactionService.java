package com.akiner.brokage_firm.service;

import com.akiner.brokage_firm.exception.IBANIsNotGivenException;
import com.akiner.brokage_firm.exception.InsufficientBalanceException;
import com.akiner.brokage_firm.exception.InvalidIBANIsGivenException;
import com.akiner.brokage_firm.model.Customer;
import com.akiner.brokage_firm.model.Transaction;
import com.akiner.brokage_firm.model.TransactionOperation;
import com.akiner.brokage_firm.repository.TransactionRepository;
import com.akiner.brokage_firm.validator.IBANValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionService {
    @Autowired private TransactionRepository transactionRepository;
    @Autowired private CustomerService customerService;

    public void deposit(Long customerId, double amount) {
        customerService.updateBalance(customerId, amount);
        Transaction transaction = new Transaction();
        transaction.setCustomerId(customerId);
        transaction.setAmount(amount);
        transaction.setType(TransactionOperation.DEPOSIT);
        transaction.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(transaction);
    }

    public void withdraw(Long customerId, double amount, String iban) {
        if (iban == null) {
            throw new IBANIsNotGivenException("IBAN is not given for withdrawal");
        }

        if (!IBANValidator.isValid(iban)) {
            throw new InvalidIBANIsGivenException("Given IBAN value for withdrawal is invalid");
        }

        Customer customer = customerService.getCustomerByCustomerId(customerId);
        if (customer.getBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient balance");
        }
        customerService.updateBalance(customerId, -amount);
        Transaction transaction = new Transaction();
        transaction.setCustomerId(customerId);
        transaction.setAmount(amount);
        transaction.setIban(iban);
        transaction.setType(TransactionOperation.WITHDRAW);
        transaction.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(transaction);
    }
}
