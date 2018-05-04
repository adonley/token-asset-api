package io.block16.assetapi;

import io.block16.assetapi.domain.cassandra.AddressAsset;
import io.block16.assetapi.domain.cassandra.AddressTransaction;
import io.block16.assetapi.service.AddressTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;


@RestController
public class TransactionHandler {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    final AddressTransactionService addressTransactionService;

    @Autowired
    public TransactionHandler(
            final AddressTransactionService addressTransactionService
    ) {
        this.addressTransactionService = addressTransactionService;
    }

    @GetMapping("/v1/{address}/transactions")
    public GenericResponse<List<AddressTransaction>> function (@PathVariable String address) {
        String a = EthereumUtilities.removeHexPrefix(address.toLowerCase());
        List<AddressTransaction> transactions = this.addressTransactionService.findTransactionsByAddress(a);
        return new GenericResponse<>(transactions);
    }

    @GetMapping("/v1/{address}/assets")
    public GenericResponse<AddressAsset> assets (@PathVariable String address) {
        String a = EthereumUtilities.removeHexPrefix(address.toLowerCase());
        Optional<AddressAsset> assets = this.addressTransactionService.findAssetsByAddress(a);
        if(assets.isPresent()) {
            return new GenericResponse<>(assets.get());
        } else {
            return new GenericResponse<>(null);
        }
    }
}