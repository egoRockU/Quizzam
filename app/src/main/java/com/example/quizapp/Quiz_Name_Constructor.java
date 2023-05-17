package com.example.quizapp;

public class Quiz_Name_Constructor {
    public int quizitem;
    public String quizname;

    public Quiz_Name_Constructor(int quizitem, String quizname) {
        this.quizitem = quizitem;
        this.quizname = quizname;
    }

    public int getQuizitem() {
        return quizitem;
    }

    public String getQuizname() {
        return quizname;
    }
}
