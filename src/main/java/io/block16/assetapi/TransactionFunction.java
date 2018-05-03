package io.block16.assetapi;

import io.block16.assetapi.domain.cassandra.AddressTransaction;
import io.block16.assetapi.service.AddressTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class TransactionFunction {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    final AddressTransactionService addressTransactionService;

    @Autowired
    public TransactionFunction(
            final AddressTransactionService addressTransactionService
    ) {
        this.addressTransactionService = addressTransactionService;
    }

    @GetMapping("/v1/{address}/transactions")
    public GenericResponse<List<AddressTransaction>> function (@PathVariable String address) {
        String a = EthereumUtilities.removeHexPrefix(address.toLowerCase());
        List<AddressTransaction> transactions = this.addressTransactionService.findByAddress(a);
        return new GenericResponse<>(transactions);
    }
}