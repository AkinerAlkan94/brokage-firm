package com.akiner.brokage_firm.rest.controller;

import com.akiner.brokage_firm.model.*;
import com.akiner.brokage_firm.rest.dto.DepositWithdrawRequest;
import com.akiner.brokage_firm.rest.dto.StockOrderDto;
import com.akiner.brokage_firm.service.AssetService;
import com.akiner.brokage_firm.service.CustomerService;
import com.akiner.brokage_firm.service.StockOrderService;
import com.akiner.brokage_firm.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class BrokageControllerTest {

    @InjectMocks private BrokageController brokageController;

    @Mock private TransactionService transactionService;

    @Mock private CustomerService customerService;

    @Mock private StockOrderService stockOrderService;

    @Mock private AssetService assetService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createCustomer_returnsCreatedCustomer() {
        Customer customer = new Customer();
        when(customerService.createCustomer(null, null, 0)).thenReturn(customer);

        ResponseEntity<String> response = brokageController.createCustomer(new Customer());

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void removeCustomer_returnsSuccessMessage() {
        ResponseEntity<String> response = brokageController.removeCustomer(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Customer removed successfully", response.getBody());
    }

    @Test
    public void deposit_returnsSuccessMessage() {
        ResponseEntity<String> response = brokageController.deposit(new DepositWithdrawRequest());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Deposit successful", response.getBody());
    }

    @Test
    public void withdraw_returnsSuccessMessage() {
        ResponseEntity<String> response = brokageController.withdraw(new DepositWithdrawRequest());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Withdrawal successful", response.getBody());
    }

    @Test
    public void createOrder_returnsCreatedOrder() {
        StockOrder order = new StockOrder();
        when(stockOrderService.createOrder(any())).thenReturn(order);

        ResponseEntity<?> response = brokageController.createOrder(new StockOrderDto());

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(order, response.getBody());
    }

    @Test
    public void listOrders_returnsOrderList() {
        StockOrder order = new StockOrder();
        when(stockOrderService.listOrders(any(), any(), any(), any(), any(), any()))
                .thenReturn(Collections.singletonList(order));

        ResponseEntity<?> response =
                brokageController.listOrders(null, null, null, null, null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.singletonList(order), response.getBody());
    }

    @Test
    public void cancelOrder_returnsSuccessMessage() {
        ResponseEntity<String> response = brokageController.cancelOrder(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Order canceled successfully", response.getBody());
    }

    @Test
    public void getAssets_returnsAssetList() {
        Asset asset = new Asset();
        when(assetService.getAssetsForCustomer(any(), any()))
                .thenReturn(Collections.singletonList(asset));

        ResponseEntity<?> response = brokageController.getAssets(1L, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.singletonList(asset), response.getBody());
    }

    @Test
    public void matchOrders_returnsOkStatus() {
        ResponseEntity<Void> response = brokageController.matchOrders();

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
