package com.darkpattern.detection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.darkpattern.detection.Fragments.ComplaintFragment;

public class HardcodedImage extends AppCompatActivity {

    private Button Reg_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hardcoded_image);

        Reg_btn = findViewById(R.id.ComplaintBtn);

        Reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HardcodedImage.this, MainActivity.class);
                intent.putExtra("fragment_to_load", "complaint");
                startActivity(intent);
            }
        });

    }

}