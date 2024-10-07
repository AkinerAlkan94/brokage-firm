package com.akiner.brokage_firm.service;

import com.akiner.brokage_firm.exception.*;
import com.akiner.brokage_firm.model.*;
import com.akiner.brokage_firm.repository.AssetRepository;
import com.akiner.brokage_firm.repository.CustomerRepository;
import com.akiner.brokage_firm.repository.StockOrderRepository;
import com.akiner.brokage_firm.rest.dto.StockOrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StockOrderService {

    @Autowired private CustomerRepository customerRepository;
    @Autowired private StockOrderRepository stockOrderRepository;
    @Autowired private CustomerService customerService;
    @Autowired private AssetRepository assetRepository;

    public StockOrder createOrder(StockOrderDto orderDto) {
        Customer customer =
                customerRepository
                        .findById(orderDto.getCustomerId())
                        .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        // Check for balance if order is a BUY
        if (orderDto.getOrderSide() == OrderSide.BUY) {
            double requiredAmount = orderDto.getPrice() * orderDto.getSize();
            if (customer.getBalance() < requiredAmount) {
                throw new InsufficientBalanceException("Insufficient balance");
            }
            // Deduct the balance for a BUY order
            customer.setBalance(customer.getBalance() - requiredAmount);
            customerRepository.save(customer);
        }

        // Check for balance if order is a SELL
        if (orderDto.getOrderSide() == OrderSide.SELL) {
            Asset customerAsset =
                    assetRepository.findByCustomerId(orderDto.getCustomerId()).stream()
                            .filter(asset -> asset.getAssetName().equals(orderDto.getAssetName()))
                            .findFirst()
                            .orElseThrow(() -> new AssetNotFoundException("Asset not found"));

            if (customerAsset.getUsableQuantity() < orderDto.getSize()) {
                throw new InsufficientAssetBalanceException("Insufficient asset balance");
            }

            // Deduct the asset balance for a SELL order
            customerAsset.setUsableQuantity(customerAsset.getUsableQuantity() - orderDto.getSize());
            assetRepository.save(customerAsset);
        }

        // Create the order
        StockOrder order = new StockOrder();
        order.setCustomerId(orderDto.getCustomerId());
        order.setAssetName(orderDto.getAssetName());
        order.setOrderSide(orderDto.getOrderSide());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setSize(orderDto.getSize());
        order.setPrice(orderDto.getPrice());

        return stockOrderRepository.save(order);
    }

    public List<StockOrder> listOrders(
            String customerId,
            String assetName,
            OrderSide orderSide,
            LocalDateTime startDate,
            LocalDateTime endDate,
            OrderStatus status) {
        return stockOrderRepository.findOrdersWithFilters(
                customerId, assetName, orderSide, startDate, endDate, status);
    }

    public void cancelOrder(Long orderId) {
        Optional<StockOrder> optionalOrder = stockOrderRepository.findById(orderId);
        StockOrder order =
                optionalOrder.orElseThrow(
                        () ->
                                new StockOrderNotFoundException(
                                        "Stock Order is not found with the given ID."));

        if (OrderStatus.PENDING == order.getStatus()) {
            order.setStatus(OrderStatus.CANCELLED);
            stockOrderRepository.save(order);

            if (order.getOrderSide() == OrderSide.BUY) {
                Customer customer = customerService.getCustomerByCustomerId(order.getCustomerId());
                // Give back the deducted balance to the customer if order is cancelled.
                double amount = order.getPrice() * order.getSize();
                customer.setBalance(customer.getBalance() + amount);
                customerRepository.save(customer);
            }

            if (order.getOrderSide() == OrderSide.SELL) {
                Asset customerAsset =
                        assetRepository.findByCustomerId(order.getCustomerId()).stream()
                                .filter(asset -> asset.getAssetName().equals(order.getAssetName()))
                                .findFirst()
                                .orElseThrow(() -> new AssetNotFoundException("Asset not found"));

                // Give back the deducted asset balance to the customer if order is cancelled.
                customerAsset.setUsableQuantity(
                        customerAsset.getUsableQuantity() + order.getSize());
                assetRepository.save(customerAsset);
            }

        } else {
            throw new StockOrderIsNotInPendingStateException(
                    "Stock Order is not in PENDING state.");
        }
    }

    public void matchOrders() {
        List<StockOrder> orders = listOrders(null, null, null, null, null, OrderStatus.PENDING);
        for (StockOrder order : orders) {
            matchOrder(order);
        }
    }

    public void matchOrder(StockOrder order) {
        Long customerId = order.getCustomerId();
        Customer customer = customerService.getCustomerByCustomerId(customerId);
        if (order.getOrderSide() == OrderSide.BUY) {
            // Balance is already deducted in createOrder method

            // Update Stock Order
            order.setStatus(OrderStatus.MATCHED);
            stockOrderRepository.save(order);

            // Add asset to the customer balance
            Asset customerAsset =
                    assetRepository
                            .findByCustomerIdAndAssetNameContainingIgnoreCase(
                                    order.getCustomerId(), order.getAssetName())
                            .stream()
                            .findFirst()
                            .orElse(new Asset());

            customerAsset.setCustomer(customer);
            customerAsset.setAssetName(order.getAssetName());
            customerAsset.setQuantity(customerAsset.getQuantity() + order.getSize());
            customerAsset.setUsableQuantity(customerAsset.getUsableQuantity() + order.getSize());
            assetRepository.save(customerAsset);
        }

        if (order.getOrderSide() == OrderSide.SELL) {
            // Increase Balance of Customer
            double amount = order.getPrice() * order.getSize();
            customer.setBalance(customer.getBalance() + amount);
            customerRepository.save(customer);

            // Update Stock Order
            order.setStatus(OrderStatus.MATCHED);
            stockOrderRepository.save(order);

            // Asset is already deducted in createOrder method
        }
    }
}
