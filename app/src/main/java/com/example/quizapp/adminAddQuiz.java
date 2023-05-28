package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class adminAddQuiz extends AppCompatActivity {

    EditText edQuizName, edWriteQuestion;
    EditText edChoiceA, edChoiceB, edChoiceC, edChoiceD, edChoiceE;
    ArrayList<adminQuestion> questions;
    Button btnNext, btnDone;

    char[] choiceLetters = {'A', 'B', 'C', 'D', 'E'};
    FirebaseStorage storage;
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_quiz);

        questions = new ArrayList<>();

        edQuizName = findViewById(R.id.edQuizName);
        edWriteQuestion = findViewById(R.id.edWriteQuestion);
        edChoiceA = findViewById(R.id.edChoiceA);
        edChoiceB = findViewById(R.id.edChoiceB);
        edChoiceC = findViewById(R.id.edChoiceC);
        edChoiceD = findViewById(R.id.edChoiceD);
        edChoiceE = findViewById(R.id.edChoiceE);
        btnNext = findViewById(R.id.btnNext);
        btnDone = findViewById(R.id.btnDone);


        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference().child("QuizFolder/");

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getQuestion();
                System.out.println(questions);
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quizName = edQuizName.getText().toString();
                if (quizName.isEmpty()){
                    edQuizName.setError("Please enter quiz name.");
                } else {
                    if (questions.size() == 0){
                        Toast.makeText(adminAddQuiz.this, "You haven't add any questions.", Toast.LENGTH_SHORT).show();
                    } else{
                        int items = questions.size();
                        uploadQuestion(quizName, items);
                        Toast.makeText(adminAddQuiz.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(adminAddQuiz.this, AdminDashboard.class));
                    }

                }
            }
        });

    }

    private void getQuestion(){
        int numOfAnswer = 0;
        ArrayList<String> choices = new ArrayList<>();
        String question = edWriteQuestion.getText().toString();
        String choiceA = edChoiceA.getText().toString();
        String choiceB = edChoiceB.getText().toString();
        String choiceC = edChoiceC.getText().toString();
        String choiceD = edChoiceD.getText().toString();
        String choiceE = edChoiceE.getText().toString();

        if (choiceA.length() != 0){
            choices.add(choiceA);
        }
        if (choiceB.length() != 0){
            choices.add(choiceB);
        }
        if (choiceC.length() != 0){
            choices.add(choiceC);
        }
        if (choiceD.length() != 0){
            choices.add(choiceD);
        }
        if (choiceE.length() != 0){
            choices.add(choiceE);
        }

        for(String c: choices){
            if (c.endsWith("*")){
                numOfAnswer += 1;
            }
        }
        if (numOfAnswer!=1){
            Toast.makeText(this, "Not enough or too much assigned correct answer.", Toast.LENGTH_SHORT).show();
        } else {
            if (question.isEmpty()){
                edWriteQuestion.setError("Please write a question");
            } else {
                questions.add(new adminQuestion(question, choices));
                Toast.makeText(this, "Question Added.", Toast.LENGTH_SHORT).show();
                edWriteQuestion.setText("");
                edChoiceA.setText("");
                edChoiceB.setText("");
                edChoiceC.setText("");
                edChoiceD.setText("");
                edChoiceE.setText("");
            }
        }


    }

    private void uploadQuestion(String quizName, int items) {
        try {
            String internalStorageDir = getFilesDir().getAbsolutePath();
            String fileName = "["+items+"]"+quizName+".txt";
            BufferedWriter writer = new BufferedWriter(new FileWriter(internalStorageDir+"/"+fileName));
            for (adminQuestion QUESTION: questions){
                int choiceIndex = 0;
                String question = QUESTION.getQuestion();
                writer.write("Q. "+question+"\n");
                for(String c: QUESTION.getChoices()){
                    if (c.endsWith("*")){
                        writer.write(choiceLetters[choiceIndex]+". "+c+"\n");
                        choiceIndex++;
                    } else {
                        writer.write(choiceLetters[choiceIndex]+". "+c+"-"+"\n");
                        choiceIndex++;
                    }
                }
                writer.write("---"+"\n");
            }
            writer.close();
            System.out.println(internalStorageDir);

            //Uploading file
            Uri file = Uri.fromFile(new File(internalStorageDir+"/"+fileName));
            StorageReference testRef = storageRef.child(file.getLastPathSegment());
            UploadTask uploadTask = testRef.putFile(file);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println("Upload Failed");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    System.out.println("Successful upload.");
                }
            });
        } catch (Exception e){
            System.out.println("Writing Failed");
            e.getStackTrace();
        }
    }
}