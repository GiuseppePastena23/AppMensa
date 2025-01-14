package com.example.app_mensa.activities;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app_mensa.rest.QueryManager;
import com.example.app_mensa.R;
import com.example.app_mensa.util.SharedPreferencesManager;
import com.example.app_mensa.callback.TransactionCallback;
import com.example.app_mensa.dao.User;
import java.util.List;

import com.example.app_mensa.dao.Transaction;

public class TransactionHistoryActivity extends AppCompatActivity {

    private SharedPreferencesManager sharedPreferencesManager;
    private User user;

    private TextView text;


    private void associateUI() {
        text = findViewById(R.id.transactions_list);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_transactionhistory);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.transactionHistory), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sharedPreferencesManager = new SharedPreferencesManager(this);

        user = sharedPreferencesManager.getUser();
        associateUI();

        QueryManager.getTransactionsById(user.getId(), new TransactionCallback() {
            @Override
            public void onSuccess(List<Transaction> transactions) {
                // Clear the text view before appending new data
                text.setText(""); // Optional: Clear previous text

                // Collect and append the string representations of each transaction
                StringBuilder transactionStrings = new StringBuilder();
                transactions.forEach(transaction -> {
                    transactionStrings.append(transaction.toString()).append("\n"); // Append each transaction and a newline
                });

                // Append the collected string to the TextView
                text.append(transactionStrings.toString());
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(TransactionHistoryActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

    }



}
