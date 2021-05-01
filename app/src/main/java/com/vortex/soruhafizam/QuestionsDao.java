package com.vortex.soruhafizam;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class QuestionsDao {

    public void addQuestion(Database database, String ders, String konu, String sebep
            , String soru_foto, String cevap_foto, String eklenis_tarihi) {

        SQLiteDatabase db = database.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(Database.COLUMN_DERS, ders);
        cv.put(Database.COLUMN_KONU, konu);
        cv.put(Database.COLUMN_SEBEP, sebep);
        cv.put(Database.COLUMN_SORU_FOTO, soru_foto);
        cv.put(Database.COLUMN_CEVAP_FOTO, cevap_foto);
        cv.put(Database.COLUMN_EKLENIS_TARIHI, eklenis_tarihi);

        db.insertOrThrow(Database.TABLE_NAME, null , cv);
        db.close();
    }

    public void removeQuestion(Database database, int soru_id) {
        SQLiteDatabase db = database.getWritableDatabase();
        db.delete(Database.TABLE_NAME, Database.COLUMN_SORU_ID + " = ?"
                , new String[] {String.valueOf(soru_id)});
        db.close();
    }

    public void updateQuestion(Database database, int soru_id, String ders, String konu, String sebep
            , String soru_foto, String cevap_foto, String eklenis_tarihi, int tekrar_etme) {
        SQLiteDatabase db = database.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(Database.COLUMN_DERS, ders);
        cv.put(Database.COLUMN_KONU, konu);
        cv.put(Database.COLUMN_SEBEP, sebep);
        cv.put(Database.COLUMN_SORU_FOTO, soru_foto);
        cv.put(Database.COLUMN_CEVAP_FOTO, cevap_foto);
        cv.put(Database.COLUMN_EKLENIS_TARIHI, eklenis_tarihi);
        cv.put(Database.COLUMN_TEKRAR_ETME, tekrar_etme);

        db.update(Database.TABLE_NAME, cv, Database.COLUMN_SORU_ID + " = ?"
                , new String[] {String.valueOf(soru_id)});
        db.close();
    }

    public ArrayList<Sorular> getAllQuestions(Database database) {
        ArrayList<Sorular> arrayList = new ArrayList<>();
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cr = db.rawQuery("SELECT * FROM " + Database.TABLE_NAME, null);

        while (cr.moveToNext()) {
            arrayList.add(new Sorular(cr.getInt(cr.getColumnIndex(Database.COLUMN_SORU_ID))
                    , cr.getString(cr.getColumnIndex(Database.COLUMN_DERS))
                    , cr.getString(cr.getColumnIndex(Database.COLUMN_KONU))
                    , cr.getString(cr.getColumnIndex(Database.COLUMN_SEBEP))
                    , cr.getString(cr.getColumnIndex(Database.COLUMN_SORU_FOTO))
                    , cr.getString(cr.getColumnIndex(Database.COLUMN_CEVAP_FOTO))
                    , cr.getString(cr.getColumnIndex(Database.COLUMN_EKLENIS_TARIHI))
                    , cr.getInt(cr.getColumnIndex(Database.COLUMN_TEKRAR_ETME))));
        }
        db.close();
        return arrayList;
    }

    public ArrayList<Sorular> searchQuestion(Database database, String searchWord) {
        ArrayList<Sorular> arrayList = new ArrayList<>();
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cr = db.rawQuery("SELECT * FROM " + Database.TABLE_NAME + " WHERE "
                + Database.COLUMN_KONU + " LIKE '%" + searchWord + "%'", null);

        while (cr.moveToNext()) {
            arrayList.add(new Sorular(cr.getInt(cr.getColumnIndex(Database.COLUMN_SORU_ID))
                    , cr.getString(cr.getColumnIndex(Database.COLUMN_DERS))
                    , cr.getString(cr.getColumnIndex(Database.COLUMN_KONU))
                    , cr.getString(cr.getColumnIndex(Database.COLUMN_SEBEP))
                    , cr.getString(cr.getColumnIndex(Database.COLUMN_SORU_FOTO))
                    , cr.getString(cr.getColumnIndex(Database.COLUMN_CEVAP_FOTO))
                    , cr.getString(cr.getColumnIndex(Database.COLUMN_EKLENIS_TARIHI))
                    , cr.getInt(cr.getColumnIndex(Database.COLUMN_TEKRAR_ETME))));
        }
        db.close();
        return arrayList;
    }
}