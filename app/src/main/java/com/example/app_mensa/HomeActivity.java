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

public class HomeActivity extends AppCompatActivity {

    private TextView text;
    private Button walletBtn;
    private Button settingsBtn;
    private Button menuBtn;

    private void associateUI() {
        // ASSIGN VARIABLES
        walletBtn = findViewById(R.id.wallet_btn);
        settingsBtn = findViewById(R.id.settings_btn);
        menuBtn = findViewById(R.id.menu_btn);

        // LISTENERS
        settingsBtn.setOnClickListener(v -> openSettings());
        walletBtn.setOnClickListener(v -> openWallet());
        menuBtn.setOnClickListener(v -> openMenu());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        Intent intent = getIntent();

        String email = intent.getStringExtra("email");
        String password = intent.getStringExtra("password");
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        associateUI();
    }




    // BUTTONS FUNCTIONS
    private void openWallet(){
        Intent intent = new Intent(this, WalletActivity.class);
        startActivity(intent);
    }

    private void openSettings(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void openMenu(){
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}
