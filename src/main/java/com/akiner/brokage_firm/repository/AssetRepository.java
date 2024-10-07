package com.akiner.brokage_firm.repository;

import com.akiner.brokage_firm.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, String> {
    List<Asset> findByCustomerId(Long customerId);

    List<Asset> findByCustomerIdAndAssetNameContainingIgnoreCase(Long customerId, String assetName);
}
