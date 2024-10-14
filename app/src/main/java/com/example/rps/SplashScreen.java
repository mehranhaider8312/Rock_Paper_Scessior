package com.example.rps;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashScreen extends AppCompatActivity {

    ImageView ivRock,ivPaper,ivScessior;
    TextView tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ivRock = findViewById(R.id.ivRockSS);
        ivPaper = findViewById(R.id.ivPaperSS);
        ivScessior = findViewById(R.id.ivScessiorSS);
        tvName = findViewById(R.id.name);

        Animation name_animation = AnimationUtils.loadAnimation(this,R.anim.name_animation);
        Animation rock_anim = AnimationUtils.loadAnimation(this,R.anim.rock_animation);
        Animation paper_anim = AnimationUtils.loadAnimation(this,R.anim.paper_animation);
        Animation scessior_anim = AnimationUtils.loadAnimation(this,R.anim.scessior_animation);

        ivRock.setAnimation(rock_anim);
        ivPaper.setAnimation(paper_anim);
        ivScessior.setAnimation(scessior_anim);
        tvName.setAnimation(name_animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, 5000);

    }
}