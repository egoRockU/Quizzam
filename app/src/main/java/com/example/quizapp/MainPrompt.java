package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainPrompt extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_prompt);
    }

    public void StartQuiz(View view){
        startActivity(new Intent(MainPrompt.this, ChooseQuizToStartPrompt.class));
    }
}