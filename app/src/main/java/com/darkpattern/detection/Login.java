package com.darkpattern.detection;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class Login extends AppCompatActivity {

    private TextInputEditText email_text;
    private TextInputEditText password_text;

    private ProgressDialog pd;

    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        checkCurrentUser();
    }

    private void checkCurrentUser() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null && currentUser.isEmailVerified())  {
            // User is logged in and email is verified, go to MainActivity
            startMainActivity();
        } else {
            if (currentUser != null) {
                currentUser.isEmailVerified();
            }//                    Toast.makeText(Login.this,"",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);
        pd = new ProgressDialog(this);
        TextView register_now = findViewById(R.id.RegisterNow);
        email_text = findViewById(R.id.email);
        password_text = findViewById(R.id.password);
        Button forgot_password = findViewById(R.id.forgotPassword);
        Button login_btn = findViewById(R.id.loginbtn);
        mAuth = FirebaseAuth.getInstance();
        pd.setTitle("Please Wait!");
        pd.setMessage("Taking you in...");
        pd.setCancelable(false);

        forgot_password.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(),ForgotPassword.class);
            startActivity(intent);
        });

        login_btn.setOnClickListener(view -> loginUser());

        register_now.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(),Register.class);
            startActivity(intent);
        });
    }

    private void loginUser() {
        pd.show();
        String email = Objects.requireNonNull(email_text.getText()).toString().trim();
        String password = Objects.requireNonNull(password_text.getText()).toString().trim();

        if (TextUtils.isEmpty(email)) {
            email_text.setError("Email is Required.");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            password_text.setError("Password is Required.");
            return;
        }


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this::handleLoginResult);
    }

    private void handleLoginResult(Task<AuthResult> task) {
        pd.dismiss();
        if (task.isSuccessful()) {
            Toast.makeText(Login.this, "Logged In Successfully.", Toast.LENGTH_SHORT).show();
            startMainActivity();
        } else {
            Toast.makeText(Login.this, "Invalid User Details.", Toast.LENGTH_SHORT).show();
        }
    }

    private void startMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}