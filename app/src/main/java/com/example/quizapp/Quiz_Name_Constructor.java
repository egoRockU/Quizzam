package com.example.quizapp;

public class Quiz_Name_Constructor {
    public int quizitem;
    public String quizname;

    public String quizFileName;

    public Quiz_Name_Constructor(int quizitem, String quizname, String quizFileName) {
        this.quizitem = quizitem;
        this.quizname = quizname;
        this.quizFileName = quizFileName;
    }

    public int getQuizitem() {
        return quizitem;
    }

    public String getQuizname() {
        return quizname;
    }

    public String getQuizFileName() {
        return quizFileName;
    }
}
