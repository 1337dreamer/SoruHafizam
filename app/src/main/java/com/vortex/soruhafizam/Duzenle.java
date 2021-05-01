package com.vortex.soruhafizam;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Duzenle extends AppCompatActivity {
    EditText txtEditDersDuzenle, txtEditKonuDuzenle, txtEditSebepDuzenle;
    ImageView cevapUpdateDuzenle, imageViewCevapUpDuzenle;
    Button btnİptalDuzenle, btnDuzenle;
    String ders, konu, sebep, currentFileName, currentImagePath, tempAnswer, answerName, soru_foto, eklenis_tarihi, cevap;
    int counterAnswer, soru_id, tekrar_etme;
    boolean duzenle = false, cvp;
    Database db;
    private static final int CAMERA_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duzenle);

        SharedPreferences sp = getSharedPreferences("KisiselBilgiler", MODE_PRIVATE);
        final SharedPreferences.Editor e = sp.edit();

        counterAnswer = 0;
        db = new Database(this);

        Sorular soru = (Sorular) getIntent().getSerializableExtra("soru");

        txtEditDersDuzenle = findViewById(R.id.txtEditDersDuzenle);
        txtEditKonuDuzenle = findViewById(R.id.txtEditKonuDuzenle);
        txtEditSebepDuzenle = findViewById(R.id.txtEditSebepDuzenle);
        cevapUpdateDuzenle = findViewById(R.id.cevapUpdateDuzenle);
        imageViewCevapUpDuzenle = findViewById(R.id.imageViewCevapUpDuzenle);
        btnİptalDuzenle = findViewById(R.id.btnİptalDuzenle);
        btnDuzenle = findViewById(R.id.btnDuzenle);

        txtEditDersDuzenle.setText(soru.getDers());
        txtEditKonuDuzenle.setText(soru.getKonu());
        txtEditSebepDuzenle.setText(soru.getSebep());
        imageViewCevapUpDuzenle.setImageBitmap(getImage(soru.getCevap_foto()));

        soru_foto = soru.getSoru_foto();
        eklenis_tarihi = soru.getEklenis_tarihi();
        soru_id = soru.getSoru_id();
        tekrar_etme = soru.getTekrar_etme();

        cevap = soru.getCevap_foto();
        if (!(cevap == null))
            cvp = true;
        else
            cvp = false;

        txtEditDersDuzenle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    txtEditDersDuzenle.setHint("");
                else
                    txtEditDersDuzenle.setHint("Eklemek İstediğiniz Konu");
            }
        });

        txtEditKonuDuzenle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    txtEditKonuDuzenle.setHint("");
                else
                    txtEditKonuDuzenle.setHint("Eklemek İstediğiniz Konu");
            }
        });
        txtEditSebepDuzenle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    txtEditSebepDuzenle.setHint("");
                else
                    txtEditSebepDuzenle.setHint("Eklemek İstediğiniz Konu");
            }
        });


        cevapUpdateDuzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counterAnswer++;
                openCamera();
            }
        });

        btnDuzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ders = txtEditDersDuzenle.getText().toString().trim();
                konu = txtEditKonuDuzenle.getText().toString().trim();
                sebep = txtEditSebepDuzenle.getText().toString().trim();

                String cevap_foto = answerName;

                if (ders.isEmpty() || konu.isEmpty()) {
                    if (ders.isEmpty() && konu.isEmpty()) {
                        txtEditDersDuzenle.setHint("Bu alan boş bırakılamaz");
                        txtEditDersDuzenle.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                        txtEditDersDuzenle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (hasFocus) {
                                    txtEditDersDuzenle.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.backgroundTint)));
                                    txtEditDersDuzenle.setHint("Eklemek İstediğiniz Ders");
                                }
                            }
                        });
                        txtEditKonuDuzenle.setHint("Bu alan boş bırakılamaz");
                        txtEditKonuDuzenle.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                        txtEditKonuDuzenle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (hasFocus) {
                                    txtEditKonuDuzenle.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.backgroundTint)));
                                    txtEditKonuDuzenle.setHint("Eklemek İstediğiniz Ders");
                                }
                            }
                        });

                        Snackbar snackbar = Snackbar.make(v, "Ders ve konu alanları boş bırakılamaz!"
                                , Snackbar.LENGTH_LONG)
                                .setAction("Tamam", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    }
                                });
                        snackbar.setTextColor(getResources().getColor(R.color.snackText));
                        snackbar.setBackgroundTint(getResources().getColor(R.color.snackBackground));
                        snackbar.setActionTextColor(getResources().getColor(R.color.snackAction));
                        snackbar.show();
                    } else if (ders.isEmpty()) {
                        txtEditDersDuzenle.setHint("Bu alan boş bırakılamaz");
                        txtEditDersDuzenle.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                        txtEditDersDuzenle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (hasFocus) {
                                    txtEditDersDuzenle.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.backgroundTint)));
                                    txtEditDersDuzenle.setHint("Eklemek İstediğiniz Ders");
                                }
                            }
                        });

                        Snackbar snackbar = Snackbar.make(v, "Ders alanı boş bırakılamaz!"
                                , Snackbar.LENGTH_LONG)
                                .setAction("Tamam", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    }
                                });
                        snackbar.setTextColor(getResources().getColor(R.color.snackText));
                        snackbar.setBackgroundTint(getResources().getColor(R.color.snackBackground));
                        snackbar.setActionTextColor(getResources().getColor(R.color.snackAction));
                        snackbar.show();

                    } else if (konu.isEmpty()) {
                        txtEditKonuDuzenle.setHint("Bu alan boş bırakılamaz");
                        txtEditKonuDuzenle.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                        txtEditKonuDuzenle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (hasFocus) {
                                    txtEditKonuDuzenle.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.backgroundTint)));
                                    txtEditKonuDuzenle.setHint("Eklemek İstediğiniz Ders");
                                }
                            }
                        });

                        Snackbar snackbar = Snackbar.make(v, "Konu alanı boş bırakılamaz!"
                                , Snackbar.LENGTH_LONG)
                                .setAction("Tamam", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    }
                                });
                        snackbar.setTextColor(getResources().getColor(R.color.snackText));
                        snackbar.setBackgroundTint(getResources().getColor(R.color.snackBackground));
                        snackbar.setActionTextColor(getResources().getColor(R.color.snackAction));
                        snackbar.show();
                    }
                } else {

                    if (cvp) {
                        if (cevap != cevap_foto)
                            new File(getImgPath(cevap)).delete();
                    }

                    new QuestionsDao().updateQuestion(db, soru_id, ders, konu, sebep, soru_foto, cevap_foto, eklenis_tarihi, tekrar_etme);
                    new QuestionsDao().getAllQuestions(db);
                    duzenle = true;
                    e.putBoolean("duzenle", true);
                    e.commit();
                    startActivity(new Intent(Duzenle.this, MainActivity.class));

                }
            }
        });

        btnİptalDuzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e.putBoolean("duzenle", true);
                e.commit();
                startActivity(new Intent(Duzenle.this, MainActivity.class));

                if (!duzenle) {
                    if (answerName != null)
                        new File(getImgPath(answerName)).delete();
                }
            }
        });

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

    void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File imageFile = createImageFile();
        Uri imageUri = FileProvider.getUriForFile(this
                ,"com.vortex.soruhafizam.fileprovider"
                , imageFile);
        String temp = imageFile.toString();
        String path = temp.substring(temp.indexOf(currentFileName), temp.length()-4);
        tempAnswer = path;
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CAMERA_PERMISSION_CODE);
    }

    private File createImageFile() {
        String fileName = "vortex" + new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss",
                Locale.getDefault()).format(new Date());
        File directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = null;
        try {
            imageFile = File.createTempFile(fileName, ".jpg", directory);
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentImagePath = imageFile.getAbsolutePath();
        currentFileName = fileName;
        return imageFile;
    }

    private Bitmap getScaledBitmap(ImageView imageView) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        int scaleFactor = Math.min( options.outWidth / imageView.getWidth() ,
                options.outHeight / imageView.getHeight());

        options.inJustDecodeBounds = false;
        options.inSampleSize = scaleFactor;
        options.inPurgeable = true;

        return BitmapFactory.decodeFile(currentImagePath, options);
    }

    //Intent dönüşlerini tutar.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PERMISSION_CODE && resultCode == RESULT_OK) {
                if (counterAnswer > 1) {
                    String delete = answerName;
                    answerName = tempAnswer;
                    checkCount(counterAnswer, delete);
                } else {
                    answerName = tempAnswer;
                }
                imageViewCevapUpDuzenle.setImageBitmap(getScaledBitmap(imageViewCevapUpDuzenle));
        }
        if (requestCode == CAMERA_PERMISSION_CODE && resultCode == RESULT_CANCELED) {
                new File(getImgPath(answerName)).delete();

        }
    }

    private void checkCount(int count, String str) {
        if (count > 1) {
            new File(getImgPath(str)).delete();
        }
    }

    String getImgPath(String str) {
        File directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String path = directory.toString() + "/" + str + ".jpg";
        Log.e("YOL", path);
        return new File(path).toString();
    }
}