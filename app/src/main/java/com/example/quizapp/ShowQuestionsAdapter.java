package com.example.quizapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedHashMap;
import java.util.List;

public class ShowQuestionsAdapter extends ArrayAdapter<String> {
    private Context ct;
    private LinkedHashMap<String, List<String>> questionSet;

    public ShowQuestionsAdapter(Context context, LinkedHashMap<String, List<String>> questionSet) {
        super(context,0);
        this.questionSet = questionSet;
        addAll(questionSet.keySet());
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_admin_view_b_lvdesign, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textView = convertView.findViewById(R.id.txtViewQuestion);
            viewHolder.listView = convertView.findViewById(R.id.lvViewChoices);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String question = getItem(position);
        List<String> choices = questionSet.get(question);

        viewHolder.textView.setText(question);

        Quiz_Choices_Adapter nestedAdapter = new Quiz_Choices_Adapter(getContext(), 0, choices);
        viewHolder.listView.setAdapter(nestedAdapter);


        return convertView;
    }

    private static class ViewHolder {
        TextView textView;
        ListView listView;
    }

}
