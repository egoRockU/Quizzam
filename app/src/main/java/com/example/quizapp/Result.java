package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

public class Result extends AppCompatActivity {

    TextView tvCongratulatoryMsg, tvScore, tvSubject;
    float score, rating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        tvScore = findViewById(R.id.tvScore);
        tvSubject = findViewById(R.id.tvSubject);
        tvCongratulatoryMsg = findViewById(R.id.tvConratulatoryMsg);
        score = QuizActivity.score;

        rating = (score/QuizActivity.items) * 100;
        System.out.println("Rating: "+rating);
        if (rating >= 90){
            tvCongratulatoryMsg.setText("Certified UCC Passer!");
        } else if (rating >= 75){
            tvCongratulatoryMsg.setText("Great Job Idol!");
        } else if (rating >= 50){
            tvCongratulatoryMsg.setText("Nice Effort!");
        } else {
            tvCongratulatoryMsg.setText("You can do it next time!");
        }

        tvSubject.setText("UCC Admission Test (" + ChooseQuizToStartPrompt.chosenTopic + ")");
        tvScore.setText((int)score + " pt");


    }

    public void RetakeQuiz(View view){
        startActivity(new Intent(Result.this, QuizActivity.class));
    }

    public void Dashboard(View view){
        startActivity(new Intent(Result.this, MainPrompt.class));
    }
}