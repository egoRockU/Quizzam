package com.example.quizapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Quiz_NameAdapter extends ArrayAdapter<Quiz_Name_Constructor> {
    private Context ct;
    private ArrayList<Quiz_Name_Constructor> arr;
    public Quiz_NameAdapter(@NonNull Context context, int resource, @NonNull List<Quiz_Name_Constructor> objects) {
        super(context, resource, objects);
        this.ct = context;
        this.arr = new ArrayList<>(objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater i = (LayoutInflater)ct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView =   i.inflate(R.layout.quiz_names, null);
        }
        if(arr.size()>0){
            Quiz_Name_Constructor d = arr.get(position);
            TextView txtviewQuizSubject = convertView.findViewById(R.id.txtviewQuizSubject);
            TextView txtviewQuizItem = convertView.findViewById(R.id.txtviewQuizItem);

            txtviewQuizSubject.setText(d.quizname);
            txtviewQuizItem.setText(d.quizitem + " Items");
        }
        return convertView;
    }
}
