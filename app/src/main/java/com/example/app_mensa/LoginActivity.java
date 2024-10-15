package com.example.app_mensa;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

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
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;

import com.example.app_mensa.dao.User;

import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {
    private SharedPreferencesManager sharedPreferencesManager;
    private User user;

    // GUI
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

        BiometricPrompt.PromptInfo promptInfo = null;
        if (savedUser != null && savedUser.getEmail() != null && savedUser.getPassword() != null) {
            // Biometria
            Executor executor = ContextCompat.getMainExecutor(this);
            BiometricPrompt biometricPrompt = new BiometricPrompt(LoginActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                    Toast.makeText(getApplicationContext(), "Errore di autenticazione biometrica: " + errString, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    Toast.makeText(getApplicationContext(), "Autenticazione biometrica riuscita", Toast.LENGTH_SHORT).show();
                    networkLogin(savedUser.getEmail(), savedUser.getPassword());
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    Toast.makeText(getApplicationContext(), "Authentication biometrica fallita", Toast.LENGTH_SHORT).show();
                }
            });

            // Controlla se il dispositivo supporta la biometria o le credenziali del dispositivo (PIN/password/pattern)
            BiometricManager biometricManager = BiometricManager.from(this);
            switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK | BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
                case BiometricManager.BIOMETRIC_SUCCESS:
                    // Biometria supportata e disponibile
                    promptInfo = new BiometricPrompt.PromptInfo.Builder()
                            .setTitle("Biometric Authentication")
                            .setSubtitle("Please authenticate to continue")
                            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_WEAK | BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                            .build();
                    break;
                case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                    // Usa solo credenziali del dispositivo se la biometria non è disponibile o non configurata
                    promptInfo = new BiometricPrompt.PromptInfo.Builder()
                            .setTitle("Device Authentication")
                            .setSubtitle("Authenticate using your device credentials")
                            .setAllowedAuthenticators(BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                            .build();
                    break;
                default:
                    // Gestisci il caso in cui non sia possibile autenticare l'utente
                    Toast.makeText(getApplicationContext(), "Authentication not supported", Toast.LENGTH_SHORT).show();
            }
            biometricPrompt.authenticate(promptInfo);
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

    private void handleSuccessfulLogin(User user) {
        showToast("Login effettuato con successo");
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("user", user);
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
                handleSuccessfulLogin(user);
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
