package io.block16.assetapi.domain;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.sql.Timestamp;

public class EthereumTransactionDeserializer extends StdDeserializer<EthereumTransaction> {

    public EthereumTransactionDeserializer() {
        this(EthereumTransaction.class);
    }

    public EthereumTransactionDeserializer(Class<?> vc) {
        super(vc);
    }

    public EthereumTransactionDeserializer(JavaType valueType) {
        super(valueType);
    }

    @Override
    public EthereumTransaction deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        EthereumTransaction et = new EthereumTransaction();
        JsonNode node = p.getCodec().readTree(p);
        et.setValue(node.get("value").asText());
        et.setToAddress(node.get("toAddress").asText());
        et.setFromAddress(node.get("fromAddress").asText());
        et.setEthereumContract(node.get("ethereumContract").asText());
        et.setTransactionHash(node.get("transactionHash").asText());
        et.setBlockNumber(node.get("blockNumber").asLong());
        et.setTime(new Timestamp(node.get("time").asLong()));
        int ordnal = node.get("transactionType").asInt();
        et.setFee(node.get("fee").asText());
        et.setTransactionType(EthereumTransactionType.values()[ordnal]);
        return et;
    }
}
