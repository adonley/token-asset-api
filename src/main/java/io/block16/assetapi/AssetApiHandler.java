package io.block16.assetapi;

import io.block16.assetapi.service.AddressTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class AssetApiHandler implements Function<Customer, GenericResponse<String>> {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    final AddressTransactionService addressTransactionService;

    @Autowired
    public AssetApiHandler(
            final AddressTransactionService addressTransactionService
    ) {
        this.addressTransactionService = addressTransactionService;
    }

    @Override
    public GenericResponse<String> apply(Customer customer) {
        return new GenericResponse<>("");
    }
}