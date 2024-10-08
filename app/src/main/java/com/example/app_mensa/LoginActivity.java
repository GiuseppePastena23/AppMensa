package com.example.app_mensa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private CheckBox biometricCheckbox;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        associateUI();
    }


    private void associateUI() {
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        biometricCheckbox = findViewById(R.id.biometricCheckbox);
        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(view -> loginAction());
    }

    private void loginAction() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Riempi tutti i campi", Toast.LENGTH_SHORT).show();
        } else if (email.equals("admin") && password.equals("admin")) {
            Toast.makeText(LoginActivity.this, "Login effettuato con successo", Toast.LENGTH_SHORT).show();
            // SUCCESS
        } else {
            Toast.makeText(LoginActivity.this, "Email o password errate", Toast.LENGTH_SHORT).show();
        }

        // TODO: debug fast login (remove here and bring up)
        Toast.makeText(LoginActivity.this, "FAST LOGIN DEBUG", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

}
