package com.example.app_mensa;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app_mensa.dao.User;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;

import java.util.Random;

public class ProvaActivity extends AppCompatActivity {
    private SharedPreferencesManager sharedPreferencesManager;
    private TextView textView;
    private ImageView qrCodeImageView;
    private Handler handler;
    private Runnable runnable;
    private User user;

    private int tmpCodeRefreshTime = 2000;
    private Random random = new Random();

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
                    QueryManager.getTemporaryString(new TemporaryStringCallback() {
                        @Override
                        public void onSuccess(String temporaryString) {
                            // Aggiorna l'interfaccia utente con la stringa temporanea
                            runOnUiThread(() -> {
                                textView.setText(temporaryString);
                                Log.d("ProvaActivity", "Stringa temporanea ottenuta: " + temporaryString);
                                textView.setTextColor(Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
                            });
                        }

                        @Override
                        public void onError(String errorMessage) {
                            // Gestisci l'errore
                            runOnUiThread(() -> {
                                Toast.makeText(ProvaActivity.this, "Errore: " + errorMessage, Toast.LENGTH_SHORT).show();
                            });
                        }
                    });
                }

                handler.postDelayed(this, tmpCodeRefreshTime);
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Inizia il ciclo di login periodico
        handler.post(runnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Ferma il ciclo quando l'attività va in background
        handler.removeCallbacks(runnable);
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

        int mainColor = 0xFF000000;
        int bgColor = 0x000000;
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                bitmap.setPixel(x, y, bitMatrix.get(x, y) ? mainColor : bgColor);
            }
        }

        return bitmap;
    }
}
