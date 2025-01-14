package com.example.app_mensa.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_mensa.R;
import com.example.app_mensa.dao.Transaction;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private List<Transaction> transactionList;

    public TransactionAdapter(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        holder.transactionDetail.setText(transaction.getId() + " - " + transaction.getModalita());
        holder.transactionAmount.setText(String.valueOf(transaction.getImporto()) + "€");
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView transactionDetail;
        public TextView transactionAmount;

        public ViewHolder(View view) {
            super(view);
            transactionDetail = view.findViewById(R.id.transaction_detail);
            transactionAmount = view.findViewById(R.id.transaction_amount);
        }
    }
}
