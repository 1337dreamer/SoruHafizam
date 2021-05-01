package com.vortex.soruhafizam;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "sorular.db";
    public static final int DATABASE_VERSION = 2;

    public static final String TABLE_NAME = "sorular";
    public static final String COLUMN_SORU_ID = "soru_id";
    public static final String COLUMN_DERS = "ders";
    public static final String COLUMN_KONU = "konu";
    public static final String COLUMN_SEBEP = "sebep";
    public static final String COLUMN_SORU_FOTO = "soru_foto";
    public static final String COLUMN_CEVAP_FOTO = "cevap_foto";
    public static final String COLUMN_EKLENIS_TARIHI = "eklenis_tarihi";
    public static final String COLUMN_TEKRAR_ETME = "tekrar_etme";

    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_SORU_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_DERS + " TEXT NOT NULL, "
                + COLUMN_KONU + " TEXT NOT NULL, "
                + COLUMN_SEBEP + " TEXT, "
                + COLUMN_SORU_FOTO + " TEXT NOT NULL, "
                + COLUMN_CEVAP_FOTO + " TEXT, "
                + COLUMN_EKLENIS_TARIHI + " TEXT NOT NULL,"
                + COLUMN_TEKRAR_ETME + " INTEGER DEFAULT 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}
