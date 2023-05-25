package com.example.quizapp;

import java.util.ArrayList;

public class adminQuestion {
    private String question;
    private ArrayList<String> choices;

    public adminQuestion(String question, ArrayList<String> choices) {
        this.question = question;
        this.choices = choices;
    }

    public ArrayList<String> getChoices() {
        return choices;
    }

    public String getQuestion() {
        return question;
    }
}
