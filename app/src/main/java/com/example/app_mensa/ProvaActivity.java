package com.example.app_mensa;
import com.example.app_mensa.dao.User;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.util.HashMap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import okhttp3.ResponseBody;

public class ProvaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Crea una HashMap per i parametri della richiesta
        HashMap<String, String> loginData = new HashMap<>();
        loginData.put("email", "mario.rossi@example.com");
        loginData.put("password", "a");

        // Ottieni un'istanza di Retrofit e il servizio API
        ApiService apiService = RetrofitClient.getApiService();

        // Fai la richiesta di login
        Call<ResponseBody> call = apiService.login(loginData);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        // Stampa la risposta del server
                        String responseBody = response.body().string();
                        Log.d("Login Success", "Response: " + responseBody);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // La richiesta non è andata a buon fine
                    Log.e("Login Error", "Invalid credentials");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Errore di rete o altro errore
                Log.e("Login Failure", t.getMessage());
            }
        });
    }
}