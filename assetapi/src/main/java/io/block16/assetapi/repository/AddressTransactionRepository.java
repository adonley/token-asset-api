package io.block16.assetapi.repository;

import io.block16.assetapi.domain.cassandra.AddressTransaction;
import io.block16.assetapi.domain.cassandra.TransactionKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AddressTransactionRepository extends CassandraRepository<AddressTransaction, TransactionKey> {
    @Query("select * from transactions WHERE address=?0 LIMIT 20")
    List<AddressTransaction> findTopByKeyAddressLimit(String address);

    @Query("select * from transactions WHERE address = ?0 and transaction_date > ?1 LIMIT 10")
    List<AddressTransaction> findByAddressAfter(String address, Date after);

    @Query("select * from transactions WHERE address=?0 LIMIT 10")
    List<AddressTransaction> findTop10ByAddress(String address);
}
