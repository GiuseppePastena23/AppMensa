package com.example.app_mensa;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_mensa.TransactionAdapter;
import com.example.app_mensa.dao.Transaction;
import com.example.app_mensa.dao.User;
import com.example.app_mensa.QueryManager;
import com.example.app_mensa.TransactionCallback;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private User user;
    private List<Transaction> transactionList = new ArrayList<>();

    // GUI
    private TextView welcomeText;
    private TextView creditoText;
    private RecyclerView transactionRecyclerView;
    private TransactionAdapter transactionAdapter;

    // Handler e Runnable per aggiornare periodicamente
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable updateTransactionsRunnable = new Runnable() {
        @Override
        public void run() {
            fetchTransactions();
            // Esegue di nuovo il runnable dopo 5 secondi
            handler.postDelayed(this, 5000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Recupera l'oggetto User dall'intent
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");

        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        associateUI();
        // Avvia l'aggiornamento periodico
        handler.post(updateTransactionsRunnable);
    }

    private void associateUI() {
        welcomeText = findViewById(R.id.welcome_text);
        welcomeText.setText(welcomeText.getText() + user.getNome());

        creditoText = findViewById(R.id.credito_text);
        creditoText.setText(creditoText.getText() + String.valueOf(user.getCredito()) + "€");

        QrCodeFragment qrCodeFragment = QrCodeFragment.newInstance(user);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.qr_container, qrCodeFragment)
                .commit();

        // Configura RecyclerView
        transactionRecyclerView = findViewById(R.id.transaction_recycler_view);
        transactionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        transactionAdapter = new TransactionAdapter(transactionList);
        transactionRecyclerView.setAdapter(transactionAdapter);
    }

    private void fetchTransactions() {
        QueryManager.getTransactionsById(user.getId(), new TransactionCallback() {
            @Override
            public void onSuccess(List<Transaction> transactions) {
                transactionList.clear();
                transactionList.addAll(transactions);
                transactionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(HomeActivity.this, "Errore nel recupero delle transazioni: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Ferma l'handler quando l'activity viene distrutta
        handler.removeCallbacks(updateTransactionsRunnable);
    }
}
