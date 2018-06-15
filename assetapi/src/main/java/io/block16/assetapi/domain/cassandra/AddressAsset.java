package io.block16.assetapi.domain.cassandra;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import static org.springframework.data.cassandra.core.cql.PrimaryKeyType.CLUSTERED;
import static org.springframework.data.cassandra.core.cql.PrimaryKeyType.PARTITIONED;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("assets")
public class AddressAsset {
    @PrimaryKeyColumn(name = "address", type = PARTITIONED)
    private String address;

    @PrimaryKeyColumn(name = "contract_address", ordinal = 0, type = CLUSTERED)
    private String contractAddress;
}
