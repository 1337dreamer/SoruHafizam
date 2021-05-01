package com.vortex.soruhafizam;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class FragmentBirinci extends Fragment {

    private ImageView settingbtn;
    private Button button, button3;
    private TextView hosgeldiniz;
    private Database database;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_birinci_layout, container,false);

        settingbtn = view.findViewById(R.id.button2);
        button = view.findViewById(R.id.button);
        button3 = view.findViewById(R.id.button3);
        hosgeldiniz = view.findViewById(R.id.hosgeldiniz);
        database = new Database(getContext());

        final SharedPreferences sp = getActivity().getSharedPreferences("KisiselBilgiler", MODE_PRIVATE);
        final SharedPreferences.Editor e = sp.edit();

        if (!sp.getString("ad", "").trim().isEmpty())
            hosgeldiniz.setText(sp.getString("ad" , ""));
        else
            hosgeldiniz.setText("Hoş geldiniz");

        List<Sorular> sorularList = new QuestionsDao().getAllQuestions(database);
        ArrayList<Sorular> remindQuestion = new ArrayList<>();
        int counter = 0 , cevapsiz = 0;
        int temp;

        for (Sorular soru : sorularList) {

            if (soru.getCevap_foto() == null)
                cevapsiz++;             // Cevapsız sorular

            temp = Fragmentİkinci.calculate(soru.getEklenis_tarihi());

            if ((temp == 1) || (temp == 7) || (temp == 16) || (temp == 35)) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                String now = simpleDateFormat.format(new Date());
                String clock = sp.getString("saat", "00:00");
                String[] nowArray = now.split(":");
                String[] clockArray = clock.split(":");
                if (Integer.parseInt(nowArray[0]) >= Integer.parseInt(clockArray[0])
                        && Integer.parseInt(nowArray[1]) >= Integer.parseInt(clockArray[1])) {
                    remindQuestion.add(soru);           // Tekrar edilmesi gereken sorular
                }
            }
        }
        for (Sorular soru : remindQuestion) {
            if (soru.getTekrar_etme() == 0)
                counter++;                      // Tekrar edilmemiş sorular
        }

        //Tekrar edilmesi gereken sorular
        if (counter != 0) {
            button.setText("Tekrar etmen gereken " + counter + " soru var!");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragment_tutucu,new Fragmentİkinci()).commit();
                }
            });
        } else {
            button.setText("Tüm soruları tekrar etmişsin tebrikler!");
        }

        //Cevabı olmayan sorular
        if (cevapsiz != 0) {
            button3.setText("Cevabını eklemediğin " + cevapsiz + " adet sorun var");
            button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragment_tutucu,new FragmentUcuncu()).commit();
                    e.putBoolean("fragmentBirinci", true);
                    e.commit();
                }
            });
        } else
            button3.setText("Harika! Tüm soruların cevabını eklemişsin.");


        settingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),SettingsActivity.class));
            }
        });

        return view;
    }

}
