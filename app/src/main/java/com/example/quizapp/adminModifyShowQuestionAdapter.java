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

public class adminModifyShowQuestionAdapter extends ArrayAdapter {

    private Context ct;
    private ArrayList<String> arr;


    public adminModifyShowQuestionAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);

        this.ct = context;
        this.arr = new ArrayList<>(objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            LayoutInflater i = (LayoutInflater)ct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = i.inflate(R.layout.activity_admin_modify_b_lvdesign, null);

        }
        if (arr.size()>0){
            String d = arr.get(position);

            TextView textViewShowQuestion = convertView.findViewById(R.id.text1);

            textViewShowQuestion.setText(d);
        }
        return convertView;
    }
}