package com.example.app_mensa;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import java.util.Map;
import okhttp3.ResponseBody;

public interface ApiService {
    @POST("/login")
    Call<ResponseBody> login(@Body Map<String, String> loginData);
}