package com.example.app_mensa;

import android.util.Log;

import com.example.app_mensa.dao.Transaction;
import com.example.app_mensa.dao.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;


public class QueryManager {

    public static void getTransactionsById(int id, TransactionCallback callback) {
        ApiService apiService = RetrofitClient.getApiService();

        Call<List<Transaction>> call = apiService.getTransactionById(id);

        call.enqueue(new Callback<List<Transaction>>() {
            @Override
            public void onResponse(Call<List<Transaction>> call, Response<List<Transaction>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    String errorMessage = "Error: " + response.code() + " " + response.message();
                    callback.onError(errorMessage);
                }
            }
            @Override
            public void onFailure(Call<List<Transaction>> call, Throwable t) {
                t.printStackTrace();
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public static void addCredit(int id, float value, CreditCallback callback) {
        ApiService apiService = RetrofitClient.getApiService();

        // Prepare the request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("id", id);
        requestBody.put("value", value);

        // Make the API call
        Call<ResponseBody> call = apiService.addCredit(requestBody);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {

                        String responseBody = response.body().string();
                        callback.onSuccess(responseBody);
                    } catch (IOException e) {

                        e.printStackTrace();
                        callback.onError("Failed to read response body");
                    }
                } else {

                    String errorMessage = "Error: " + response.code() + " " + response.message();
                    callback.onError(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                t.printStackTrace();
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public static void doLogin(String email, String password, LoginCallback callback) {
        HashMap<String, String> loginData = new HashMap<>();
        loginData.put("email", email);
        loginData.put("password", password);
        ApiService apiService = RetrofitClient.getApiService();
        Call<ResponseBody> call = apiService.login(loginData);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    parseResponse(response.body(), callback);
                } else {
                    handleErrorResponse(response, callback);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Login Failure", "Network or other error", t);
                callback.onError("Network or other error: " + t.getMessage());
            }
        });
    }

    public static void getTemporaryString(TemporaryStringCallback callback) {
        ApiService apiService = RetrofitClient.getApiService();

        Call<ResponseBody> call = apiService.getTemporaryString(); // Assicurati di avere questo metodo nell'ApiService

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseBody = response.body().string();
                        // Parsing del JSON
                        JSONObject jsonObject = new JSONObject(responseBody);
                        String tmpCode = jsonObject.optString("tmpCode"); // Usa "tmpCode"

                        if (!tmpCode.isEmpty()) {
                            callback.onSuccess(tmpCode);
                        } else {
                            callback.onError("Stringa temporanea non trovata nella risposta");
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        callback.onError("Errore nel parsing della risposta: " + e.getMessage());
                    }
                } else {
                    String errorMessage = "Errore: " + response.code() + " " + response.message();
                    callback.onError(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                callback.onError("Errore di rete: " + t.getMessage());
            }
        });
    }




    private static void parseResponse(ResponseBody responseBody, LoginCallback callback) {
        try {
            String bodyString = responseBody.string();
            Log.d("Login Status", "Success: " + bodyString);
            JSONObject jsonObject = new JSONObject(bodyString);
            JSONObject userObject = jsonObject.getJSONObject("user");

            User user = User.createFromJSON(userObject);
            Log.d("Login Status", "User: " + user.toString());
            callback.onSuccess(user);
        } catch (IOException | JSONException e) {
            Log.e("Login Error", "Error parsing response", e);
            callback.onError("Error parsing response");
        } finally {
            // Chiudere il corpo della risposta per liberare le risorse
            responseBody.close();
        }
    }

    private static void handleErrorResponse(Response<ResponseBody> response, LoginCallback callback) {
        Log.e("Login Error", "Invalid credentials or server error: " + response.code());
        callback.onError("Invalid credentials or server error: " + response.code());
    }
}
