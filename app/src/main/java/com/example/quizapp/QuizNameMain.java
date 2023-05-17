package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class QuizNameMain extends AppCompatActivity {

    private static List<Quiz_Name_Constructor> Questions;
    private ListView quizList;
    public static String chosenTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_main);

        quizList = findViewById(R.id.quizList);

        Questions = new ArrayList<>();

        //NAMES
        Questions.add(new Quiz_Name_Constructor(10,"UCC Admission Test Reviewer (Sample1)"));
        Questions.add(new Quiz_Name_Constructor(30,"UCC Admission Test Reviewer (Gen Know)"));
        Questions.add(new Quiz_Name_Constructor(3,"UCC Admission Test Reviewer (Sample)"));

        Quiz_NameAdapter adapter = new Quiz_NameAdapter(this, 0, Questions);
        quizList.setAdapter(adapter);

        //getQuizzes();

        quizList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //Quiz chosenquiz = new Quiz(Questions.get(i));
                chosenTopic = Questions.get(i).getQuizname();
                startActivity(new Intent(QuizNameMain.this, Quiz_Questions.class));
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
               // Questions.add(line);
            }
            reader.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}