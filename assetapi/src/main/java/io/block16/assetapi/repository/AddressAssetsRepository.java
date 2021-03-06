package io.block16.assetapi.repository;

import io.block16.assetapi.domain.cassandra.AddressAsset;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressAssetsRepository extends CassandraRepository<AddressAsset, String> {
    List<AddressAsset> findByAddress(final String address);
}
