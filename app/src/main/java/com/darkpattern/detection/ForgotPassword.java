package com.darkpattern.detection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import java.util.Objects;

public class ForgotPassword extends AppCompatActivity {

    private TextInputEditText email;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email = findViewById(R.id.email);
        Button reset_btn = findViewById(R.id.resetbtn);
        TextView goToRegister = findViewById(R.id.RegisterNow);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        goToRegister.setOnClickListener(view -> startRegisterActivity());


        reset_btn.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);

            if(checkInput()){

                sendEmail();
            }
        });
    }
    private void sendEmail() {
        String mail = Objects.requireNonNull(email.getText()).toString();

        mAuth.sendPasswordResetEmail(mail)
                .addOnSuccessListener(unused -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ForgotPassword.this, "Reset Password Link Sent Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    if (e instanceof FirebaseAuthInvalidUserException) {
                        // User does not exist
                        Toast.makeText(ForgotPassword.this, "Error! User does not exist", Toast.LENGTH_SHORT).show();
                    } else {
                        // Other error
                        Toast.makeText(ForgotPassword.this, "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private boolean checkInput() {
        String emailText = Objects.requireNonNull(email.getText()).toString();

        if (TextUtils.isEmpty(emailText)) {
            email.setError("Email is Required");
            progressBar.setVisibility(View.GONE);
            return false;
        }
        return true;
    }


    private void startRegisterActivity() {
        Intent intent = new Intent(getApplicationContext(), Register.class);
        startActivity(intent);
        finish();
    }
}