package com.example.app_mensa;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import java.util.Map;
import okhttp3.ResponseBody;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("/login")
    Call<ResponseBody> login(@Body Map<String, String> loginData);

    @GET("/qr/{id}")
    Call<ResponseBody> getQr(@Path("id") int id);
}