package com.example.app_mensa;

public interface QrCallback {
    void onSuccess(String qrCodeString);
    void onError(String errorMessage);
}
