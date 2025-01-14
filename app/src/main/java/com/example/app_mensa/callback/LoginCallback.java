package com.example.app_mensa.callback;
import com.example.app_mensa.dao.User;

public interface LoginCallback {
    void onSuccess(User user);
    void onError(String errorMessage);
}
