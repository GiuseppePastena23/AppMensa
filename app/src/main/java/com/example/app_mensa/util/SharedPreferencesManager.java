package com.example.app_mensa.util;

import com.example.app_mensa.dao.User;
import com.google.gson.Gson;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {
    private SharedPreferences sp;

    private Gson gson = new Gson();

    public SharedPreferencesManager(Context context) {
        this.sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
    }

    public void saveUser(User user) {
        SharedPreferences.Editor editor = sp.edit();
        String userJson = gson.toJson(user); // Serializza l'oggetto Utente in JSON
        editor.putString("user", userJson);
        editor.apply();
    }

    // Metodo per recuperare l'utente
    public User getUser() {
        String userJson = sp.getString("user", null);
        if (userJson != null) {
            return gson.fromJson(userJson, User.class); // Deserializza il JSON in un oggetto Utente
        }
        return null; // Nessun utente salvato
    }

    public void clearUser() {
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("user");
        editor.apply();
    }

}
