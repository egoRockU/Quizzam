package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ChooseQuizToStartPrompt extends AppCompatActivity {

    private static List<String> Questions;
    private ListView quizList;
    public static String chosenTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_quiz_to_start_prompt);

        quizList = findViewById(R.id.quizList);
        Questions = new ArrayList<>();

        getQuizzes();
        ArrayAdapter<String> quizAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Questions);
        quizList.setAdapter(quizAdapter);

        quizList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                chosenTopic = Questions.get(i);
                startActivity(new Intent(ChooseQuizToStartPrompt.this, QuizActivity.class));
            }
        });

    }

    private void getQuizzes(){

        try {
            Questions.clear(); //need to be cleared so that when called again, it won't iterate
            AssetManager assetManager = getAssets();
            InputStream inputStream = assetManager.open("QuizFolder/-QUIZZES.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null ){
                line = line.replaceAll(".txt", ""); //Remove .txt so it wont appear in listview
                Questions.add(line);
            }
            reader.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}