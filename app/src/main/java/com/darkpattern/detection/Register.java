package com.darkpattern.detection;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.darkpattern.detection.model.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class Register extends AppCompatActivity {

    private TextInputEditText name_text;
    private TextInputEditText email_text;
    private TextInputEditText password_text;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fstore;
    ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name_text = findViewById(R.id.name);
        email_text = findViewById(R.id.email);
        password_text = findViewById(R.id.password);
        Button register_btn = findViewById(R.id.registerbtn);
        TextView login_now = findViewById(R.id.LoginNow);
        fstore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(Register.this);
        progressDialog.setTitle("Please Wait!");
        progressDialog.setMessage("Registering...");
        progressDialog.setCancelable(false);

        register_btn.setOnClickListener(view -> {
            if (validateInputs()) {
                performRegistration();
            }
        });

        login_now.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
        });
    }

    private void performRegistration() {
        // Display a progress dialog

        progressDialog.show();

        String email = Objects.requireNonNull(email_text.getText()).toString().trim();
        String password = Objects.requireNonNull(password_text.getText()).toString().trim();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        saveUserDataToFirestore();
                    } else {
                        Toast.makeText(Register.this, "Registration failed. " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserDataToFirestore() {
        String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        String name = Objects.requireNonNull(name_text.getText()).toString().trim();
        String email = Objects.requireNonNull(email_text.getText()).toString().trim();


        // Create a User object
        User user = new User(userID,null,name,email);

        // Save the user data to Firestore
        DocumentReference documentReference = fstore.collection("users").document(userID);
        documentReference.set(user).addOnSuccessListener(unused -> {

            // Send email verification
            sendEmailVerification();
            progressDialog.dismiss();
            // Display a success dialog
            showVerificationDialog();

        }).addOnFailureListener(e -> Toast.makeText(Register.this, "Registration Failed"+e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void showVerificationDialog() {
        // Display a dialog box informing the user that a verification email has been sent
        // You can customize the dialog content as needed
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Email Verification!")
                .setMessage("A verification email has been sent to your email address. Please check your email and verify your account.")
                .setPositiveButton("Ok", (dialogInterface, i) -> {

                    dialogInterface.dismiss();
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    Toast.makeText(Register.this, "Please Verifiy Your Email Before Logging In", Toast.LENGTH_LONG).show();

                })
                .show();
    }

    private void sendEmailVerification() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(Register.this, "Verification Email Sent Successfully", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(Register.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    private boolean validateInputs() {
        String name = Objects.requireNonNull(name_text.getText()).toString().trim();
        String email = Objects.requireNonNull(email_text.getText()).toString().trim();
        String password = Objects.requireNonNull(password_text.getText()).toString().trim();

        if (TextUtils.isEmpty(name)) {
            name_text.setError("Name is Required.");
            return false;
        }
        if (TextUtils.isEmpty(email)) {
            email_text.setError("Roll Number is Required.");
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            password_text.setError("Password is Required.");
            return false;
        }
        if (password.length() < 6) {
            password_text.setError("Password must be >= 6 characters.");
            return false;
        }
        return true;
    }
}