package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;


public class LogIn extends AppCompatActivity {

    private FirebaseAuth mAuth;
    public static FirebaseUser user;
    private EditText edTxtEnterEmail, edTxtEnterPassword;
    private String email, password;
    private Button btnSignIn;
    private TextView tvForgotPassword, tvContribute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        edTxtEnterEmail = findViewById(R.id.edTxtEnterEmail);
        edTxtEnterPassword = findViewById(R.id.edTxtEnterPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvContribute = findViewById(R.id.tvContibute);

        mAuth = FirebaseAuth.getInstance();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = edTxtEnterEmail.getText().toString();
                password = edTxtEnterPassword.getText().toString();

                if (email.isEmpty()){
                    edTxtEnterEmail.setError("Please enter your email");
                } else if (password.isEmpty()){
                    edTxtEnterPassword.setError("Please enter your password.");
                } else {
                    signIn(email,password);
                }
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = edTxtEnterEmail.getText().toString();
                if (email.isEmpty()){
                    edTxtEnterEmail.setError("Please enter your email");
                } else {
                    forgotPassword(email);
                }
            }
        });

        tvContribute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertContribute();
            }
        });

    }

    void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    user = mAuth.getCurrentUser();
                    startActivity(new Intent(LogIn.this, AdminDashboard.class));
                } else {
                    Toast.makeText(LogIn.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void forgotPassword(String email){
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(LogIn.this, "Email Sent!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LogIn.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void alertContribute(){
        AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
        alert.setTitle("How to be a Contributor?");
        alert.setMessage("1. Indicate your full name.\n2.Indicate Your Profession \n3. Are you related to University of Caloocan City by any chance? \n4. Tell us why do you want to be a contributor? \n5. Your Email\n" +
                "Developers will give you a feedback once your application was reviewed.");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openGmail();
                finish();
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alert.create().show();
    }

    private void openGmail(){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + "quizzam.devs@gmail.com"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Contributor Application");
        intent.putExtra(Intent.EXTRA_TEXT, "1. Indicate your full name.\n2.Indicate Your Profession \n3. Are you related to University of Caloocan City by any chance? \n4. Tell us why do you want to be a contributor? \n5. Your Email\n" +
                "Developers will give you a feedback once your application was reviewed.");

        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, 0);


        if (resolveInfoList.size() > 0) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "No email app found.", Toast.LENGTH_SHORT).show();
        }
    }

}