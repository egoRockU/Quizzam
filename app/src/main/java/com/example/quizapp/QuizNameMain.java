package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class QuizNameMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public DrawerLayout drawerLayout;
    Menu menu;
    NavigationView navigationView;
    androidx.appcompat.widget.Toolbar Toolbar;


    private static List<Quiz_Name_Constructor> Questions;
    private ListView quizList;
    public static String chosenTopic;

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

        quizList = findViewById(R.id.quizList);

        Questions = new ArrayList<>();

        //NAMES
        Questions.add(new Quiz_Name_Constructor(10,"UCC Admission Test Reviewer (Sample1)"));
        Questions.add(new Quiz_Name_Constructor(30,"UCC Admission Test Reviewer (Gen Know)"));
        Questions.add(new Quiz_Name_Constructor(3,"UCC Admission Test Reviewer (Sample)"));

        Quiz_NameAdapter adapter = new Quiz_NameAdapter(this, 0, Questions);
        quizList.setAdapter(adapter);

        //getQuizzes();

        quizList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //Quiz chosenquiz = new Quiz(Questions.get(i));
                chosenTopic = Questions.get(i).getQuizname();
                startActivity(new Intent(QuizNameMain.this, Quiz_Questions.class));
            }
        });
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
            default:
                System.out.println("no one matches");
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    private void getQuizzes(){

        try {
            Questions.clear(); //need to be cleared so that when called again, it won't iterate
            AssetManager assetManager = getAssets();
            InputStream inputStream = assetManager.open("QuizFolder/-QUIZZES.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null ){
                line = line.replaceAll(".txt", ""); //Remove .txt so it wont appear in listview
               // Questions.add(line);
            }
            reader.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}