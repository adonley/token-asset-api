package io.block16.assetapi.domain;

import com.fasterxml.jackson.core.JsonGenerator;
// import com.fasterxml.jackson.core.type.WritableTypeId;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

import static com.fasterxml.jackson.core.JsonToken.START_OBJECT;

public class EthereumTransactionSerializer extends StdSerializer<EthereumTransaction> {

    public EthereumTransactionSerializer() {
        this(EthereumTransaction.class);
    }

    public EthereumTransactionSerializer(Class<EthereumTransaction> t) {
        super(t);
    }

    public EthereumTransactionSerializer(JavaType type) {
        super(type);
    }

    public EthereumTransactionSerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }

    public EthereumTransactionSerializer(StdSerializer<?> src) {
        super(src);
    }

    @Override
    public void serialize(EthereumTransaction value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStringField("value", value.getValue());
        gen.writeStringField("toAddress", value.getToAddress());
        gen.writeStringField("fromAddress", value.getFromAddress());
        gen.writeStringField("ethereumContract", value.getEthereumContract());
        gen.writeStringField("transactionHash", value.getTransactionHash());
        gen.writeNumberField("blockNumber", value.getBlockNumber());
        gen.writeNumberField("time", value.getTime().getTime());
        gen.writeNumberField("transactionType", value.getTransactionType().ordinal());
        gen.writeStringField("fee", value.getFee());
    }

    @Override
    public void serializeWithType(EthereumTransaction value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
        /*WritableTypeId typeId = typeSer.typeId(value, START_OBJECT);
        typeSer.writeTypePrefix(gen, typeId);
        serialize(value, gen, serializers);
        typeSer.writeTypeSuffix(gen, typeId); */
    }
}
