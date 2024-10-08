package com.example.app_mensa;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class WalletActivity extends AppCompatActivity {

    private float saldo = 0.0f;
    private TextView saldoText;
    private Button addBtn;

    private void associateUI(){
        // ASSIGN VARIABLES
        saldoText = findViewById(R.id.saldo_text);
        saldoText.setText(String.valueOf(saldo));
        addBtn = findViewById(R.id.add_button);

        // LISTENERS
        addBtn.setOnClickListener(v -> addMoney(1000.5232f));}

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
        associateUI();
    }

    // BUTTONS FUNCTIONS
    private void addMoney(float value) {
        saldo += value;
        saldoText.setText(String.valueOf(saldo));
    }
}
