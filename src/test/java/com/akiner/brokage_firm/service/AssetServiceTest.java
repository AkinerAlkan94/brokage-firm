package com.akiner.brokage_firm.service;

import com.akiner.brokage_firm.exception.CustomerNotFoundException;
import com.akiner.brokage_firm.model.Asset;
import com.akiner.brokage_firm.repository.AssetRepository;
import com.akiner.brokage_firm.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class AssetServiceTest {

    @InjectMocks private AssetService assetService;

    @Mock private AssetRepository assetRepository;

    @Mock private CustomerRepository customerRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAssetsForCustomer_returnsAssets_whenCustomerExistsAndAssetNameIsNull() {
        when(customerRepository.existsById(anyLong())).thenReturn(true);
        when(assetRepository.findByCustomerId(anyLong()))
                .thenReturn(Collections.singletonList(new Asset()));

        List<Asset> assets = assetService.getAssetsForCustomer(1L, null);

        assertEquals(1, assets.size());
    }

    @Test
    public void getAssetsForCustomer_returnsAssets_whenCustomerExistsAndAssetNameIsNotNull() {
        when(customerRepository.existsById(anyLong())).thenReturn(true);
        when(assetRepository.findByCustomerIdAndAssetNameContainingIgnoreCase(
                        anyLong(), anyString()))
                .thenReturn(Collections.singletonList(new Asset()));

        List<Asset> assets = assetService.getAssetsForCustomer(1L, "assetName");

        assertEquals(1, assets.size());
    }

    @Test
    public void getAssetsForCustomer_throwsCustomerNotFoundException_whenCustomerDoesNotExist() {
        when(customerRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(
                CustomerNotFoundException.class, () -> assetService.getAssetsForCustomer(1L, null));
    }
}
