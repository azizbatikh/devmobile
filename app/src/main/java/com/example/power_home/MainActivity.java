package com.example.power_home;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView logo = findViewById(R.id.logo);
        TextView titre = findViewById(R.id.titre);

// Effet "découpe" : scale et rotation
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(logo, "scaleX", 1f, 1.5f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(logo, "scaleY", 1f, 0.5f, 1f);
        ObjectAnimator rotate = ObjectAnimator.ofFloat(logo, "rotation", 0f, 15f, -15f, 0f);

        scaleX.setDuration(800);
        scaleY.setDuration(800);
        rotate.setDuration(800);

// Apparition progressive du texte
        ObjectAnimator fadeInText = ObjectAnimator.ofFloat(titre, "alpha", 0f, 1f);
        fadeInText.setDuration(1000);
        fadeInText.setStartDelay(900); // attend que l'image finisse

// Lance l'animation
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY, rotate);
        animatorSet.start();
        fadeInText.start();



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }, 5000);
    }
}
