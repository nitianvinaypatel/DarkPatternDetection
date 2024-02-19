package com.darkpattern.detection;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        ProgressBar progressBar = findViewById(R.id.progressBar);

        Animation bottom_anim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        TextView logo_name = findViewById(R.id.logoName);

        logo_name.setAnimation(bottom_anim);

        progressBar.setVisibility(View.VISIBLE);


        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashScreen.this,Login.class);
            startActivity(intent);
            finish();
        },2500);
    }

}