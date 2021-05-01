package com.vortex.soruhafizam;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SorularAdapter2 extends RecyclerView.Adapter<SorularAdapter2.CardTasarımTutucu2>{
    private Context mContext;
    private List<Sorular> sorularList;
    Database database;

    public SorularAdapter2(Database database,Context mContext, List<Sorular> sorularList) {
        this.database = database;
        this.mContext = mContext;
        this.sorularList = sorularList;
    }

    public class CardTasarımTutucu2 extends RecyclerView.ViewHolder{
        private TextView textViewKonu;
        private TextView textViewTarih;
        private TextView textViewDers;
        private TextView textView3;
        private ImageView imageViewAlert;
        private CardView cardView;

        public CardTasarımTutucu2(@NonNull View itemView) {
            super(itemView);

            textViewKonu = itemView.findViewById(R.id.textViewTekrarKonu);
            textViewDers = itemView.findViewById(R.id.textViewTekrarDers);
            textViewTarih = itemView.findViewById(R.id.textViewTekrarTarih);
            cardView = itemView.findViewById(R.id.cardview2);
            textView3 = itemView.findViewById(R.id.textView3);
            imageViewAlert = itemView.findViewById(R.id.imageViewAlert);
        }
    }

    @NonNull
    @Override
    public CardTasarımTutucu2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.kisi_card_tekrar_tasarim,parent,false);
        return new CardTasarımTutucu2(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final CardTasarımTutucu2 holder, final int position) {

        final Sorular soru = sorularList.get(position);

        holder.textViewDers.setText(soru.getDers());                            // Ders Bilgsi Ekleme
        holder.textViewTarih.setText(soru.getEklenis_tarihi());                 // Tarih Bilgisi Ekleme
        holder.textViewKonu.setText(soru.getKonu());                            // Konu Bilgisi Ekleme
        if (soru.getTekrar_etme() == 1) {
            holder.imageViewAlert.setImageDrawable(mContext.getResources().getDrawable(R.drawable.done));
        }
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

                new QuestionsDao().updateQuestion(database, id, ders, konu, sebep, soruFoto, cevapFoto
                        , tarih, 1);

                Intent intent = new Intent(mContext.getApplicationContext(),MainActivity2.class);
                intent.putExtra("vortex", soru);
                mContext.startActivity(intent);
                holder.imageViewAlert.setImageDrawable(mContext.getResources().getDrawable(R.drawable.done));
            }
        });
    }

    @Override
    public int getItemCount() { return sorularList.size(); }

}
