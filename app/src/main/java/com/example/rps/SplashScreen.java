package com.example.rps;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashScreen extends AppCompatActivity {

    ImageView ivRock, ivPaper, ivScissors;
    TextView tvName, tvLoading;
    View mainLayout, impactEffect;

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

        initViews();
        startSplashAnimation();
    }

    private void initViews() {
        ivRock = findViewById(R.id.ivRockSS);
        ivPaper = findViewById(R.id.ivPaperSS);
        ivScissors = findViewById(R.id.ivScessiorSS);
        impactEffect = findViewById(R.id.impactEffect);
        tvName = findViewById(R.id.name);
        tvLoading = findViewById(R.id.tvLoading);
        mainLayout = findViewById(R.id.main);
    }

    private void startSplashAnimation() {
        // Phase 1: App name animation (0-1000ms)
        animateAppName();

        // Phase 2: Icons collision (1000-2500ms)
        new Handler(Looper.getMainLooper()).postDelayed(this::animateCollision, 1000);

        // Phase 3: Rock wins and screen shake (2500-3500ms)
        new Handler(Looper.getMainLooper()).postDelayed(this::animateImpactAndPaper, 2500);

        // Phase 4: Loading text and transition (3500-5000ms)
        new Handler(Looper.getMainLooper()).postDelayed(this::showLoadingAndTransition, 3500);

        // Phase 5: Navigate to main activity (5000ms)
        new Handler(Looper.getMainLooper()).postDelayed(this::navigateToMain, 5000);
    }

    private void animateAppName() {
        // Title slides down and scales up
        ObjectAnimator slideDown = ObjectAnimator.ofFloat(tvName, "translationY", -200f, 0f);
        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(tvName, "scaleX", 0.5f, 1f);
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(tvName, "scaleY", 0.5f, 1f);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(tvName, "alpha", 0f, 1f);

        AnimatorSet titleAnimation = new AnimatorSet();
        titleAnimation.playTogether(slideDown, scaleUpX, scaleUpY, fadeIn);
        titleAnimation.setDuration(1000);
        titleAnimation.setInterpolator(new BounceInterpolator());
        titleAnimation.start();
    }

    private void animateCollision() {
        // Calculate proper distances to center
        // Rock needs to move from left (-100dp margin) to center
        float rockDistance = getResources().getDisplayMetrics().density * 100 + 200f; // margin + distance to center

        // Scissors needs to move from right (-100dp margin) to center
        float scissorsDistance = -(getResources().getDisplayMetrics().density * 100 + 200f); // negative because moving left

        // Rock moves from left to center in straight line
        ObjectAnimator rockMove = ObjectAnimator.ofFloat(ivRock, "translationX", 0f, rockDistance);
        rockMove.setDuration(1500);
        rockMove.setInterpolator(new AccelerateInterpolator());

        // Scissors moves from right to center in straight line
        ObjectAnimator scissorsMove = ObjectAnimator.ofFloat(ivScissors, "translationX", 0f, scissorsDistance);
        scissorsMove.setDuration(1500);
        scissorsMove.setInterpolator(new AccelerateInterpolator());

        // Fade in the icons as they move
        ObjectAnimator rockFade = ObjectAnimator.ofFloat(ivRock, "alpha", 0f, 1f);
        ObjectAnimator scissorsFade = ObjectAnimator.ofFloat(ivScissors, "alpha", 0f, 1f);
        rockFade.setDuration(300);
        scissorsFade.setDuration(300);

        AnimatorSet collisionAnimation = new AnimatorSet();
        collisionAnimation.playTogether(
                rockMove, scissorsMove,
                rockFade, scissorsFade
        );
        collisionAnimation.start();
    }

    private void animateImpactAndPaper() {
        // Impact effect first
        animateImpactEffect();

        // Small delay then screen shake
        new Handler(Looper.getMainLooper()).postDelayed(this::animateScreenShake, 100);

        // Hide colliding icons immediately when impact happens
        ObjectAnimator hideRock = ObjectAnimator.ofFloat(ivRock, "alpha", 1f, 0f);
        ObjectAnimator hideScissors = ObjectAnimator.ofFloat(ivScissors, "alpha", 1f, 0f);
        hideRock.setDuration(100);
        hideScissors.setDuration(100);
        hideRock.start();
        hideScissors.start();

        // Rock emerges as winner slightly after impact
        new Handler(Looper.getMainLooper()).postDelayed(this::animateRockWinner, 200);
    }

    private void animateImpactEffect() {
        // Circular impact explosion - make it more dramatic
        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(impactEffect, "scaleX", 0f, 3f, 1f, 0f);
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(impactEffect, "scaleY", 0f, 3f, 1f, 0f);
        ObjectAnimator fadeInOut = ObjectAnimator.ofFloat(impactEffect, "alpha", 0f, 1f, 0.8f, 0f);

        AnimatorSet impactAnimation = new AnimatorSet();
        impactAnimation.playTogether(scaleUpX, scaleUpY, fadeInOut);
        impactAnimation.setDuration(800);
        impactAnimation.start();
    }

    private void animateScreenShake() {
        // More dramatic earthquake-like screen tremor
        ObjectAnimator shakeX = ObjectAnimator.ofFloat(mainLayout, "translationX",
                0f, -25f, 25f, -20f, 20f, -15f, 15f, -10f, 10f, -5f, 5f, 0f);
        ObjectAnimator shakeY = ObjectAnimator.ofFloat(mainLayout, "translationY",
                0f, -20f, 20f, -15f, 15f, -10f, 10f, -5f, 5f, 0f);

        AnimatorSet earthquakeShake = new AnimatorSet();
        earthquakeShake.playTogether(shakeX, shakeY);
        earthquakeShake.setDuration(600);
        earthquakeShake.start();
    }

    private void animateRockWinner() {
        // Reset rock position and show it as winner
        ivRock.setTranslationX(0f);
        ivRock.setRotation(0f);

        // Rock emerges dramatically as the winner
        ObjectAnimator rockScale = ObjectAnimator.ofFloat(ivRock, "scaleX", 0f, 1.5f, 1.2f);
        ObjectAnimator rockScaleY = ObjectAnimator.ofFloat(ivRock, "scaleY", 0f, 1.5f, 1.2f);
        ObjectAnimator rockFade = ObjectAnimator.ofFloat(ivRock, "alpha", 0f, 1f);
        ObjectAnimator rockRotate = ObjectAnimator.ofFloat(ivRock, "rotation", 0f, 360f);

        AnimatorSet rockWinAnimation = new AnimatorSet();
        rockWinAnimation.playTogether(rockScale, rockScaleY, rockFade, rockRotate);
        rockWinAnimation.setDuration(1000);
        rockWinAnimation.setInterpolator(new OvershootInterpolator(2f));

        rockWinAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                // Start floating effect after emergence
                ObjectAnimator rockFloat = ObjectAnimator.ofFloat(ivRock, "translationY", 0f, -30f, 0f);
                rockFloat.setDuration(1000);
                rockFloat.setRepeatCount(ValueAnimator.INFINITE);
                rockFloat.setRepeatMode(ValueAnimator.REVERSE);
                rockFloat.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {}
            @Override
            public void onAnimationRepeat(Animator animation) {}
        });

        rockWinAnimation.start();
    }

    private void showLoadingAndTransition() {
        // Loading text appears
        ObjectAnimator loadingFade = ObjectAnimator.ofFloat(tvLoading, "alpha", 0f, 1f);
        loadingFade.setDuration(500);
        loadingFade.start();

        // Final title animation - slight glow effect
        ObjectAnimator titlePulse = ObjectAnimator.ofFloat(tvName, "scaleX", 1f, 1.05f, 1f);
        ObjectAnimator titlePulseY = ObjectAnimator.ofFloat(tvName, "scaleY", 1f, 1.05f, 1f);

        AnimatorSet finalAnimation = new AnimatorSet();
        finalAnimation.playTogether(titlePulse, titlePulseY);
        finalAnimation.setDuration(1000);
        finalAnimation.start();
    }

    private void navigateToMain() {
        // Final fade out effect
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(mainLayout, "alpha", 1f, 0f);
        fadeOut.setDuration(500);
        fadeOut.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
                // Add transition animation
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }

            @Override
            public void onAnimationCancel(Animator animation) {}
            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        fadeOut.start();
    }
}