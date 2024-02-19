package com.darkpattern.detection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class ResultText extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_text);

        // Create a handler to post a delayed task to navigate to another activity after 5 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the new activity
                Intent intent = new Intent(ResultText.this, HardcodedText.class);
                startActivity(intent);
                finish();

                // Finish this activity
                finish();
            }
        }, 4000); // 5000 milliseconds = 5 seconds
    }
}