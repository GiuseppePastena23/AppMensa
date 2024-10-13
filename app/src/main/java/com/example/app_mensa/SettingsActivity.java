package com.example.app_mensa;

import android.annotation.SuppressLint;
import android.content.Intent;
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

public class SettingsActivity extends AppCompatActivity  {
    private SharedPreferencesManager sharedPreferencesManager;
    private User user;

    private Button exitBtn;
    private Button anagraficaBtn;
    private Button cardsBtn;
    private Button statusBtn;
    private Button authBtn;

    private TextView usernameTxt;


    private void associateUI(){
        // ASSIGN VARIABLES
        exitBtn = findViewById(R.id.exit_btn);
        anagraficaBtn = findViewById(R.id.anagrafica_btn);
        cardsBtn = findViewById(R.id.cards_btn);
        statusBtn = findViewById(R.id.status_btn);
        authBtn = findViewById(R.id.auth_btn);

        usernameTxt = findViewById(R.id.username_txt);


        // LISTENERS
        exitBtn.setOnClickListener(v -> doExit());
        anagraficaBtn.setOnClickListener(v -> openAnagrafica());
        cardsBtn.setOnClickListener(v -> openCards());
        statusBtn.setOnClickListener(v -> openStatus());
        authBtn.setOnClickListener(v -> openAuth());
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        sharedPreferencesManager = new SharedPreferencesManager(this);
        user = sharedPreferencesManager.getUser();



        setContentView(R.layout.activity_settings);
        associateUI();
        usernameTxt.setText(user.getNome() + " " + user.getCognome());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    // BUTTONS FUNCTIONS
    private void doExit(){
        // Rimuovere file di autenticazione
        sharedPreferencesManager.clearUser();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
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

