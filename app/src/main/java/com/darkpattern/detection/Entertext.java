package com.darkpattern.detection;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;


public class Entertext extends AppCompatActivity {

    private TextInputEditText entered_txt;
    private Button get_result;
    private ImageButton back;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entertext);

        entered_txt = findViewById(R.id.messageBox_notice);
        get_result = findViewById(R.id.uploadTxtBtn);
        back = findViewById(R.id.backbtn);

        back.setOnClickListener(view -> onBackPressed());

        get_result.setOnClickListener(view -> {

            if(validateInput()) {
                Intent intent = new Intent(getApplicationContext(), ResultText.class);
                startActivity(intent);
            }
        });
    }

    private boolean validateInput() {
        String txt = entered_txt.getText().toString().trim();
        if (txt.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Enter Text", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
