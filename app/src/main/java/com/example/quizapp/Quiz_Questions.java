package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Quiz_Questions extends AppCompatActivity {

    private TextView tvQuestion, tvItems;
    private ListView choicesList;
    private ProgressBar pgTimeLeft;
    private LinkedHashMap<String, List<String>> questions;
    private String answer;//this is only for quiz function
    private int questionIndex;
    List<String> choices;
    public static int count, items, score, timeLeft;
    private static CountDownTimer countDownTimer;
    private static long startTime, endTime;
    public static long runningTime;
    TextView tvSubject;
    public List<Quiz_Choices_Constructor> Choices;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        tvSubject = findViewById(R.id.tvSubject);
        tvQuestion = findViewById(R.id.tvQuestion);
        tvItems = findViewById(R.id.tvItems);
        choicesList = findViewById(R.id.choicesList);
        pgTimeLeft = findViewById(R.id.pgTimeLeft);

        startTime = System.currentTimeMillis();
        startQuiz();

        tvSubject.setText("UCC Admission Test (" + QuizNameMain.chosenTopic + ")");

        //Eto dinagdag ko, ewan lang kung tama

    }
    //GET QUESTIONS
    private void getQuestions() {
        String question = "";
        List<String> choices = new ArrayList<>();
        questions = new LinkedHashMap<>();
        try {
            AssetManager assetManager = getAssets();
            InputStream inputStream = assetManager.open("QuizFolder/"+ QuizNameMain.chosenTopic+".txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            //creating pattern for matching
            Pattern qPat = Pattern.compile("\\d{1,3}[.] .*");
            Pattern cPat = Pattern.compile("[A-E][.] .*[^*]");
            Matcher Qm, Cm;

            while ((line = reader.readLine()) != null) {
                Qm = qPat.matcher(line);
                Cm = cPat.matcher(line);
                //if line matches..
                if (Qm.find()) {
                    question = line;
                    choices = new ArrayList<>();//declare new arraylist to clear the choices. since new question has been set.
                }
                if (Cm.find()) {
                    choices.add(line);
                }
                if (line.equals("---")){
                    questions.put(question, choices);
                }

            }
            reader.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    //START QUIZ
    private void startQuiz(){
        getQuestions();
        String correctAnswer = "a";

        List<String> questionsKeys = new ArrayList<>(questions.keySet()); //list of keys(questions)
        choices = new ArrayList<>(); //clear out choices because it has value from getQuestions
        List<String> displayChoices = new ArrayList<>();
        Random random = new Random();

        count = 0;
        items = questionsKeys.size();
        score = 0;

        //Display loop
        answer = loadQuestions(correctAnswer, random, questionsKeys, displayChoices, items);
        startQuizTimer(correctAnswer, random, questionsKeys, displayChoices, items);
        choicesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //String answer = loadQuestions(correctAnswer, random, questionsKeys, displayChoices);
                if (choices.get(i).equals(answer)){
                    Toast.makeText(Quiz_Questions.this, "Correct!", Toast.LENGTH_SHORT).show();
                    score += 1;
                    countDownTimer.cancel();
                    questionsKeys.remove(questionIndex);
                } else {
                    Toast.makeText(Quiz_Questions.this, "Wrong!", Toast.LENGTH_SHORT).show();
                    questionsKeys.remove(questionIndex);
                    countDownTimer.cancel();
                }

                if (questionsKeys.size() != 0){
                    answer = loadQuestions(correctAnswer, random, questionsKeys, displayChoices, items);
                    startQuizTimer(correctAnswer, random, questionsKeys, displayChoices, items);
                } else {
                    finish();
                    endTime = System.currentTimeMillis();
                    runningTime = endTime - startTime;
                    startActivity(new Intent(Quiz_Questions.this, Result.class));
                }

            }
        });



    }

    //LOAD QUESTIONS
    private String loadQuestions(String correctAnswer, Random random, List<String> questionsKeys, List<String> displayChoices, int items){
        count++;
        questionIndex = random.nextInt(questionsKeys.size()); //get random index from questions keyset
        displayChoices.clear();
        //pattern for finding correct answer
        Pattern aPat = Pattern.compile("[A-E]. .*\\*");
        Matcher Am;
        //get the correct answer
        for (String a: questions.get(questionsKeys.get(questionIndex))){
            Am = aPat.matcher(a);
            if (Am.find()){
                correctAnswer = a;
            }
        }
        String answer = correctAnswer;
        //**** show questions and choices ****
        tvQuestion.setText(questionsKeys.get(questionIndex).replaceAll("\\d{1,3}[.]\\s",""));
        //iterate the choices arraylist, remove the last character, add to new arraylist. this is only for display purposes.
        choices = questions.get(questionsKeys.get(questionIndex));
        for(String c: choices){
            c = c.replaceAll(".$","");
            displayChoices.add(c);
        }
        tvItems.setText(count + " of " + items);

        //Nilagay ko

        Quiz_Choices_Adapter adapter = new Quiz_Choices_Adapter(this, 0, displayChoices);
        choicesList.setAdapter(adapter);

        return answer;
    }

    //QUIZ TIMER
    private void startQuizTimer(String correctAnswer, Random random, List<String> questionsKeys, List<String> displayChoices, int items){
        timeLeft = 14;

        //DINAGDAG KO - JHUDE
        pgTimeLeft.setVisibility(View.VISIBLE);
        //done

        countDownTimer = new CountDownTimer(15000, 1000) {
            public void onTick(long millisUntilFinished) {
                pgTimeLeft.setProgress(timeLeft);
                timeLeft--;
            }
            public void onFinish() {
                timeLeft--;
                pgTimeLeft.setProgress(timeLeft);
                questionsKeys.remove(questionIndex);
                cancel();
                if (questionsKeys.size() != 0){
                    answer = loadQuestions(correctAnswer, random, questionsKeys, displayChoices, items);
                    startQuizTimer(correctAnswer, random, questionsKeys, displayChoices, items);
                } else {
                    finish();
                    endTime = System.currentTimeMillis();
                    runningTime = endTime - startTime;
                    startActivity(new Intent(Quiz_Questions.this, Result.class));
                }
            }
        }.start();
    }


    @Override
    public void onBackPressed(){
        super.onBackPressed();
        countDownTimer.cancel();
        finish();
    }
}