package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import io.github.muddz.styleabletoast.StyleableToast;

public class Settings extends AppCompatActivity {


    TextView tvPressureLevel;
    static int timeInMillis, timeSec;
    Button btnResetProgress, btnPressureSlow, btnPressureMedium, btnPressureFast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        tvPressureLevel = findViewById(R.id.tvPressureLevel);
        btnPressureSlow = findViewById(R.id.btnPressureSlow);
        btnPressureMedium = findViewById(R.id.btnPressureMedium);
        btnPressureFast = findViewById(R.id.btnPressureFast);
        btnResetProgress = findViewById(R.id.btnResetProgress);


        getData();
        //for displaying in settings
        switch (timeSec){
            case 59:
                tvPressureLevel.setText("Pressure Level : Slow(60 seconds)");
                break;
            case 29:
                tvPressureLevel.setText("Pressure Level : Medium(30 seconds)");
                break;
            case 14:
                tvPressureLevel.setText("Pressure Level : Fast(15 seconds)");
                break;
            default:
                tvPressureLevel.setText("Pressure Level: ");
                break;
        }



        //ON CLICK LISTENERS-----------------------------------------
        btnPressureSlow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeSec = 59;
                timeInMillis = 60000;
                updateStats(timeSec, timeInMillis);
                tvPressureLevel.setText("Pressure Level : Slow(60 seconds)");
            }
        });

        btnPressureMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeSec= 29;
                timeInMillis = 30000;
                updateStats(timeSec, timeInMillis);
                tvPressureLevel.setText("Pressure Level : Medium(30 seconds)");
            }
        });

        btnPressureFast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeSec = 14;
                timeInMillis = 15000;
                updateStats(timeSec, timeInMillis);
                tvPressureLevel.setText("Pressure Level : Fast(15 seconds)");
            }
        });


        btnResetProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetData();
                QuizNameMain.getData(Settings.this);
                StyleableToast.makeText(Settings.this, "All your progress is now gone.", Toast.LENGTH_SHORT, R.style.ToastResetProgress).show();
            }
        });

    }

    public void getData(){
        TimeDBHelper tDbHelper = new TimeDBHelper(Settings.this);
        Cursor cursor = tDbHelper.readData();
        if (cursor.getCount() == 0){
            tDbHelper.add(14,15000);
        }
        if(cursor.getCount()==1 && cursor.moveToFirst()){
            timeSec = cursor.getInt(1);
            timeInMillis = cursor.getInt(2);
        }
    }

    private void updateStats(int timeSec, int timeInMillis){
        TimeDBHelper tDbHelper = new TimeDBHelper(Settings.this);
        tDbHelper.update("1", timeSec, timeInMillis);
    }

    public void resetData(){
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        dbHelper.update("1", 0, 0, 0);
    }
}