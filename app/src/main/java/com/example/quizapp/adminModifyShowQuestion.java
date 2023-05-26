package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class adminModifyShowQuestion extends AppCompatActivity {

    Button btnDoneModify;
    ListView lvQuestions;
    static LinkedHashMap<String, List<String>> questionsSet;
    static String chosenQuestion;
    static ArrayList<String> chosenChoices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_modify_show_question);

        questionsSet = new LinkedHashMap<>();

        btnDoneModify = findViewById(R.id.btnDoneModify);
        lvQuestions = findViewById(R.id.lvQuestions);

        listQuestions();


    }

    private void getQuestions(byte[] bytes) {
        String question = "";
        List<String> choices = new ArrayList<>();
        questionsSet = new LinkedHashMap<>();
        try {
            InputStream inputStream = new ByteArrayInputStream(bytes);
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
                    questionsSet.put(question, choices);
                }

            }
            reader.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void listQuestions(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference fileRef = storage.getReference().child("QuizFolder/" + adminModifyQuiz.chosenTopic);
        final long ONE_MEGABYTE = 1024*1024;

        fileRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                getQuestions(bytes);

                ArrayList<String> questions = new ArrayList<>(questionsSet.keySet());
                ArrayAdapter<String> adapter = new ArrayAdapter<>(adminModifyShowQuestion.this, android.R.layout.simple_list_item_1, questions);
                lvQuestions.setAdapter(adapter);
                lvQuestions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        chosenQuestion = questions.get(position);
                        chosenChoices = new ArrayList<>(questionsSet.get(chosenQuestion));
                        startActivity(new Intent(adminModifyShowQuestion.this, adminEditQuiz.class));

                    }
                });
                System.out.println(questions);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Failure to download");
            }
        });
    }

}