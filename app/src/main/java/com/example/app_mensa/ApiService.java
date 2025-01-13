package com.example.app_mensa;

import com.example.app_mensa.dao.Transaction;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import java.util.Map;
import okhttp3.ResponseBody;
import retrofit2.http.Path;
import java.util.List;

public interface ApiService {
    @POST("/login")
    Call<ResponseBody> login(@Body Map<String, String> loginData);

    @GET("/getTmpStr")
    Call<ResponseBody> getTemporaryString();

    @POST("/newTransaction")
    Call<ResponseBody> createNewTransaction(@Body Map<String, Object> requestBody);

    @GET("/transactions/{id}")
    Call<List<Transaction>> getTransactionById(@Path("id") int id);
}