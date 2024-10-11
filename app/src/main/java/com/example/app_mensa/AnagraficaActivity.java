package com.example.app_mensa;

import com.example.app_mensa.dao.User;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Date;

public class AnagraficaActivity extends AppCompatActivity {

    private User user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anagrafica);
        user = getUserInfo();
        associateUI();
    }


    private void associateUI() {
        // recyclerView.add
    }

    private User getUserInfo() {
        return new User(1, "Rossi", "Rossi", "RSSMRA556JSAIUDAS", "mario@mail.com",
                "asdf", "123451234", "Studente", 10.4f);
    }

}
