package io.block16.assetapi.domain.cassandra;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.block16.assetapi.domain.EthereumTransaction;
import io.block16.assetapi.domain.EthereumTransactionType;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@NoArgsConstructor
@Table("transactions")
public class AddressTransaction {
    @JsonIgnore
    @PrimaryKey
    private TransactionKey key;
    private String value;
    private String toAddress;
    private String fromAddress;
    private String ethereumContract;
    private String transactionHash;
    private Long blockNumber;
    private EthereumTransactionType transactionType;
    private String fee;

    public AddressTransaction(TransactionKey key, EthereumTransaction ethereumTransaction) {
        this.key = key;

        this.value = ethereumTransaction.getValue();
        this.toAddress = ethereumTransaction.getToAddress();
        this.fromAddress = ethereumTransaction.getFromAddress();
        this.ethereumContract = ethereumTransaction.getEthereumContract();
        this.transactionHash = ethereumTransaction.getTransactionHash();
        this.blockNumber = ethereumTransaction.getBlockNumber();
        this.transactionType = ethereumTransaction.getTransactionType();
        this.fee = ethereumTransaction.getFee();
    }
}
