package com.example.app_mensa;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_mensa.dao.User;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;

public class ProvaActivity extends AppCompatActivity {
    private SharedPreferencesManager sharedPreferencesManager;
    private TextView textView;
    private ImageView qrCodeImageView;
    private Handler handler;
    private Runnable runnable;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prova);

        textView = findViewById(R.id.textView);
        qrCodeImageView = findViewById(R.id.qrCodeImageView);

        sharedPreferencesManager = new SharedPreferencesManager(this);
        user = sharedPreferencesManager.getUser();

        handler = new Handler(Looper.getMainLooper());

        runnable = new Runnable() {
            @Override
            public void run() {
                if (user != null) {
                    QueryManager.doLogin(user.getEmail(), user.getPassword(), new LoginCallback() {
                        @Override
                        public void onSuccess(User updatedUser) {
                            sharedPreferencesManager.clearUser();
                            sharedPreferencesManager.saveUser(updatedUser);

                            try {
                                Bitmap qrCodeBitmap = generateQRCode(updatedUser.getTmpCode());
                                textView.setText(updatedUser.getTmpCode());
                                qrCodeImageView.setImageBitmap(qrCodeBitmap);
                            } catch (WriterException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(String errorMessage) {
                            Toast.makeText(ProvaActivity.this, "Errore nel login: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                // Ripeti il task ogni 10 secondi
                handler.postDelayed(this, 10000);
            }
        };

        // Inizia il ciclo di login periodico
        handler.post(runnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Ferma il ciclo quando l'attività viene distrutta
        handler.removeCallbacks(runnable);
    }

    public static Bitmap generateQRCode(String qrCodeString) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        int size = 800;
        BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeString, BarcodeFormat.QR_CODE, size, size);
        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

        int greenColor = 0xFF00FF00; // Colore verde per i quadrati QR
        int transparentColor = 0x00000000; // Trasparente per lo sfondo

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                bitmap.setPixel(x, y, bitMatrix.get(x, y) ? greenColor : transparentColor);
            }
        }

        return bitmap;
    }
}
