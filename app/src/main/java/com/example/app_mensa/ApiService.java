package com.example.app_mensa;
import com.example.app_mensa.dao.User;

import retrofit2.Call;
import retrofit2.http.GET;
import java.util.List;

public interface ApiService {
    @GET("data/")
    Call<List<User>> getUsers();
}
