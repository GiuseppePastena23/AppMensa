package com.example.app_mensa;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.app_mensa.dao.User;

public class AddCreditActivity extends AppCompatActivity {

    private SharedPreferencesManager sharedPreferencesManager;
    private User user;

    private Button googlePayBtn;
    private Button paypalBtn;
    private Button creditCardBtn;
    private Button debugBtn;
    private Button annullaButton;


    private void associateUI() {
        googlePayBtn = findViewById(R.id.googlepay_btn);
        paypalBtn = findViewById(R.id.paypal_btn);
        creditCardBtn = findViewById(R.id.creditCard_btn);
        debugBtn = findViewById(R.id.debug_btn);
        annullaButton = findViewById(R.id.annulla_button);

        googlePayBtn.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(), "Not Implemented", Toast.LENGTH_SHORT).show();
        });

        paypalBtn.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(), "Not Implemented", Toast.LENGTH_SHORT).show();
        });

        creditCardBtn.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(), "Not Implemented", Toast.LENGTH_SHORT).show();
        });

        debugBtn.setOnClickListener(view -> debugPayment());

        annullaButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), WalletActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_addcredit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.addCredit), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sharedPreferencesManager = new SharedPreferencesManager(this);

        user = sharedPreferencesManager.getUser();
        associateUI();

    }


    private void debugPayment() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Credit");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        builder.setView(input);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String creditAmount = input.getText().toString();
            if (!creditAmount.isEmpty()) {
                addCreditToUser(user.getId(), Float.parseFloat(creditAmount));
            } else {
                Toast.makeText(this, "Please enter a value", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void addCreditToUser(int userId, float value) {
        QueryManager.createNewTransaction(userId, value, new CreditCallback() {

            @Override
            public void onSuccess(String responseBody) {
                Toast.makeText(getApplicationContext(), "Credit added successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), WalletActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String errorMessage) {
                // Handle error (display the error message to the user)
                Toast.makeText(getApplicationContext(), "Error: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });

    }
}
