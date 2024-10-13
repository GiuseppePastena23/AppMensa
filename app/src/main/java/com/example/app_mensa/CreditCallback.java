package com.example.app_mensa;
import com.example.app_mensa.dao.User;

public interface CreditCallback {
    void onSuccess(String responseBody);
    void onError(String errorMessage);
}
