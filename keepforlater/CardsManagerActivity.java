package com.example.app_mensa;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Date;
import java.util.LinkedList;

public class CardsManagerActivity extends AppCompatActivity {
    private LinkedList<CreditCard> cardsList;

    // TODO: fill the userid from memory
    private int userID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        associateUI();

        cardsList = getCreditCardList(1);
    }

    public void associateUI() {

    }

    public LinkedList<CreditCard> getCreditCardList(int userid) {
        // sql che da tutte le carte associate a quell'utente

        LinkedList<CreditCard> list = new LinkedList<CreditCard>();
        list.add(new CreditCard("Mario Rossi", "123123123132", new Date(2025, 01, 01),))

        return
    }

}
