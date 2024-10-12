package com.example.app_mensa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app_mensa.dao.User;

public class LoginActivity extends AppCompatActivity {
    private SharedPreferencesManager sharedPreferencesManager;

    private boolean debug = false;

    private EditText emailEditText, passwordEditText;
    private CheckBox biometricCheckbox;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        sharedPreferencesManager = new SharedPreferencesManager(this);

        setContentView(R.layout.activity_login);
        associateUI();

        User savedUser = sharedPreferencesManager.getUser();
        if (savedUser != null && savedUser.getEmail() != null && savedUser.getPassword() != null) {
            networkLogin(savedUser.getEmail(), savedUser.getPassword());
        }

    }

    private void associateUI() {
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        biometricCheckbox = findViewById(R.id.biometricCheckbox);
        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(view -> loginAction());
    }

    private void loginAction() {
        // Gestione login di debug
        if (debug) {
            showToast("DEBUG FAST LOGIN");
            handleSuccessfulLogin();
            return;
        }

        // Ottieni i dati inseriti dall'utente
        String email = emailEditText.getText().toString().trim();
        String passwordChiara = passwordEditText.getText().toString().trim();
        // Controlla che i campi non siano vuoti
        if (email.isBlank() || passwordChiara.isBlank()) {
            showToast("Riempi tutti i campi");
            return;
        }
        String passwordCifrata = HashUtil.sha256(passwordChiara);

        // Esegui il login tramite rete
        networkLogin(email, passwordCifrata);
    }

    private void handleSuccessfulLogin() {
        showToast("Login effettuato con successo");
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void networkLogin(String email, String password) {
        QueryManager.doLogin(email, password, new LoginCallback() {
            @Override
            public void onSuccess(User user) {
                if (biometricCheckbox.isChecked()) {
                    sharedPreferencesManager.saveUser(user);
                } else {
                    sharedPreferencesManager.clearUser();
                }
                handleSuccessfulLogin();
            }

            @Override
            public void onError(String errorMessage) {
                sharedPreferencesManager.clearUser();
                showToast("Login fallito: " + errorMessage);
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
