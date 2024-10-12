package com.example.app_mensa;

import android.util.Log;

import com.example.app_mensa.dao.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QueryManager {

    public static void getQr(int id, LoginCallback callback) {
        ApiService apiService = RetrofitClient.getApiService();
        Call<ResponseBody> call = apiService.getQr(id);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    ResponseBody qrData = response.body();

                    if (qrData != null) {
                        try {
                            String qrCodeString = qrData.string(); // Convert to String
                            Log.d("QR", qrCodeString);
                            callback.onSuccess(); // Notify success
                        } catch (IOException e) {
                            e.printStackTrace();
                            callback.onError("Failed to parse QR code data."); // Notify error
                        }
                    } else {
                        callback.onError("QR data is null.");
                    }
                } else {
                    callback.onError("Error: " + response.code() + " " + response.message()); // Notify error
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                callback.onError("Network request failed: " + t.getMessage()); // Notify error
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
