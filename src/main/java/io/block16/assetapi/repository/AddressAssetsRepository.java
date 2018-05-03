package io.block16.assetapi.repository;

import io.block16.assetapi.domain.cassandra.AddressAsset;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressAssetsRepository extends CassandraRepository<AddressAsset, String> {

}
