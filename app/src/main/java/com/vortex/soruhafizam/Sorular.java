package com.vortex.soruhafizam;

import java.io.Serializable;

public class Sorular implements Serializable {
    private int soru_id;
    private String ders;
    private String konu;
    private String sebep;
    private String soru_foto;
    private String cevap_foto;
    private String eklenis_tarihi;
    private int tekrar_etme;

    public Sorular() {
    }

    public Sorular(int soru_id, String ders, String konu, String sebep, String soru_foto, String cevap_foto, String eklenis_tarihi, int tekrar_etme) {
        this.soru_id = soru_id;
        this.ders = ders;
        this.konu = konu;
        this.sebep = sebep;
        this.soru_foto = soru_foto;
        this.cevap_foto = cevap_foto;
        this.eklenis_tarihi = eklenis_tarihi;
        this.tekrar_etme = tekrar_etme;
    }

    public int getSoru_id() {
        return soru_id;
    }

    public void setSoru_id(int soru_id) {
        this.soru_id = soru_id;
    }

    public String getDers() {
        return ders;
    }

    public void setDers(String ders) {
        this.ders = ders;
    }

    public String getKonu() {
        return konu;
    }

    public void setKonu(String konu) {
        this.konu = konu;
    }

    public String getSebep() {
        return sebep;
    }

    public void setSebep(String sebep) {
        this.sebep = sebep;
    }

    public String getSoru_foto() {
        return soru_foto;
    }

    public void setSoru_foto(String soru_foto) {
        this.soru_foto = soru_foto;
    }

    public String getCevap_foto() {
        return cevap_foto;
    }

    public void setCevap_foto(String cevap_foto) {
        this.cevap_foto = cevap_foto;
    }

    public String getEklenis_tarihi() {
        return eklenis_tarihi;
    }

    public void setEklenis_tarihi(String eklenis_tarihi) {
        this.eklenis_tarihi = eklenis_tarihi;
    }

    public int getTekrar_etme() { return tekrar_etme; }

    public void setTekrar_etme(int tekrar_etme) { this.tekrar_etme = tekrar_etme; }
}
