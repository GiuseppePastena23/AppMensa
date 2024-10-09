package com.example.app_mensa;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        Intent intent = new Intent(this, ProvaActivity.class);
        startActivity(intent);
        finish();
    }
}