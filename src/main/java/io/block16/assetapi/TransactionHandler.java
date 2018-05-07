package io.block16.assetapi;

import io.block16.assetapi.domain.cassandra.AddressAsset;
import io.block16.assetapi.domain.cassandra.AddressTransaction;
import io.block16.assetapi.service.AddressTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


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

    @CrossOrigin
    @GetMapping("/v1/address/{address}/transactions")
    public GenericResponse<List<AddressTransaction>> function (@PathVariable String address) {
        String a = EthereumUtilities.removeHexPrefix(address.toLowerCase());
        List<AddressTransaction> transactions = this.addressTransactionService.findTransactionsByAddress(a);
        return new GenericResponse<>(transactions);
    }

    @CrossOrigin
    @GetMapping("/v1/address/{address}/assets")
    public GenericResponse<List<String>> assets (@PathVariable String address) {
        String a = EthereumUtilities.removeHexPrefix(address.toLowerCase());
        List<AddressAsset> assets = this.addressTransactionService.findAssetsByAddress(a);
        return new GenericResponse<>(assets.stream().map(AddressAsset::getContractAddress).collect(Collectors.toList()));
    }
}