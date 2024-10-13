package com.example.app_mensa;
import java.util.List;


import com.example.app_mensa.dao.Transaction;

public interface TransactionCallback {
    void onSuccess(List<Transaction> transactionList);
    void onError(String errorMessage);
}
