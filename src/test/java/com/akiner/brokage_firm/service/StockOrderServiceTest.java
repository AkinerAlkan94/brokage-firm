package com.akiner.brokage_firm.service;

import com.akiner.brokage_firm.exception.*;
import com.akiner.brokage_firm.model.*;
import com.akiner.brokage_firm.repository.AssetRepository;
import com.akiner.brokage_firm.repository.CustomerRepository;
import com.akiner.brokage_firm.repository.StockOrderRepository;
import com.akiner.brokage_firm.rest.dto.StockOrderDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class StockOrderServiceTest {

    @InjectMocks private StockOrderService stockOrderService;

    @Mock private CustomerRepository customerRepository;

    @Mock private StockOrderRepository stockOrderRepository;

    @Mock private CustomerService customerService;

    @Mock private AssetRepository assetRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createOrder_returnsOrder_whenCustomerExistsAndHasSufficientBalance() {
        Customer customer = new Customer();
        customer.setBalance(1000);
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(stockOrderRepository.save(any())).thenReturn(new StockOrder());

        StockOrderDto orderDto = new StockOrderDto();
        orderDto.setCustomerId(1L);
        orderDto.setOrderSide(OrderSide.BUY);
        orderDto.setPrice(100);
        orderDto.setSize(5);

        assertDoesNotThrow(() -> stockOrderService.createOrder(orderDto));
    }

    @Test
    public void
            createOrder_throwsInsufficientBalanceException_whenCustomerExistsAndHasInsufficientBalance() {
        Customer customer = new Customer();
        customer.setBalance(100);
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));

        StockOrderDto orderDto = new StockOrderDto();
        orderDto.setCustomerId(1L);
        orderDto.setOrderSide(OrderSide.BUY);
        orderDto.setPrice(100);
        orderDto.setSize(5);

        assertThrows(
                InsufficientBalanceException.class, () -> stockOrderService.createOrder(orderDto));
    }

    @Test
    public void createOrder_throwsCustomerNotFoundException_whenCustomerDoesNotExist() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

        StockOrderDto orderDto = new StockOrderDto();
        orderDto.setCustomerId(1L);

        assertThrows(
                CustomerNotFoundException.class, () -> stockOrderService.createOrder(orderDto));
    }

    @Test
    public void listOrders_returnsOrders_whenOrdersExist() {
        when(stockOrderRepository.findOrdersWithFilters(any(), any(), any(), any(), any(), any()))
                .thenReturn(Collections.singletonList(new StockOrder()));

        assertDoesNotThrow(() -> stockOrderService.listOrders(null, null, null, null, null, null));
    }

    @Test
    public void cancelOrder_cancelsOrder_whenOrderExistsAndIsPending() {
        StockOrder order = new StockOrder();
        order.setStatus(OrderStatus.PENDING);
        when(stockOrderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        when(stockOrderRepository.save(any())).thenReturn(order);

        assertDoesNotThrow(() -> stockOrderService.cancelOrder(1L));
    }

    @Test
    public void cancelOrder_throwsStockOrderNotFoundException_whenOrderDoesNotExist() {
        when(stockOrderRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(StockOrderNotFoundException.class, () -> stockOrderService.cancelOrder(1L));
    }

    @Test
    public void
            cancelOrder_throwsStockOrderIsNotInPendingStateException_whenOrderExistsAndIsNotPending() {
        StockOrder order = new StockOrder();
        order.setStatus(OrderStatus.MATCHED);
        when(stockOrderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        assertThrows(
                StockOrderIsNotInPendingStateException.class,
                () -> stockOrderService.cancelOrder(1L));
    }

    @Test
    public void cancelOrder_throwsStockOrderIsNotInPendingStateException_whenOrderIsNotPending() {
        StockOrder order = new StockOrder();
        order.setStatus(OrderStatus.MATCHED);
        when(stockOrderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        assertThrows(
                StockOrderIsNotInPendingStateException.class,
                () -> stockOrderService.cancelOrder(1L));
    }

    @Test
    public void cancelOrder_throwsAssetNotFoundException_whenAssetDoesNotExist() {
        StockOrder order = new StockOrder();
        order.setStatus(OrderStatus.PENDING);
        order.setOrderSide(OrderSide.SELL);
        order.setAssetName("assetName");
        order.setSize(5);
        when(stockOrderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        when(assetRepository.findByCustomerId(anyLong())).thenReturn(Collections.emptyList());

        assertThrows(AssetNotFoundException.class, () -> stockOrderService.cancelOrder(1L));
    }

    @Test
    public void matchOrder_matchesBuyOrder() {
        StockOrder order = new StockOrder();
        order.setCustomerId(1L);
        order.setOrderSide(OrderSide.BUY);
        order.setAssetName("assetName");
        order.setSize(5);

        Customer customer = new Customer();
        customer.setBalance(1000);

        when(customerService.getCustomerByCustomerId(anyLong())).thenReturn(customer);
        when(assetRepository.findByCustomerIdAndAssetNameContainingIgnoreCase(
                        anyLong(), anyString()))
                .thenReturn(Collections.singletonList(new Asset()));

        assertDoesNotThrow(() -> stockOrderService.matchOrder(order));

        assertEquals(OrderStatus.MATCHED, order.getStatus());
        verify(stockOrderRepository).save(any());
        verify(assetRepository).save(any());
    }

    @Test
    public void matchOrder_matchesSellOrder() {
        StockOrder order = new StockOrder();
        order.setCustomerId(1L);
        order.setOrderSide(OrderSide.SELL);
        order.setAssetName("assetName");
        order.setSize(5);
        order.setPrice(100);

        Customer customer = new Customer();
        customer.setBalance(1000);

        when(customerService.getCustomerByCustomerId(anyLong())).thenReturn(customer);

        assertDoesNotThrow(() -> stockOrderService.matchOrder(order));

        assertEquals(OrderStatus.MATCHED, order.getStatus());
        assertEquals(1500, customer.getBalance());
        verify(stockOrderRepository).save(any());
        verify(customerRepository).save(any());
    }

    @Test
    public void createOrder_createsSellOrder_whenCustomerExistsAndHasSufficientAssetBalance() {
        StockOrderDto orderDto = new StockOrderDto();
        orderDto.setCustomerId(1L);
        orderDto.setOrderSide(OrderSide.SELL);
        orderDto.setAssetName("assetName");
        orderDto.setSize(5);
        orderDto.setPrice(100);

        Customer customer = new Customer();
        customer.setBalance(1000);

        Asset asset = new Asset();
        asset.setAssetName("assetName");
        asset.setUsableQuantity(10);

        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(assetRepository.findByCustomerId(anyLong()))
                .thenReturn(Collections.singletonList(asset));
        when(stockOrderRepository.save(any())).thenReturn(new StockOrder());

        assertDoesNotThrow(() -> stockOrderService.createOrder(orderDto));

        assertEquals(5, asset.getUsableQuantity());
        verify(stockOrderRepository).save(any());
        verify(assetRepository).save(any());
    }
}
