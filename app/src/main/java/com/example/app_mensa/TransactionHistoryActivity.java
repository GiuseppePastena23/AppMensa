package com.example.app_mensa;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app_mensa.dao.User;
import java.util.List;
import org.w3c.dom.Text;
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
