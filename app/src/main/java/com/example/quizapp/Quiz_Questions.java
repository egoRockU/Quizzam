package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
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

import io.github.muddz.styleabletoast.StyleableToast;

public class Quiz_Questions extends AppCompatActivity {

    private TextView tvQuestion, tvItems;
    private ListView choicesList;
    private ProgressBar pgTimeLeft;
    private LinkedHashMap<String, List<String>> questions;
    private String answer;
    private int questionIndex;
    List<String> choices;
    public static int count, items, score, timeSec, timeInMillis;
    public static boolean hasBeenCompleted;
    private static CountDownTimer countDownTimer;
    private static long startTime, endTime;
    public static long runningTime;

    TextView tvSubject;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        hasBeenCompleted = false;

        tvSubject = findViewById(R.id.tvSubject);
        tvQuestion = findViewById(R.id.tvQuestion);
        tvItems = findViewById(R.id.tvItems);
        choicesList = findViewById(R.id.choicesList);
        pgTimeLeft = findViewById(R.id.pgTimeLeft);

        startTime = System.currentTimeMillis();
        startQuiz();

        tvSubject.setText(QuizNameMain.subjectName);

    }
    private void getQuestions(byte[] bytes) {
        String question = "";
        List<String> choices = new ArrayList<>();
        questions = new LinkedHashMap<>();
        try {
            InputStream inputStream = new ByteArrayInputStream(bytes);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            //creating pattern for matching
            Pattern qPat = Pattern.compile("^Q. ");
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


    private void startQuiz(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference fileRef = storage.getReference().child("QuizFolder/" + QuizNameMain.chosenTopic);
        final long ONE_MEGABYTE = 1024*1024;

        fileRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                getQuestions(bytes);

                String correctAnswer = "a";

                List<String> questionsKeys = new ArrayList<>(questions.keySet());
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
                        if (choices.get(i).equals(answer)){
                            StyleableToast.makeText(Quiz_Questions.this, "Correct!", Toast.LENGTH_SHORT, R.style.ToastCorrect).show();
                            score += 1;
                            countDownTimer.cancel();
                            questionsKeys.remove(questionIndex);
                        } else {
                            StyleableToast.makeText(Quiz_Questions.this, "Wrong!", Toast.LENGTH_SHORT, R.style.ToastWrong).show();
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
                            hasBeenCompleted = true;
                            startActivity(new Intent(Quiz_Questions.this, Result.class));
                        }

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Failure on download");
                Log.e("FireBase Storage", e.getMessage());
            }
        });

    }

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
        tvQuestion.setText(questionsKeys.get(questionIndex).replaceAll("^Q. ",""));
        //iterate the choices arraylist, remove the last character, add to new arraylist. this is only for display purposes.
        choices = questions.get(questionsKeys.get(questionIndex));
        for(String c: choices){
            c = c.replaceAll(".$","");
            displayChoices.add(c);
        }
        tvItems.setText(count + " of " + items);

        Quiz_Choices_Adapter adapter = new Quiz_Choices_Adapter(this, 0, displayChoices);
        choicesList.setAdapter(adapter);

        return answer;
    }


    private void startQuizTimer(String correctAnswer, Random random, List<String> questionsKeys, List<String> displayChoices, int items){
        getData();

        pgTimeLeft.setMax(timeSec);
        pgTimeLeft.setVisibility(View.VISIBLE);

        countDownTimer = new CountDownTimer(timeInMillis, 1000) {
            public void onTick(long millisUntilFinished) {
                pgTimeLeft.setProgress(timeSec);
                timeSec--;
            }
            public void onFinish() {
                timeSec--;
                pgTimeLeft.setProgress(timeSec);
                questionsKeys.remove(questionIndex);
                cancel();
                if (questionsKeys.size() != 0){
                    answer = loadQuestions(correctAnswer, random, questionsKeys, displayChoices, items);
                    startQuizTimer(correctAnswer, random, questionsKeys, displayChoices, items);
                } else {
                    finish();
                    endTime = System.currentTimeMillis();
                    runningTime = endTime - startTime;
                    hasBeenCompleted = true;
                    startActivity(new Intent(Quiz_Questions.this, Result.class));
                }
            }
        }.start();
    }

    public void getData(){
        TimeDBHelper tDbHelper = new TimeDBHelper(Quiz_Questions.this);
        Cursor cursor = tDbHelper.readData();
        if (cursor.getCount() == 0){
            tDbHelper.add(14,15000);
        }
        if(cursor.getCount()==1 && cursor.moveToFirst()){
            timeSec = cursor.getInt(1);
            timeInMillis = cursor.getInt(2);
        }
        tDbHelper.close();
        cursor.close();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        countDownTimer.cancel();
        finish();
    }
}