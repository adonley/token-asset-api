package io.block16.assetapi.service;

import io.block16.assetapi.domain.EthereumTransaction;
import io.block16.assetapi.domain.EthereumTransactionType;
import io.block16.assetapi.domain.cassandra.AddressAsset;
import io.block16.assetapi.domain.cassandra.AddressTransaction;
import io.block16.assetapi.domain.cassandra.TransactionKey;
import io.block16.assetapi.repository.AddressAssetsRepository;
import io.block16.assetapi.repository.AddressTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class AddressTransactionService {

    private final AddressTransactionRepository addressTransactionRepository;
    private final AddressAssetsRepository addressAssetsRepository;

    @Autowired
    public AddressTransactionService(
            final AddressTransactionRepository addressTransactionRepository,
            final AddressAssetsRepository addressAssetsRepository
    ) {
        this.addressTransactionRepository = addressTransactionRepository;
        this.addressAssetsRepository = addressAssetsRepository;
    }

    public void save(List<EthereumTransaction> ethereumTransactions) {

        List<AddressTransaction> dbTxs = new ArrayList<>(2 * ethereumTransactions.size());
        List<AddressAsset> addressAssets = new LinkedList<>();

        for(EthereumTransaction e: ethereumTransactions) {
            boolean isTokenTx = e.getTransactionType() == EthereumTransactionType.TOKEN_TRANSACTION;

            // toAddress
            if(e.getToAddress() != null) {
                TransactionKey transactionKey = new TransactionKey(
                        e.getToAddress(),
                        new Date(e.getTime().getTime()),
                        e.getTxIndex(),
                        e.getLogIndex()
                );

                dbTxs.add(new AddressTransaction(transactionKey, e));

                if(isTokenTx) {
                    addressAssets.add(new AddressAsset(e.getToAddress(), e.getEthereumContract()));
                }
            }

            // fromAddress
            if(e.getFromAddress() != null) {
                TransactionKey transactionKey = new TransactionKey(
                        e.getFromAddress(),
                        new Date(e.getTime().getTime()),
                        e.getTxIndex(),
                        e.getLogIndex()
                );

                dbTxs.add(new AddressTransaction(transactionKey, e));

                if(isTokenTx) {
                    addressAssets.add(new AddressAsset(e.getFromAddress(), e.getEthereumContract()));
                }
            }
        }

        addressTransactionRepository.saveAll(dbTxs);
        addressAssetsRepository.saveAll(addressAssets);
    }
}
