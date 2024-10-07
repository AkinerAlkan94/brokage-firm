package com.akiner.brokage_firm.rest.controller;

import com.akiner.brokage_firm.model.*;
import com.akiner.brokage_firm.rest.dto.DepositWithdrawRequest;
import com.akiner.brokage_firm.rest.dto.StockOrderDto;
import com.akiner.brokage_firm.service.AssetService;
import com.akiner.brokage_firm.service.CustomerService;
import com.akiner.brokage_firm.service.StockOrderService;
import com.akiner.brokage_firm.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class BrokageController {

    @Autowired private TransactionService transactionService;
    @Autowired private CustomerService customerService;
    @Autowired private StockOrderService stockOrderService;
    @Autowired private AssetService assetService;

    @PostMapping("/customers")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<String> createCustomer(@RequestBody Customer customer) {
        Customer createdCustomer =
                customerService.createCustomer(
                        customer.getName(), customer.getSurname(), customer.getBalance());
        return new ResponseEntity<>(
                "Customer created successfully with ID: " + createdCustomer.getId(),
                HttpStatus.CREATED);
    }

    @DeleteMapping("/customers/{customerId}")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<String> removeCustomer(@PathVariable Long customerId) {
        customerService.removeCustomer(customerId);
        return new ResponseEntity<>("Customer removed successfully", HttpStatus.OK);
    }

    @PostMapping("/deposit")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<String> deposit(@RequestBody DepositWithdrawRequest request) {
        transactionService.deposit(request.getCustomerId(), request.getAmount());
        return ResponseEntity.ok("Deposit successful");
    }

    @PostMapping("/withdraw")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<String> withdraw(@RequestBody DepositWithdrawRequest request) {
        transactionService.withdraw(
                request.getCustomerId(), request.getAmount(), request.getIban());
        return ResponseEntity.ok("Withdrawal successful");
    }

    @PostMapping("/orders")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<?> createOrder(@RequestBody StockOrderDto orderDto) {
        StockOrder order = stockOrderService.createOrder(orderDto);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping("/orders")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<List<StockOrder>> listOrders(
            @RequestParam(required = false) String customerId,
            @RequestParam(required = false) String assetName,
            @RequestParam(required = false) OrderSide orderSide,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    LocalDateTime endDate,
            @RequestParam(required = false) OrderStatus status) {

        List<StockOrder> orders =
                stockOrderService.listOrders(
                        customerId, assetName, orderSide, startDate, endDate, status);
        return ResponseEntity.ok(orders);
    }

    @PostMapping("orders/{orderId}")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId) {
        stockOrderService.cancelOrder(orderId);
        return ResponseEntity.ok("Order canceled successfully");
    }

    @GetMapping("/assets")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<List<Asset>> getAssets(
            @RequestParam Long customerId, @RequestParam(required = false) String assetName) {
        List<Asset> assets = assetService.getAssetsForCustomer(customerId, assetName);
        return ResponseEntity.ok(assets);
    }

    @PostMapping("admin/match-orders")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> matchOrders() {
        stockOrderService.matchOrders();
        return ResponseEntity.ok().build();
    }
}
