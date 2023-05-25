package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import io.github.muddz.styleabletoast.StyleableToast;

public class MainActivity extends AppCompatActivity {


    TextView tvSwitchToAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvSwitchToAdmin = findViewById(R.id.tvSwitchToAdmin);
        tvSwitchToAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LogIn.class));
            }
        });

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