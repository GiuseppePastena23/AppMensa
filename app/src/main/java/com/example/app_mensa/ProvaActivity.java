package com.example.app_mensa;

import androidx.activity.EdgeToEdge;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app_mensa.dao.User;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.concurrent.Executor;

public class ProvaActivity extends AppCompatActivity {
    private SharedPreferencesManager sharedPreferencesManager;


    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prova);

        // Initialize UI elements
        ImageView qrCodeImageView = findViewById(R.id.qrCodeImageView);

        sharedPreferencesManager = new SharedPreferencesManager(this);
        user = sharedPreferencesManager.getUser();

        // Display user information
        if (user != null) {


            // Fetch the QR code using the user's ID
            QueryManager.getQr(user.getId(), new QrCallback() {
                @Override
                public void onSuccess(String qrCodeString) {
                    String result = qrCodeString.replace("\"", "");
                    try {
                        Bitmap qrCodeBitmap = generateQRCode(qrCodeString);
                        qrCodeImageView.setImageBitmap(qrCodeBitmap);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(String errorMessage) {
                    // Handle the error message
                    Toast.makeText(ProvaActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
        }
    }

    public static Bitmap generateQRCode(String qrCodeString) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        int size = 800;
        BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeString, BarcodeFormat.QR_CODE, size, size);
        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                bitmap.setPixel(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }

        return bitmap;
    }




}
