package com.example.app_mensa;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SettingsActivity extends AppCompatActivity  {
    private Button exitBtn;
    private Button anagraficaBtn;
    private Button cardsBtn;
    private Button statusBtn;
    private Button authBtn;



    private void associateUI(){
        // ASSIGN VARIABLES
        exitBtn = findViewById(R.id.exit_btn);
        anagraficaBtn = findViewById(R.id.anagrafica_btn);
        cardsBtn = findViewById(R.id.cards_btn);
        statusBtn = findViewById(R.id.status_btn);
        authBtn = findViewById(R.id.auth_btn);


        // LISTENERS
        exitBtn.setOnClickListener(v -> doExit());
        anagraficaBtn.setOnClickListener(v -> openAnagrafica());
        cardsBtn.setOnClickListener(v -> openCards());
        statusBtn.setOnClickListener(v -> openStatus());
        authBtn.setOnClickListener(v -> openAuth());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        associateUI();
    }

    // BUTTONS FUNCTIONS
    private void doExit(){
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
        // Rimuovere file di autenticazione
    }

    private void openAnagrafica() {
        Intent intent = new Intent(this, AnagraficaActivity.class);
        startActivity(intent);
    }

    private void openCards() {
        Toast.makeText(this, "not implemented", Toast.LENGTH_SHORT).show();
    }

    private void openStatus() {
        Toast.makeText(this, "not implemented", Toast.LENGTH_SHORT).show();
    }

    private void openAuth() {
        Toast.makeText(this, "not implemented", Toast.LENGTH_SHORT).show();
    }
}

