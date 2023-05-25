package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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


public class LogIn extends AppCompatActivity {

    private FirebaseAuth mAuth;
    public static FirebaseUser user;
    private EditText edTxtEnterEmail, edTxtEnterPassword;
    private String email, password;
    private Button btnSignIn;
    private TextView tvForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        edTxtEnterEmail = findViewById(R.id.edTxtEnterEmail);
        edTxtEnterPassword = findViewById(R.id.edTxtEnterPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

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

}