package com.example.app_mensa;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app_mensa.dao.User;

public class HomeActivity extends AppCompatActivity {

    private User user;

    // GUI
    private TextView welcomeText;
    private TextView creditoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Recupera l'oggetto User dall'intent
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");

        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        associateUI();


        // Passa l'oggetto User al QrCodeFragment
        QrCodeFragment qrCodeFragment = QrCodeFragment.newInstance(user);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.qr_container, qrCodeFragment)
                .commit();

    }

    private void associateUI() {
        welcomeText = findViewById(R.id.welcome_text);
        welcomeText.setText(welcomeText.getText() + user.getNome());

        creditoText = findViewById(R.id.credito_text);
        creditoText.setText(creditoText.getText() +  String.valueOf(user.getCredito()) + "€");

    }
}
