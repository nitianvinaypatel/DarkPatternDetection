package com.darkpattern.detection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.airbnb.lottie.LottieAnimationView;
import com.darkpattern.detection.Fragments.ComplaintFragment;

public class ComplaintResult extends AppCompatActivity {

    private LottieAnimationView lottieAnimationView;
    private Button Track_Complaint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_result);

        lottieAnimationView = findViewById(R.id.lottieAnimationView);
        Track_Complaint = findViewById(R.id.ComplaintBtn1);

        lottieAnimationView.setVisibility(View.VISIBLE);
        Track_Complaint.setVisibility(View.INVISIBLE);



        // Delay visibility change after 3 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                lottieAnimationView.setVisibility(View.GONE);
                Track_Complaint.setVisibility(View.VISIBLE);
            }
        }, 2000); // 3000 milliseconds = 3 seconds


        Track_Complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),TrackinComplaint.class);
                startActivity(intent);
            }
        });

    }
}
