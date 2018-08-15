package io.block16.assetapi;

import io.block16.assetapi.domain.cassandra.AddressAsset;
import io.block16.assetapi.domain.cassandra.AddressTransaction;
import io.block16.assetapi.dto.FlattenedAddressTransaction;
import io.block16.assetapi.service.AddressTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@RestController
public class TransactionHandler {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final AddressTransactionService addressTransactionService;

    @Autowired
    public TransactionHandler(
            final AddressTransactionService addressTransactionService
    ) {
        this.addressTransactionService = addressTransactionService;
    }

    @CrossOrigin
    @GetMapping("/v1/address/{address}/transactions")
    public List<FlattenedAddressTransaction> function (
            @PathVariable final String address,
            @RequestParam(value = "after",  required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final Date after
    ) {
        String a = EthereumUtilities.removeHexPrefix(address.toLowerCase());
        List<AddressTransaction> transactions;
        if (after == null) {
            transactions = this.addressTransactionService.findTransactionsByAddress(a);
        } else {
            transactions = this.addressTransactionService.findTransactionsByAddressAfter(a, after);
        }
        return transactions.stream().map(FlattenedAddressTransaction::fromDomain).collect(Collectors.toList());
    }

    /*@CrossOrigin
    @GetMapping("/v1/address/{address}/transactions")
    public List<FlattenedAddressTransaction> function (
            @PathVariable final String address,
            @RequestParam(value = "limit",  required = false) final Long limit
    ) {
        String a = EthereumUtilities.removeHexPrefix(address.toLowerCase());
        List<AddressTransaction> transactions;
        if (limit == null) {
            transactions = this.addressTransactionService.findTransactionsByAddress(a);
        } else {
            transactions = this.addressTransactionService.findTopTenTransactionsByAddress(a);
        }
        return transactions.stream().map(FlattenedAddressTransaction::fromDomain).collect(Collectors.toList());
    }*/

    @CrossOrigin
    @GetMapping("/v1/address/{address}/assets")
    public List<String> assets (@PathVariable String address) {
        String a = EthereumUtilities.removeHexPrefix(address.toLowerCase());
        List<AddressAsset> assets = this.addressTransactionService.findAssetsByAddress(a);
        return assets.stream().map(AddressAsset::getContractAddress).collect(Collectors.toList());
    }
}