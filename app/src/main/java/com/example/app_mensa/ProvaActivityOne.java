package com.example.app_mensa;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProvaActivityOne extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prova1);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                /*
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        // selectedFragment = new HomeFragment(); // Sostituisci con il tuo fragment
                        break;
                    case R.id.nav_search:
                        // selectedFragment = new SearchFragment(); // Sostituisci con il tuo fragment
                        break;
                    case R.id.nav_settings:
                        // selectedFragment = new SettingsFragment(); // Sostituisci con il tuo fragment
                        break;
                }

                 */
                // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

                return true;
            }
        });

        // Carica il fragment di default all'avvio
        bottomNavigationView.setSelectedItemId(R.id.nav_home); // Imposta l'elemento di navigazione selezionato
    }
}
