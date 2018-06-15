package io.block16.assetapi.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@JsonSerialize(using = EthereumTransactionSerializer.class)
@JsonDeserialize(using = EthereumTransactionDeserializer.class)
public class EthereumTransaction {
    private String value;
    private String toAddress;
    private String fromAddress;
    private String ethereumContract;
    private String transactionHash;
    private Long blockNumber;
    private Timestamp time;
    private EthereumTransactionType transactionType;
    private String fee;

    private Integer txIndex;
    private Integer logIndex;
}
