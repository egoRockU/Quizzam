package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class QuizNameMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public DrawerLayout drawerLayout;
    NavigationView navigationView;
    androidx.appcompat.widget.Toolbar Toolbar;


    private static List<Quiz_Name_Constructor> Questions;
    private ListView quizList;
    public static String subjectName;
    public static String chosenTopic;

    FirebaseStorage storage;
    StorageReference storageRef;

    //Database
    static DatabaseHelper dbHelper;
    static Cursor cursor;
    static int HighScore;
    static int QuizCompleted;
    static int PerfectQuiz;

    static TextView tvHighScore, tvPerfectQuiz, tvQuizCompleted;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_main);


        /* For Drawer */
        drawerLayout = findViewById(R.id.drawerlayoutdb);
        navigationView = findViewById(R.id.drawerview);
        Toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(Toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, Toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.dashboard);

        /* For Drawer */

        //User Stats
        tvHighScore = findViewById(R.id.tvHighScoreValues);
        tvQuizCompleted = findViewById(R.id.tvQuizCompletedValues);
        tvPerfectQuiz = findViewById(R.id.tvPerfectQuizValues);
        getData(QuizNameMain.this);


        //for quiz list view
        quizList = findViewById(R.id.quizList);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        getQuizList();
    }

    @Override
    public void onBackPressed() {
            if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                drawerLayout.closeDrawer(GravityCompat.START);
                }
            else {
                super.onBackPressed();
                }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.drawerSettings:
                Intent intent = new Intent(QuizNameMain.this, Settings.class);
                startActivity(intent);
                break;
            case R.id.drawerAboutUs:
                Intent intent1 = new Intent(QuizNameMain.this, About_Us.class);
                startActivity(intent1);
                break;
            case R.id.drawerOverview:
                Intent intent2 = new Intent(QuizNameMain.this, Overview.class);
                startActivity(intent2);
                break;
            default:
                System.out.println("no one matches");
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    private void getQuizList(){
        StorageReference folderRef = storageRef.child("QuizFolder");
        Questions = new ArrayList<>();

        folderRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference item : listResult.getItems()){
                    String itemName = item.getName();
                    String itemFileName = itemName;
                    int itemCount = getItemCount(itemName);
                    String subject = getSubject(itemName);
                    Questions.add(new Quiz_Name_Constructor(itemCount, subject, itemFileName));
                }

                Quiz_NameAdapter adapter = new Quiz_NameAdapter(QuizNameMain.this, 0, Questions);
                quizList.setAdapter(adapter);

                quizList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        subjectName = Questions.get(i).getQuizname();
                        chosenTopic = Questions.get(i).getQuizFileName();
                        startActivity(new Intent(QuizNameMain.this, Quiz_Questions.class));
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Firebase Storage", e.getMessage());
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

    public static void getData(Context context){
        dbHelper = new DatabaseHelper(context);
        cursor = dbHelper.readData();
        if (cursor.getCount() == 0){
            dbHelper.add(0,0,0);
        }
        if(cursor.getCount()==1 && cursor.moveToFirst()){
            HighScore = cursor.getInt(1);
            QuizCompleted = cursor.getInt(2);
            PerfectQuiz = cursor.getInt(3);
        }
        int highScore = HighScore;
        int quizCompleted = QuizCompleted;
        int perfectQuiz = PerfectQuiz;

        tvHighScore.setText(String.valueOf(highScore));
        tvQuizCompleted.setText(String.valueOf(quizCompleted));
        tvPerfectQuiz.setText(String.valueOf(perfectQuiz));


    }


}