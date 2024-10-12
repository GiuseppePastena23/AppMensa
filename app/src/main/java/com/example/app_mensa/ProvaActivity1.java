package com.example.app_mensa;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class ProvaActivity1 extends AppCompatActivity {

    private WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prova);
        /*
        webView = findViewById(R.id.web_view);
        String pdf = "https://www.adisurcampania.it/sites/default/files/2024-10/Pranzo_4.pdf";
        webView.getSettings().setJavaScriptEnabled(true);
        //webView.loadUrl("https://www.adisurcampania.it/sites/default/files/2024-10/Pranzo_4.pdf");
        /*
        for (int i = 0; i < 10; i++) {
        }
        webView.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url=" + pdf);
        Log.d("caricamentosito", "URL:" + webView.getUrl());
        webView.get
        */



        //webView.loadUrl("https://www.google.com");

        /*
        String url = "https://www.adisurcampania.it/sites/default/files/2024-10/Pranzo_4.pdf";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
        finish();
        */
    }
}