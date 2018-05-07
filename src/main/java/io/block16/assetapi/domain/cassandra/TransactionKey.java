package io.block16.assetapi.domain.cassandra;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import static org.springframework.data.cassandra.core.cql.Ordering.DESCENDING;
import static org.springframework.data.cassandra.core.cql.PrimaryKeyType.CLUSTERED;
import static org.springframework.data.cassandra.core.cql.PrimaryKeyType.PARTITIONED;

import java.io.Serializable;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyClass
public class TransactionKey implements Serializable {
    @PrimaryKeyColumn(name = "address", type = PARTITIONED)
    private String address;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @PrimaryKeyColumn(name = "transaction_date", ordinal = 0, type = CLUSTERED, ordering = DESCENDING)
    private Date transactionDate;

    @PrimaryKeyColumn(name = "tx_index", ordinal = 1, type = CLUSTERED)
    private Integer txIndexKey;

    @PrimaryKeyColumn(name = "tx_log_index", ordinal = 2, type = CLUSTERED)
    private Integer txLogIndex;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        TransactionKey that = (TransactionKey) o;

        if (!address.equals(that.address)) return false;
        if (!transactionDate.equals(that.transactionDate)) return false;
        if (!txIndexKey.equals(that.txIndexKey)) return false;
        return txLogIndex.equals(that.txLogIndex);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + address.hashCode();
        result = 31 * result + transactionDate.hashCode();
        result = 31 * result + txIndexKey.hashCode();
        result = 31 * result + txLogIndex.hashCode();
        return result;
    }
}
