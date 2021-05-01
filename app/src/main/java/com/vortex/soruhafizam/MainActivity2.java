package com.vortex.soruhafizam;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MainActivity2 extends AppCompatActivity {
    TextView textViewMain2Konu, textViewMain2Ders, textViewMain2Tekrar;
    ImageView imageViewMain2Close, imageMain2Soru, imageMain2Cevap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        textViewMain2Ders = findViewById(R.id.textViewMain2Ders);
        textViewMain2Konu = findViewById(R.id.textViewMain2Konu);
        textViewMain2Tekrar = findViewById(R.id.textViewMain2Tekrar);
        imageViewMain2Close = findViewById(R.id.imageViewMain2Close);
        imageMain2Soru = findViewById(R.id.imageMain2Soru);
        imageMain2Cevap = findViewById(R.id.imageMain2Cevap);

        imageViewMain2Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        Sorular gelenSoru = (Sorular) getIntent().getSerializableExtra("vortex");

        textViewMain2Ders.setText("Ders: " + gelenSoru.getDers());
        textViewMain2Konu.setText("Konu: " + gelenSoru.getKonu());
        if (gelenSoru.getSebep().isEmpty())
            textViewMain2Tekrar.setVisibility(View.GONE);
        else
            textViewMain2Tekrar.setText("Tekrar Sebebi: " + gelenSoru.getSebep());
        imageMain2Soru.setImageBitmap(getImage(gelenSoru.getSoru_foto()));
        imageMain2Cevap.setImageBitmap(getImage(gelenSoru.getCevap_foto()));


    }
    Bitmap getImage(String str) {
        Bitmap b = null;
        try {
            File directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            String path = directory.toString() + "/" + str + ".jpg";
            Log.e("YOL", path);
            File f=new File(path);
            b = BitmapFactory.decodeStream(new FileInputStream(f));
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return b;
    }
}