package com.example.app_mensa;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;



public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText email;
    private EditText password;

    private void initUI() {
        loginButton = findViewById(R.id.login_button);
        email = findViewById(R.id.email_edit);
        password = findViewById(R.id.password_edit);
    }

    private void login(String email, String password){
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("password", password);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initUI();
        loginButton.setOnClickListener(v -> login(email.getText().toString(), password.getText().toString()));
    }

}
