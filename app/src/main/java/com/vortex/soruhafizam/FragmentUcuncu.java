package com.vortex.soruhafizam;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.io.File;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class FragmentUcuncu extends Fragment {

    private ImageView artıbuton, imageViewDelete,imageViewChange;
    private RecyclerView rv;
    private ArrayList<Sorular> sorularArrayList;
    private ArrayList<Sorular> allQuestion;
    private SorularAdapter adapter;
    private Database db;
    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ucuncu_layout,container,false);

        rv = view.findViewById(R.id.rv);
        artıbuton = view.findViewById(R.id.button2);
        imageViewChange = view.findViewById(R.id.imageViewChange);
        imageViewDelete = view.findViewById(R.id.imageViewDelete);      // tümü sil çöp kutusu
        searchView = view.findViewById(R.id.searchView);                       // aratma şeysii

        final SharedPreferences sp = getActivity().getSharedPreferences("KisiselBilgiler", MODE_PRIVATE);
        final SharedPreferences.Editor e = sp.edit();

        db = new Database(getContext());

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        if (sp.getBoolean("answerShow", true)) {
            sorularArrayList = new QuestionsDao().getAllQuestions(db);
            adapter = new SorularAdapter(db, getContext(), sorularArrayList);
            rv.setAdapter(adapter);
            imageViewChange.setBackgroundResource(R.drawable.hide);
        } else {
            ArrayList<Sorular> sorular = new QuestionsDao().getAllQuestions(db);
            ArrayList<Sorular> cevapsız = new ArrayList<>();
            for (Sorular cevap : sorular)
                if (cevap.getCevap_foto() == null) {
                    cevapsız.add(cevap);
                }
            adapter = new SorularAdapter(db, getContext(), cevapsız);
            rv.setAdapter(adapter);
            imageViewChange.setBackgroundResource(R.drawable.eye);
        }

        if (sp.getBoolean("fragmentBirinci", false)) {
            if (sp.getBoolean("answerShow", true)) {

                imageViewChange.setPressed(true);
                imageViewChange.invalidate();
                // delay completion till animation completes
                imageViewChange.postDelayed(new Runnable() {  //delay button
                    public void run() {
                        imageViewChange.performClick();
                        imageViewChange.setPressed(false);
                        imageViewChange.invalidate();
                        //any other associated action
                    }
                },0);
            }
            e.putBoolean("fragmentBirinci", false);
            e.commit();
        }

        artıbuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(),AddQuestionActivity.class));    // MainActivity Yerine + basıldığında SoruEkle Activitysi gelicek.
            }
        });

        allQuestion = new QuestionsDao().getAllQuestions(db);

        imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle( "Uyarı" )
                        .setIcon(R.drawable.warning)
                        .setMessage("Tüm sorularını silmek istediğinden emin misin?")
                        .setPositiveButton("Sil", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for (Sorular soru : allQuestion) {

                                    new File(getImgPath(soru.getSoru_foto())).delete();
                                    new File(getImgPath(soru.getCevap_foto())).delete();

                                    new QuestionsDao().removeQuestion(db, soru.getSoru_id());
                                }
                                sorularArrayList = new QuestionsDao().getAllQuestions(db);
                                adapter = new SorularAdapter(db, getContext(), sorularArrayList);
                                rv.setAdapter(adapter);
                            }
                        })
                        .setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                aramaYap(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                aramaYap(newText);
                return false;
            }
        });


        imageViewChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sp.getBoolean("answerShow", true)) {
                    ArrayList<Sorular> sorular = new QuestionsDao().getAllQuestions(db);
                    ArrayList<Sorular> cevapsız = new ArrayList<>();
                    for (Sorular cevap : sorular)
                        if (cevap.getCevap_foto() == null) {
                            cevapsız.add(cevap);
                        }
                    adapter = new SorularAdapter(db, getContext(), cevapsız);
                    rv.setAdapter(adapter);
                    e.putBoolean("answerShow", false);
                    e.commit();
                    imageViewChange.setBackgroundResource(R.drawable.eye);
                } else {
                    sorularArrayList = new QuestionsDao().getAllQuestions(db);
                    adapter = new SorularAdapter(db, getContext(), sorularArrayList);
                    rv.setAdapter(adapter);
                    e.putBoolean("answerShow", true);
                    e.commit();
                    imageViewChange.setBackgroundResource(R.drawable.hide);
                }

            }
        });


        return view;
    }

    public void aramaYap(String query) {
        sorularArrayList = new QuestionsDao().searchQuestion(db, query);
        adapter = new SorularAdapter(db, getContext(), sorularArrayList);
        rv.setAdapter(adapter);
    }

     String getImgPath(String str) {
        File directory = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String path = directory.toString() + "/" + str + ".jpg";
        Log.e("YOL", path);
        return new File(path).toString();
    }

}
