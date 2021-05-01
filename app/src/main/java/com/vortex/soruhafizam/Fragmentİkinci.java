package com.vortex.soruhafizam;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class Fragmentİkinci extends Fragment {
    private RecyclerView rv;
    private ArrayList<Sorular> sorularArrayList;
    private SorularAdapter2 adapter;
    private Database db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ikinci_layout,container,false);
        rv = view.findViewById(R.id.rv2);

        db = new Database(getContext());

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        SharedPreferences sp = getActivity().getSharedPreferences("KisiselBilgiler", MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();

        sorularArrayList = new QuestionsDao().getAllQuestions(db);
        ArrayList<Sorular> remindQuestion = new ArrayList<>();
        int temp;

        for (Sorular soru : sorularArrayList) {
            temp = calculate(soru.getEklenis_tarihi());
            if ((temp == 0) || (temp == 7) || (temp == 16) || (temp == 35)) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                String now = simpleDateFormat.format(new Date());
                String clock = sp.getString("saat", "12:00");
                String[] nowArray = now.split(":");
                String[] clockArray = clock.split(":");
                if (Integer.parseInt(nowArray[0]) >= Integer.parseInt(clockArray[0])
                        && Integer.parseInt(nowArray[1]) >= Integer.parseInt(clockArray[1])) {
                    remindQuestion.add(soru);
                }
            } else if (temp > 35) {
                new File(getImgPath(soru.getSoru_foto())).delete();
                new File(getImgPath(soru.getCevap_foto())).delete();
                new QuestionsDao().removeQuestion(db, soru.getSoru_id());
            }
        }
        adapter = new SorularAdapter2(db, getContext(), remindQuestion);
        rv.setAdapter(adapter);

        return view;
    }

    public static int calculate(String upDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d.M.y");
        Date date = new Date();
        String now = simpleDateFormat.format(date);

        Calendar cal = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        try {
            cal.setTime(simpleDateFormat.parse(upDate));
            cal2.setTime(simpleDateFormat.parse(now));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Date d1 = new Date(cal.getTimeInMillis());
        Date d2 = new Date(cal2.getTimeInMillis());

        long diff = d2.getTime() - d1.getTime() ;
        String days =String.valueOf(diff / (1000*60*60*24));

        return Integer.parseInt(days);
    }
    String getImgPath(String str) {
        File directory = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String path = directory.toString() + "/" + str + ".jpg";
        return new File(path).toString();
    }

}


