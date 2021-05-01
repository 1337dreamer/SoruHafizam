package com.vortex.soruhafizam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TimePicker;

import java.util.Calendar;


public class SettingsActivity extends AppCompatActivity {
    EditText txtEditAd, txtEditSaat;
    ImageView close;
    Switch switchDark;
    CardView cardView;
    Button btnKaydet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        txtEditAd = findViewById(R.id.txtEditAd);
        txtEditSaat = findViewById(R.id.txtEditSaat);
        close = findViewById(R.id.close);
        btnKaydet = findViewById(R.id.btnEkle);
        cardView = findViewById(R.id.cardView);
        switchDark = findViewById(R.id.switchDark);

        SharedPreferences sp = getSharedPreferences("KisiselBilgiler", MODE_PRIVATE);
        final SharedPreferences.Editor e = sp.edit();

        //ad, saat ve karanlık mod özelliklerini shared preference'de saklar.
        txtEditAd.setText(sp.getString("ad", ""));
        txtEditSaat.setText(sp.getString("saat", "00:00"));
        switchDark.setChecked(sp.getBoolean("darkMode", false));

        txtEditAd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    txtEditAd.setHint("");
                else
                    txtEditAd.setHint("Eklemek İstediğiniz Konu");
            }
        });

        //Time picker ile edit text'te saat açar.
        txtEditSaat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                final int min = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(SettingsActivity.this
                        , new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        txtEditSaat.setText(String.format("%02d:%02d", hourOfDay, minute));
                    }
                }, hour, min, true);
                timePickerDialog.setTitle("Saat seçiniz.");
                timePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ayarla", timePickerDialog);
                timePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "İptal", timePickerDialog);
                timePickerDialog.show();
            }
        });

        switchDark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                else
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        //Bize mail göndermesini sağlar.
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
                intent.putExtra(Intent.EXTRA_SUBJECT, "");
                intent.putExtra(Intent.EXTRA_TEXT, "");
                intent.setData(Uri.parse("mailto:16vortex@gmail.com")); // or just "mailto:" for blank
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
                startActivity(intent);
            }
        });

        //Ayarlardan çıkar.
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e.putBoolean("settings", true);
                e.commit();
                startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                finish();
            }
        });

        //Ayarları shared preference'e kaydedip çıkar.
        btnKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e.putString("ad", txtEditAd.getText().toString());
                e.putString("saat", txtEditSaat.getText().toString());
                e.putBoolean("darkMode", switchDark.isChecked());
                e.putBoolean("settings", true);
                e.commit();
                startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                finish();
            }
        });

    }
}