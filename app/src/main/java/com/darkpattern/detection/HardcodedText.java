package com.darkpattern.detection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HardcodedText extends AppCompatActivity {

    private Button Reg_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hardcoded_text);

        Reg_btn = findViewById(R.id.ComplaintBtn);

        Reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HardcodedText.this, MainActivity.class);
                intent.putExtra("fragment_to_load", "complaint");
                startActivity(intent);
            }
        });
    }
}