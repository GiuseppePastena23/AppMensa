package com.example.app_mensa;

import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.Executor;

public class ProvaActivity extends AppCompatActivity {

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(ProvaActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(), "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(), "Authentication succeeded!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
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
                return;
        }

        // NONBUTTON.setOnClickListener(view -> biometricPrompt.authenticate(promptInfo));
    }
}
