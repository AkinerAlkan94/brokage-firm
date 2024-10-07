package com.akiner.brokage_firm.repository;

import com.akiner.brokage_firm.model.OrderSide;
import com.akiner.brokage_firm.model.OrderStatus;
import com.akiner.brokage_firm.model.StockOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StockOrderRepository extends JpaRepository<StockOrder, Long> {

    @Query(
            "SELECT s FROM StockOrder s WHERE "
                    + "(:customerId IS NULL OR s.customerId = :customerId) AND "
                    + "(:assetName IS NULL OR s.assetName = :assetName) AND "
                    + "(:orderSide IS NULL OR s.orderSide = :orderSide) AND "
                    + "(:startDate IS NULL OR s.orderDate >= :startDate) AND "
                    + "(:endDate IS NULL OR s.orderDate <= :endDate) AND "
                    + "(:status IS NULL OR s.status <= :status)")
    List<StockOrder> findOrdersWithFilters(
            @Param("customerId") String customerId,
            @Param("assetName") String assetName,
            @Param("orderSide") OrderSide orderSide,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("status") OrderStatus status);
}
