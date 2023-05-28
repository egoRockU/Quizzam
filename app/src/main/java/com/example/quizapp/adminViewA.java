package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import io.github.muddz.styleabletoast.StyleableToast;

public class adminViewA extends AppCompatActivity {

    private List<Quiz_Name_Constructor> Quizzes;
    public static String subjectName;
    public static String chosenTopic;

    ListView lvShowQuiz;

    FirebaseStorage storage;
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_a);

        lvShowQuiz = findViewById(R.id.lvShowQuiz);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        getQuizList();

    }

    private void getQuizList(){
        StorageReference folderRef = storageRef.child("QuizFolder");
        Quizzes = new ArrayList<>();

        folderRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference item : listResult.getItems()){
                    String itemName = item.getName();
                    String itemFileName = itemName;
                    int itemCount = getItemCount(itemName);
                    String subject = getSubject(itemName);
                    Quizzes.add(new Quiz_Name_Constructor(itemCount, subject, itemFileName));
                }
                Quiz_NameAdapter adapter = new Quiz_NameAdapter(adminViewA.this, 0, Quizzes);
                lvShowQuiz.setAdapter(adapter);

                lvShowQuiz.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        subjectName = Quizzes.get(i).getQuizname();
                        chosenTopic = Quizzes.get(i).getQuizFileName();
                        startActivity(new Intent(adminViewA.this, adminViewB.class));
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                StyleableToast.makeText(adminViewA.this, "Please make sure you have internet connection", Toast.LENGTH_SHORT, R.style.ToastWrong).show();
            }
        });
    }

    private String getSubject(String itemName){
        itemName = itemName.replaceAll(".txt", "");
        itemName = itemName.replaceAll("\\[[^\\]]*\\]", "");
        return itemName;
    }

    private int getItemCount(String itemName){
        int itemCount;
        String[] items;
        items = itemName.split("]");
        itemName = items[0];
        itemName = itemName.replaceAll("[\\[]", "");
        itemCount = Integer.parseInt(itemName);
        return itemCount;
    }
}