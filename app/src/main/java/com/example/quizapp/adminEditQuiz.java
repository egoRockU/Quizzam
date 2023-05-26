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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class adminEditQuiz extends AppCompatActivity {


    FirebaseStorage storage;
    StorageReference storageRef;
    Button btnSaveChanges;
    EditText edEditQuestion, edEditChoiceA, edEditChoiceB, edEditChoiceC, edEditChoiceD, edEditChoiceE;
    String selectedQuestion, question, choiceA, choiceB, choiceC, choiceD, choiceE;
    ArrayList<String> choices;
    LinkedHashMap<String, List<String>> changedSet;

    char[] choiceLetters = {'A', 'B', 'C', 'D', 'E'};
    static ArrayList<String> savedChoices;
    boolean questionSaved;
    static adminQuestion questionBlock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_quiz);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference().child("QuizFolder/");

        changedSet = new LinkedHashMap<>(adminModifyShowQuestion.questionsSet);
        selectedQuestion = adminModifyShowQuestion.chosenQuestion;
        edEditQuestion = findViewById(R.id.edEditQuestion);
        edEditChoiceA = findViewById(R.id.edEditChoiceA);
        edEditChoiceB = findViewById(R.id.edEditChoiceB);
        edEditChoiceC = findViewById(R.id.edEditChoiceC);
        edEditChoiceD = findViewById(R.id.edEditChoiceD);
        edEditChoiceE = findViewById(R.id.edEditChoiceE);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);

        choices = new ArrayList<>(adminModifyShowQuestion.chosenChoices);
        show();

        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
                if(questionSaved){
                    changedSet.remove(selectedQuestion);
                    changedSet.put(questionBlock.getQuestion(), questionBlock.getChoices());
                    updateQuiz();
                    Toast.makeText(adminEditQuiz.this, "Update Successful", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(adminEditQuiz.this, adminModifyQuiz.class));
                }
            }
        });

    }
    private void show(){
        edEditQuestion.setText(adminModifyShowQuestion.chosenQuestion);

        if (choices.size()>=1){
            choiceA = choices.get(0).replaceAll("-$", "");
            choiceA = choiceA.replaceAll("[A-E][.]", "");
            edEditChoiceA.setText(choiceA);
        }
        if (choices.size()>=2){
            choiceB = choices.get(1).replaceAll("-$", "");
            choiceB = choiceB.replaceAll("[A-E][.]", "");
            edEditChoiceB.setText(choiceB);
        }
        if(choices.size()>=3){
            choiceC = choices.get(2).replaceAll("-$", "");
            choiceC = choiceC.replaceAll("[A-E][.]", "");
            edEditChoiceC.setText(choiceC);
        }
        if(choices.size()>=4){
            choiceD = choices.get(3).replaceAll("-$", "");
            choiceD = choiceD.replaceAll("[A-E][.]", "");
            edEditChoiceD.setText(choiceD);
        }
        if(choices.size()>=5){
            choiceE = choices.get(4).replaceAll("-$", "");
            choiceE = choiceE.replaceAll("[A-E][.]", "");
            edEditChoiceE.setText(choiceE);
        }
    }

    private void save(){
        int num=0;
        questionSaved = false;
        savedChoices = new ArrayList<>();
        choiceA = edEditChoiceA.getText().toString();
        choiceB = edEditChoiceB.getText().toString();
        choiceC = edEditChoiceC.getText().toString();
        choiceD = edEditChoiceD.getText().toString();
        choiceE = edEditChoiceE.getText().toString();

        if (choiceA.length()!=0){
            savedChoices.add(choiceA);
        }
        if (choiceB.length()!=0){
            savedChoices.add(choiceB);
        }
        if (choiceC.length()!=0){
            savedChoices.add(choiceC);
        }
        if (choiceD.length()!=0){
            savedChoices.add(choiceD);
        }
        if (choiceE.length()!=0){
            savedChoices.add(choiceE);
        }

        for(String s: savedChoices){
            savedChoices.set(num, choiceLetters[num]+". "+s);
            num+=1;
        }
        boolean answerValidity = checkAns();
        boolean questionValidity = checkQuestion();
        if (answerValidity && questionValidity){
            questionSaved = true;
            questionBlock = new adminQuestion(question, savedChoices);
        }

    }

    private boolean checkAns(){
        int numOfAns = 0;
        for(String s: savedChoices){
            if (s.endsWith("*")){
                numOfAns+=1;
            }
        }
        if(numOfAns==1){
            return true;
        }else{
            Toast.makeText(this, "Not Enough or Too much asterisk.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    private boolean checkQuestion(){
        question = edEditQuestion.getText().toString();
        if (question.length() != 0){
            Pattern qPat = Pattern.compile("\\d{1,3}[.] .*");
            Matcher Qm = qPat.matcher(question);
            if (Qm.find()){
                return true;
            } else{
                edEditQuestion.setError("Please don't forget item number followed by space. (Ex.\"1. \")");
                return false;
            }
        }else{
            edEditQuestion.setError("Please don't forget the question.");
            return false;
        }
    }

    private void deleteQuiz(){
        StorageReference fileRef = storageRef.child(adminModifyQuiz.chosenTopic);
        fileRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                System.out.println("Deleted Successfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Deletion Failed");
            }
        });
    }
    private void updateQuiz(){
        String internalStorageDir = getFilesDir().getAbsolutePath();
        String fileName = adminModifyQuiz.chosenTopic;
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(internalStorageDir + "/" + fileName));
            for (Map.Entry<String,List<String>> entrySet: changedSet.entrySet()){
                writer.write(entrySet.getKey()+"\n");
                for (String c: entrySet.getValue()){
                    if (c.endsWith("*")){
                        writer.write(c+"\n");
                    }else{
                        writer.write(c+"-\n");
                    }
                }
                writer.write("---");
            }
            writer.close();

            //delete First
            deleteQuiz();

            //upload
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

        }catch (Exception e){
            System.out.println("Writing Failed");
            e.getStackTrace();
        }

    }
}