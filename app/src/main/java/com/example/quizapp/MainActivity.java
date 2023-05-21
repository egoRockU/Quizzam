package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import io.github.muddz.styleabletoast.StyleableToast;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void Start(View view){
        if (isConnected()){
            startActivity(new Intent(MainActivity.this, QuizNameMain.class));
        } else {
            StyleableToast.makeText(MainActivity.this, "Please connect to the internet!", Toast.LENGTH_LONG , R.style.ToastNoWifi).show();
        }
    }

    boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null){
            if (networkInfo.isConnected()){
                return true;
            } else {
                return false;

            }
        } else {
            return false;
        }
    }
}