package com.example.app_mensa;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app_mensa.dao.User;
import com.google.zxing.WriterException;

public class WalletActivity extends AppCompatActivity {

    private float saldo = 0.0f;
    private TextView saldoText;
    private Button addBtn;
    private Button historyBtn;
    private SharedPreferencesManager sharedPreferencesManager;
    private User user;

    private void associateUI(){
        // ASSIGN VARIABLES
        saldoText = findViewById(R.id.saldo_text);
        saldoText.setText(String.valueOf(saldo));
        addBtn = findViewById(R.id.add_button);
        historyBtn = findViewById(R.id.history_btn);

        // LISTENERS
        addBtn.setOnClickListener(v -> addMoney());
        historyBtn.setOnClickListener(v -> openHistory());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_wallet);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.wallet), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sharedPreferencesManager = new SharedPreferencesManager(this);
        user = sharedPreferencesManager.getUser();
        associateUI();
        if (user != null) {
            QueryManager.doLogin(user.getEmail(), user.getPassword(), new LoginCallback() {
                @Override
                public void onSuccess(User updatedUser) {
                    sharedPreferencesManager.clearUser();
                    sharedPreferencesManager.saveUser(updatedUser);

                    float saldoUser = updatedUser.getCredito();
                    String txtToShow = "€ " + String.valueOf(saldoUser);
                    saldoText.setText(txtToShow);
                }

                @Override
                public void onError(String errorMessage) {
                    Toast.makeText(WalletActivity.this, "Errore nel login: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // BUTTONS FUNCTIONS
    private void addMoney() {
        Intent intent = new Intent(this, AddCreditActivity.class);
        startActivity(intent);
        finish();
    }

    private void openHistory(){
        Intent intent = new Intent(this, TransactionHistoryActivity.class);
        startActivity(intent);
    }
}
