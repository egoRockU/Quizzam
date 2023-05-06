package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

public class Result extends AppCompatActivity {

    TextView tvCongratulatoryMsg, tvScore, tvSubject, tvTimeTaken;
    float score, rating;
    long timeInMillis, timeSec, timeMin, timeHour;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        tvScore = findViewById(R.id.tvScore);
        tvSubject = findViewById(R.id.tvSubject);
        tvCongratulatoryMsg = findViewById(R.id.tvConratulatoryMsg);
        tvTimeTaken = findViewById(R.id.tvTimeTaken);
        score = QuizActivity.score;

        rating = (score/QuizActivity.items) * 100;
        if (rating >= 90){
            tvCongratulatoryMsg.setText("Certified UCC Passer!");
        } else if (rating >= 75){
            tvCongratulatoryMsg.setText("Great Job Idol!");
        } else if (rating >= 50){
            tvCongratulatoryMsg.setText("Nice Effort!");
        } else {
            tvCongratulatoryMsg.setText("You can do it next time!");
        }

        timeInMillis = QuizActivity.runningTime;
        timeSec = timeInMillis/1000;
        timeMin = timeSec/60;
        timeHour = timeMin/60;


        tvSubject.setText("UCC Admission Test (" + ChooseQuizToStartPrompt.chosenTopic + ")");
        tvScore.setText((int)score + " pt");
        tvTimeTaken.setText(String.format("%02d:%02d:%02d", timeHour%60,timeMin%60, timeSec%60));


    }

    public void RetakeQuiz(View view){
        startActivity(new Intent(Result.this, QuizActivity.class));
    }

    public void Dashboard(View view){
        startActivity(new Intent(Result.this, ChooseQuizToStartPrompt.class));
    }
}