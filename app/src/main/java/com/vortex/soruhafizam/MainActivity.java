package com.vortex.soruhafizam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

// activity_main fragment tutucunun yarragını bükmeyi unutma
    private BottomNavigationView bottom_navigation;
    private Fragment tempFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottom_navigation = findViewById(R.id.bottom_navigation);

        SharedPreferences sp = getSharedPreferences("KisiselBilgiler", MODE_PRIVATE);
        final SharedPreferences.Editor e = sp.edit();

        if (sp.getBoolean("darkMode", false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        boolean question = sp.getBoolean("addQuestion", false);
        boolean settings = sp.getBoolean("settings", false);
        boolean duzenle = sp.getBoolean("duzenle", false);
        if (question || duzenle) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_tutucu, new FragmentUcuncu()).commit();
            if (question) {
                e.putBoolean("addQuestion", false);
                e.commit();
            } else {
                e.putBoolean("duzenle", false);
                e.commit();
            }
        } else if (settings) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_tutucu, new FragmentBirinci()).commit();
            e.putBoolean("settings", false);
            e.commit();
        } else
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_tutucu,new FragmentBirinci()).commit();


        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.action_birinci){
                    tempFragment = new FragmentBirinci();
                }
                if (item.getItemId() == R.id.action_ikinci){
                    tempFragment  = new Fragmentİkinci();
                }
                if (item.getItemId() == R.id.action_ucuncu){
                    tempFragment = new FragmentUcuncu();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_tutucu,tempFragment).commit();
                return true;
            }
        });
    }
}