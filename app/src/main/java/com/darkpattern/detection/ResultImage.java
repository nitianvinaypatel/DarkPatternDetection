package com.darkpattern.detection;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class ResultImage extends AppCompatActivity {

    private TextView text_result;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_image);

        text_result = findViewById(R.id.text);

        Intent intent = getIntent();
        String text = intent.getStringExtra("text");
        text_result.setText(text);

    }
}