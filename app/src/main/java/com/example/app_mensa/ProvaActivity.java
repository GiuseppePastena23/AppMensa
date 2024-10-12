package com.example.app_mensa;

import androidx.activity.EdgeToEdge;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app_mensa.dao.User;

import java.util.concurrent.Executor;

public class ProvaActivity extends AppCompatActivity {
    private SharedPreferencesManager sharedPreferencesManager;

    private TextView textView;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        sharedPreferencesManager = new SharedPreferencesManager(this);
        user = sharedPreferencesManager.getUser();
        setContentView(R.layout.activity_prova);
        associateUI();

        textView.setText(user.toString());
    }

    private void associateUI() {
        textView = findViewById(R.id.textView);

    }

}
