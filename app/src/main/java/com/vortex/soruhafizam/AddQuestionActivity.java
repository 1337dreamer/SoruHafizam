package com.vortex.soruhafizam;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.Paint;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class AddQuestionActivity extends AppCompatActivity {
    EditText txtEditDers, txtEditKonu, txtEditSebep;
    TextView textView9;
    ImageView soruUp, cevapUp, soru, cevap, close2;
    Button btnEkle;
    boolean soruFoto, cevapFoto, ekle = false;
    Database db;
    String currentImagePath, currentFileName, questionName, answerName, tempQuestion, tempAnswer;
    private static final int CAMERA_PERMISSION_CODE = 1;
    int counterQuestion, counterAnswer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question2);
        counterQuestion = 0;
        counterAnswer = 0;
        soruFoto = false;
        cevapFoto = false;

        txtEditDers = findViewById(R.id.txtEditDers);
        txtEditKonu = findViewById(R.id.txtEditKonu);
        txtEditSebep = findViewById(R.id.txtEditSebep);
        soruUp = findViewById(R.id.soruUp);
        cevapUp = findViewById(R.id.cevapUp);
        soru = findViewById(R.id.soru);
        cevap = findViewById(R.id.cevap);
        btnEkle = findViewById(R.id.btnEkle);
        close2 = findViewById(R.id.close2);
        textView9 = findViewById(R.id.textView9);

        SharedPreferences sp = getSharedPreferences("KisiselBilgiler", MODE_PRIVATE);
        final SharedPreferences.Editor e = sp.edit();

        db = new Database(this);

        close2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e.putBoolean("addQuestion", true);
                e.commit();
                startActivity(new Intent(AddQuestionActivity.this, MainActivity.class));
                finish();

                if (!ekle) {
                    if (questionName != null) {
                        new File(getImgPath(questionName)).delete();
                    }
                    if (answerName != null) {
                        new File(getImgPath(answerName)).delete();
                    }
                }
            }
        });

        //Soru fotoğrafı alır.
        soruUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counterQuestion++;
                openCamera("question");
                soruFoto = true;
            }
        });

        // Cevap fotoğrafı alır.
        cevapUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counterAnswer++;
                openCamera("answer");
                cevapFoto = true;
            }
        });

        txtEditDers.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    txtEditDers.setHint("");
                else
                    txtEditDers.setHint("Eklemek İstediğiniz Ders");

            }
        });
        txtEditKonu.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    txtEditKonu.setHint("");
                else
                    txtEditKonu.setHint("Eklemek İstediğiniz Konu");
            }
        });
        txtEditSebep.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    txtEditSebep.setHint("");
                else
                    txtEditSebep.setHint("Sebebiniz");
            }
        });


        // Gelen fotoğrafa göre soru yada cevap fotoğrafını image view'e ekler.
        btnEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ders = txtEditDers.getText().toString().trim();
                String konu = txtEditKonu.getText().toString().trim();
                String sebep = txtEditSebep.getText().toString().trim();
                String soru_foto = questionName;
                String cevap_foto = answerName;
                String eklenis_tarihi = new SimpleDateFormat("d.M.y").format(new Date());

                if (ders.isEmpty() || konu.isEmpty() || soru_foto == null) {
                    if (ders.isEmpty() && konu.isEmpty() && soru_foto == null) {

                        txtEditDers.setHint("Bu alan boş bırakılamaz");
                        txtEditDers.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                        txtEditDers.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (hasFocus) {
                                    txtEditDers.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.backgroundTint)));
                                    txtEditDers.setHint("");
                                }
                            }
                        });
                        txtEditKonu.setHint("Bu alan boş bırakılamaz");
                        txtEditKonu.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                        txtEditKonu.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (hasFocus) {
                                    txtEditKonu.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.backgroundTint)));
                                    txtEditKonu.setHint("");
                                }
                            }
                        });

                        textView9.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

                        Snackbar snackbar = Snackbar.make(v, "Ders, Konu alanları ve sorunun fotoğrafı boş bırakılamaz!"
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
                    } else if (ders.isEmpty() && konu.isEmpty()) {

                        txtEditDers.setHint("Bu alan boş bırakılamaz");
                        txtEditDers.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                        txtEditDers.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (hasFocus) {
                                    txtEditDers.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.backgroundTint)));
                                    txtEditDers.setHint("");
                                }
                            }
                        });
                        txtEditKonu.setHint("Bu alan boş bırakılamaz");
                        txtEditKonu.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                        txtEditKonu.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (hasFocus) {
                                    txtEditKonu.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.backgroundTint)));
                                    txtEditKonu.setHint("");
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
                    } else if (ders.isEmpty() && soru_foto == null) {

                        txtEditDers.setHint("Bu alan boş bırakılamaz");
                        txtEditDers.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                        txtEditDers.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (hasFocus) {
                                    txtEditDers.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.backgroundTint)));
                                    txtEditDers.setHint("");
                                }
                            }
                        });

                        textView9.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

                        Snackbar snackbar = Snackbar.make(v, "Konu alanı ve sorunun fotoğrafı boş bırakılamaz!"
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
                    } else if (konu.isEmpty() && soru_foto == null) {

                        txtEditKonu.setHint("Bu alan boş bırakılamaz");
                        txtEditKonu.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                        txtEditKonu.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (hasFocus) {
                                    txtEditKonu.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.backgroundTint)));
                                    txtEditKonu.setHint("");
                                }
                            }
                        });

                        textView9.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

                        Snackbar snackbar = Snackbar.make(v, "Konu alanı ve sorunun fotoğrafı boş bırakılamaz!"
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

                        txtEditDers.setHint("Bu alan boş bırakılamaz");
                        txtEditDers.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                        txtEditDers.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (hasFocus) {
                                    txtEditDers.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.backgroundTint)));
                                    txtEditDers.setHint("");
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

                        txtEditKonu.setHint("Bu alan boş bırakılamaz");
                        txtEditKonu.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                        txtEditKonu.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (hasFocus) {
                                    txtEditKonu.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.backgroundTint)));
                                    txtEditKonu.setHint("");
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
                    } else if (soru_foto == null) {

                        textView9.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

                        Snackbar snackbar = Snackbar.make(v, "Sorunun fotoğrafı boş bırakılamaz!"
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
                    new QuestionsDao().addQuestion(db, ders, konu, sebep, soru_foto, cevap_foto, eklenis_tarihi);
                    new QuestionsDao().getAllQuestions(db);
                    ekle = true;
                    e.putBoolean("addQuestion", true);
                    e.commit();
                    startActivity(new Intent(AddQuestionActivity.this, MainActivity.class));
                    finish();

                }
            }
        });
    }
    void openCamera(String str) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File imageFile = createImageFile();
        Uri imageUri = FileProvider.getUriForFile(this
                ,"com.vortex.soruhafizam.fileprovider"
                , imageFile);
        String temp = imageFile.toString();
        String path = temp.substring(temp.indexOf(currentFileName), temp.length()-4);
        switch (str) {
            case "question":
                tempQuestion = path;
                break;
            case "answer":
                tempAnswer = path;
                break;
        }
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
            if (soruFoto) {
                if (counterQuestion > 1) {
                    String delete = questionName;
                    questionName = tempQuestion;
                    checkCount(counterQuestion, delete);
                } else {
                    questionName = tempQuestion;
                }
                soru.setImageBitmap(getScaledBitmap(soru));
                soru.setVisibility(View.VISIBLE);
                soruFoto = false;
            }
            if (cevapFoto) {
                if (counterAnswer > 1) {
                    String delete = answerName;
                    answerName = tempAnswer;
                    checkCount(counterAnswer, delete);
                } else {
                    answerName = tempAnswer;
                }
                cevap.setImageBitmap(getScaledBitmap(cevap));
                cevap.setVisibility(View.VISIBLE);
                cevapFoto = false;
            }
        }
        if (requestCode == CAMERA_PERMISSION_CODE && resultCode == RESULT_CANCELED) {
            if (soruFoto) {
                new File(getImgPath(questionName)).delete();
            }
            if (cevapFoto) {
                new File(getImgPath(answerName)).delete();
            }
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