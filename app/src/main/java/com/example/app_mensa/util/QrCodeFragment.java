package com.example.app_mensa.util;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.app_mensa.R;
import com.example.app_mensa.dao.User;
import com.example.app_mensa.rest.QueryManager;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;

public class QrCodeFragment extends Fragment {

    private ImageView qrCodeImageView;
    private Handler handler;
    private Runnable runnable;
    private User user;
    private final int tmpCodeRefreshTime = 2000;

    public static QrCodeFragment newInstance(User user) {
        QrCodeFragment fragment = new QrCodeFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Recupera l'oggetto User dal Bundle
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable("user");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qr_code, container, false);
        qrCodeImageView = view.findViewById(R.id.qrCodeImageView);

        handler = new Handler(Looper.getMainLooper());
        runnable = new Runnable() {
            @Override
            public void run() {
                if (user != null) {

                        @Override
                        public void onSuccess(String temporaryString) {
                            requireActivity().runOnUiThread(() -> {
                                Log.d("QrCodeFragment", "Stringa temporanea ottenuta: " + temporaryString);
                                try {
                                    Bitmap qrBitmap = generateQrCode(user.getId() + ":" + temporaryString);
                                    qrCodeImageView.setImageBitmap(qrBitmap);
                                } catch (WriterException e) {
                                    e.printStackTrace();
                                    showErrorImage();
                                }
                            });
                        }

                        @Override
                        public void onError(String errorMessage) {
                            requireActivity().runOnUiThread(() -> {
                                Log.e("QrCodeFragment", "Errore: " + errorMessage);
                                showErrorImage();
                            });
                        }
                    });
                }

                handler.postDelayed(this, tmpCodeRefreshTime);
            }
        };

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Inizia il ciclo di aggiornamento periodico
        handler.post(runnable);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Ferma il ciclo quando il frammento va in background
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Ferma il ciclo quando il frammento viene distrutto
        handler.removeCallbacks(runnable);
    }

    private Bitmap generateQrCode(String qrCodeString) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        int size = 800;
        BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeString, BarcodeFormat.QR_CODE, size, size);
        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

        int mainColor = getResources().getColor(R.color.qr_code_color_dark, null);
        int bgColor = Color.TRANSPARENT;

        // Genera il QR code senza background
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                bitmap.setPixel(x, y, bitMatrix.get(x, y) ? mainColor : bgColor);
            }
        }

        return bitmap;
    }


    private void showErrorImage() {
        qrCodeImageView.setImageResource(R.drawable.ic_no_internet);
    }
}
