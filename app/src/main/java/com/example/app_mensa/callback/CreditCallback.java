package com.example.app_mensa.callback;

public interface CreditCallback {
    void onSuccess(String responseBody);
    void onError(String errorMessage);
}
