package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Result extends AppCompatActivity {

    TextView tvCongratulatoryMsg, tvScore, tvSubject, tvTimeTaken, tvPerfectQuizValues;
    float score, items, rating;
    long timeInMillis, timeSec, timeMin, timeHour;
    boolean isQuizCompleted;

    int highScore, quizCompleted, perfectQuiz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


        tvScore = findViewById(R.id.tvScore);
        tvSubject = findViewById(R.id.tvSubject);
        tvCongratulatoryMsg = findViewById(R.id.tvConratulatoryMsg);
        tvTimeTaken = findViewById(R.id.tvTimeTaken);
        tvPerfectQuizValues = findViewById(R.id.tvPerfectQuizValues);
        score = Quiz_Questions.score;
        items = Quiz_Questions.items;
        isQuizCompleted = Quiz_Questions.hasBeenCompleted;

        updateStats();


        rating = (score/ Quiz_Questions.items) * 100;
        if (rating >= 90){
            tvCongratulatoryMsg.setTextSize(40);
            tvCongratulatoryMsg.setText("Certified UCC Passer!");
        } else if (rating >= 75){
            tvCongratulatoryMsg.setText("Great Job Idol!");
        } else if (rating >= 50){
            tvCongratulatoryMsg.setText("Nice Effort!");
        } else {
            tvCongratulatoryMsg.setText("Just do it!");
        }


        timeInMillis = Quiz_Questions.runningTime;
        timeSec = timeInMillis/1000;
        timeMin = timeSec/60;
        timeHour = timeMin/60;


        tvSubject.setText(QuizNameMain.subjectName);
        tvScore.setText((int)score + " pt");
        tvTimeTaken.setText(String.format("%02d:%02d", timeMin, timeSec%60));


    }

    private void updateStats(){
        highScore = QuizNameMain.HighScore;
        quizCompleted = QuizNameMain.QuizCompleted;
        perfectQuiz = QuizNameMain.PerfectQuiz;

        if ((int)score>highScore){
            highScore = (int)score;
        }
        if ((score/items) == 1){
            perfectQuiz += 1;
        }
        if (isQuizCompleted){
            quizCompleted += 1;
        }

        DatabaseHelper db = new DatabaseHelper(Result.this);
        db.update("1", highScore, quizCompleted, perfectQuiz);
        tvPerfectQuizValues.setText(String.valueOf(perfectQuiz));
        db.close();
    }


    public void RetakeQuiz(View view){
        startActivity(new Intent(Result.this, Quiz_Questions.class));
    }

    public void Dashboard(View view){
        startActivity(new Intent(Result.this, QuizNameMain.class));
    }
}