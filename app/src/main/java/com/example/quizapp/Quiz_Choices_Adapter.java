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

public class Quiz_Choices_Adapter extends ArrayAdapter {

    private Context ct;
    private ArrayList<String> arr;


    public Quiz_Choices_Adapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        this.ct = context;
        this.arr = new ArrayList<>(objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            LayoutInflater i = (LayoutInflater)ct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = i.inflate(R.layout.quiz_choices, null);
        }
        if (arr.size()>0){
            //Quiz_Choices_Constructor d = arr.get(position);
            String d = arr.get(position);

            TextView textViewQuizChoice = convertView.findViewById(R.id.txtViewQuizChoice);

            textViewQuizChoice.setText(d);
        }
        return convertView;
    }
}
