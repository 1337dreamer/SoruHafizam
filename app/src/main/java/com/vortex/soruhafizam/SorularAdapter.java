package com.vortex.soruhafizam;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class SorularAdapter extends RecyclerView.Adapter<SorularAdapter.CardTasarımTutucu>{

    private Context mContext;
    private List<Sorular> sorularList;
    Database database;

    public SorularAdapter(Database database,Context mContext, List<Sorular> sorularList) {
        this.database = database;
        this.mContext = mContext;
        this.sorularList = sorularList;
    }

    public class CardTasarımTutucu extends RecyclerView.ViewHolder{
        private TextView textViewKonu;
        private TextView textViewTarih;
        private TextView textViewDers;
        private TextView textView3;
        private ImageView imageViewNokta, cevapUpdate, imageViewCevapUp;

        private ImageView action_sil;
        private Button action_düzenle;

        private CardView cardView;

        public CardTasarımTutucu(@NonNull View itemView) {
            super(itemView);

            textViewKonu = itemView.findViewById(R.id.textViewKonu);
            textViewDers = itemView.findViewById(R.id.textViewDers);
            textViewTarih = itemView.findViewById(R.id.textViewTarih);
            imageViewNokta = itemView.findViewById(R.id.imageViewNokta);

            action_düzenle = itemView.findViewById(R.id.action_düzenle);
            action_sil = itemView.findViewById(R.id.action_sil);

            cardView = itemView.findViewById(R.id.cardview);

            cevapUpdate = itemView.findViewById(R.id.cevapUpdate);
            imageViewCevapUp = itemView.findViewById(R.id.imageViewCevapUp);
            textView3 = itemView.findViewById(R.id.textView3);

        }

    }

    @NonNull
    @Override
    public CardTasarımTutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.kisi_card_tasarim,parent,false);
        return new CardTasarımTutucu(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final CardTasarımTutucu holder, final int position) {
        final Sorular soru = sorularList.get(position);

        holder.textViewDers.setText(soru.getDers());                            // Ders Bilgsi Ekleme
        holder.textViewTarih.setText(soru.getEklenis_tarihi());                 // Tarih Bilgisi Ekleme
        holder.textViewKonu.setText(soru.getKonu());                            // Konu Bilgisi Ekleme


        holder.imageViewNokta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                PopupMenu popupMenu = new PopupMenu(mContext,holder.imageViewNokta);
                popupMenu.getMenuInflater().inflate(R.menu.pop_up_menu,popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.action_sil:
                                sorularList = new QuestionsDao().getAllQuestions(database);

                                new File(getImgPath(soru.getSoru_foto())).delete();
                                new File(getImgPath(soru.getCevap_foto())).delete();

                                new QuestionsDao().removeQuestion(database, soru.getSoru_id());
                                sorularList = new QuestionsDao().getAllQuestions(database);         // Sile bastıktan sonra hemen güncellemesi için
                                notifyDataSetChanged();

                                return true;
                            case R.id.action_düzenle:

                                Intent intent = new Intent(mContext.getApplicationContext(), Duzenle.class);
                                intent.putExtra("soru", soru);
                                mContext.startActivity(intent);
                                return true;
                            default:
                                return false;
                        }
                    }
                });

                popupMenu.show();
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ders = sorularList.get(position).getDers();
                String konu = sorularList.get(position).getKonu();
                String soruFoto = sorularList.get(position).getSoru_foto();
                String cevapFoto = sorularList.get(position).getCevap_foto();
                int id = sorularList.get(position).getSoru_id();
                String tarih = sorularList.get(position).getEklenis_tarihi();
                String sebep = sorularList.get(position).getSebep();
                int tekrar_etme = sorularList.get(position).getTekrar_etme();

                Sorular soru = new Sorular(id,ders,konu,sebep,soruFoto,cevapFoto,tarih, tekrar_etme);

                Intent intent = new Intent(mContext.getApplicationContext(),MainActivity2.class);
                intent.putExtra("vortex", soru);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() { return sorularList.size(); }

    String getImgPath(String str) {
        File directory = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String path = directory.toString() + "/" + str + ".jpg";
        Log.e("YOL", path);
        return new File(path).toString();
    }

    Bitmap getImage(String str) {
        Bitmap b = null;
        try {
            File directory = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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
