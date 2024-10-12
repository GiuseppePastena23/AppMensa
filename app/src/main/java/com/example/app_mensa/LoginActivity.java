package com.example.app_mensa;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app_mensa.dao.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private boolean debug = false;

    public User user = null;

    private EditText emailEditText, passwordEditText;
    private CheckBox biometricCheckbox;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);

        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(this);
        User user = sharedPreferencesManager.getUser();
        if (user != null && user.getEmail() != null) {
            handleSuccessfulLogin();
        }

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
        String email = emailEditText.getText().toString().trim();
        String passwordChiara = passwordEditText.getText().toString().trim();

        // Gestione login di debug
        if (debug) {
            showToast("DEBUG FAST LOGIN");
            handleSuccessfulLogin();
            return;
        }

        // Controlla che i campi non siano vuoti
        if (email.isEmpty() || passwordChiara.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Riempi tutti i campi", Toast.LENGTH_SHORT).show();
            return;
        }

        // Esegui il login tramite rete
        performNetworkLogin(email, passwordChiara);
    }

    private void performNetworkLogin(String email, String password) {
        HashMap<String, String> loginData = new HashMap<>();
        loginData.put("email", email);
        loginData.put("password", password);

        ApiService apiService = RetrofitClient.getApiService();
        Call<ResponseBody> call = apiService.login(loginData);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseBody = response.body().string();
                        Log.d("Login Status", "Success: " + responseBody);
                        JSONObject jsonObject = new JSONObject(responseBody);
                        JSONObject userObject = jsonObject.getJSONObject("user");

                        user = User.createFromJSON(userObject);
                        Log.d("Login Status", "User: " + user.toString());

                        if (biometricCheckbox.isChecked()) {
                            saveUserData();
                        }

                        handleSuccessfulLogin();
                    } catch (IOException | JSONException e) {
                        Log.e("Login Error", "Error parsing response", e);
                        showToast("Errore nella risposta del server.");
                    }
                } else {
                    Log.e("Login Error", "Invalid credentials or server error: " + response.code());
                    showToast("Email o password errate.");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Login Failure", "Network or other error", t);
                showToast("Errore di rete. Riprova più tardi.");
            }
        });
    }

    private void saveUserData() {
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(this);
        sharedPreferencesManager.saveUser(user);
    }



    private void handleSuccessfulLogin() {
        Toast.makeText(LoginActivity.this, "Login effettuato con successo", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void showToast(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }

}
