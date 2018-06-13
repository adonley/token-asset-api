package io.block16.assetapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.block16.assetapi.domain.EthereumTransactionType;
import io.block16.assetapi.domain.cassandra.AddressTransaction;
import lombok.Data;

import java.util.Date;

@Data
public class FlattenedAddressTransaction {
    private String address;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date transactionDate;
    private String value;
    private String toAddress;
    private String fromAddress;
    private String ethereumContract;
    private String transactionHash;
    private Long blockNumber;
    private EthereumTransactionType transactionType;
    private String fee;

    public static FlattenedAddressTransaction fromDomain(AddressTransaction addressTransaction) {
        FlattenedAddressTransaction ft = new FlattenedAddressTransaction();
        ft.setAddress(addressTransaction.getKey().getAddress());
        ft.setTransactionDate(addressTransaction.getKey().getTransactionDate());
        ft.setValue(addressTransaction.getValue());
        ft.setToAddress(addressTransaction.getToAddress());
        ft.setFromAddress(addressTransaction.getFromAddress());
        ft.setEthereumContract(addressTransaction.getEthereumContract());
        ft.setTransactionHash(addressTransaction.getTransactionHash());
        ft.setBlockNumber(addressTransaction.getBlockNumber());
        ft.setTransactionType(addressTransaction.getTransactionType());
        ft.setFee(addressTransaction.getFee());
        return ft;
    }
}
