package com.example.app_mensa.activities;

import com.example.app_mensa.rest.QueryManager;
import com.example.app_mensa.R;
import com.example.app_mensa.util.SharedPreferencesManager;
import com.example.app_mensa.callback.LoginCallback;
import com.example.app_mensa.dao.User;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AnagraficaActivity extends AppCompatActivity {

    private User user;
    private SharedPreferencesManager sharedPreferencesManager;

    private TextView textViewNome;
    private TextView textViewCognome;
    private TextView textViewCF;
    private TextView textViewEmail;
    private TextView textViewPhone;
    private TextView textViewStatus;
    private TextView textViewCredit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferencesManager = new SharedPreferencesManager(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anagrafica);
        associateUI();
        user = sharedPreferencesManager.getUser();
        if (user != null) {
            QueryManager.doLogin(user.getEmail(), user.getPassword(), new LoginCallback() {
                @Override
                public void onSuccess(User updatedUser) {
                    sharedPreferencesManager.clearUser();
                    sharedPreferencesManager.saveUser(updatedUser);
                    loadUserData();
                }

                @Override
                public void onError(String errorMessage) {
                    Toast.makeText(AnagraficaActivity.this, "Errore nel login: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        }


    }


    private void associateUI() {
        textViewNome = findViewById(R.id.text_view_nome);
        textViewCognome = findViewById(R.id.text_view_cognome);
        textViewCF = findViewById(R.id.text_view_cf);
        textViewEmail = findViewById(R.id.text_view_email);
        textViewPhone = findViewById(R.id.text_view_phone);
        textViewStatus = findViewById(R.id.text_view_status);
        textViewCredit = findViewById(R.id.text_view_credit);
    }

    private void loadUserData() {
        // Simulating data fetching (replace this with actual data retrieval logic)
        String nome = user.getNome();
        String cognome = user.getCognome();
        String cf = user.getCf();
        String email = user.getEmail();
        String password = user.getPassword();
        String phone = user.getTelefono();
        String status = user.getStatus();
        String credit = String.valueOf(user.getCredito());

        // Set the fetched data to the TextViews
        textViewNome.setText(nome);
        textViewCognome.setText(cognome);
        textViewCF.setText(cf);
        textViewEmail.setText(email);

        textViewPhone.setText(phone);
        textViewStatus.setText(status);
        textViewCredit.setText(credit);
    }


}
