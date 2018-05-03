package io.block16.assetapi.repository;

import io.block16.assetapi.domain.cassandra.AddressTransaction;
import io.block16.assetapi.domain.cassandra.TransactionKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressTransactionRepository extends CassandraRepository<AddressTransaction, TransactionKey> {
    @Query("select * from transactions WHERE address=?0 LIMIT 10")
    List<AddressTransaction> findTop10ByKeyAddressLimit(String address);
}
