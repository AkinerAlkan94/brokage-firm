package com.akiner.brokage_firm.service;

import com.akiner.brokage_firm.exception.CustomerNotFoundException;
import com.akiner.brokage_firm.model.Asset;
import com.akiner.brokage_firm.repository.AssetRepository;
import com.akiner.brokage_firm.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetService {

    @Autowired private AssetRepository assetRepository;

    @Autowired private CustomerRepository customerRepository;

    public List<Asset> getAssetsForCustomer(Long customerId, String assetName) {
        if (!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException("Customer not found");
        }

        if (assetName == null || assetName.isEmpty()) {
            return assetRepository.findByCustomerId(customerId);
        } else {
            return assetRepository.findByCustomerIdAndAssetNameContainingIgnoreCase(
                    customerId, assetName);
        }
    }
}
