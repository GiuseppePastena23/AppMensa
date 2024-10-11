package com.example.app_mensa;
import com.example.app_mensa.dao.User;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProvaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ApiService apiService = RetrofitClient.getApiService();
        Call<List<User>> call = apiService.getUsers();
        Log.d("MainActivity", "URL: " + call.request().url());

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                System.out.println(response.body());
                if (response.isSuccessful() && response.body() != null) {

                    List<User> users = response.body();
                    for (User user : users) {
                        Log.d("MainActivity", "User: " + user.getNome());
                    }
                } else {
                    Toast.makeText(ProvaActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(ProvaActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                Log.e("MainActivity", t.getMessage());
                t.printStackTrace();
            }
        });
    }
}
