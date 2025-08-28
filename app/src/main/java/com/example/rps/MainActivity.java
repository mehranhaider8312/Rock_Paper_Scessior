package com.example.rps;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    static int winsCount = 0;
    static int drawsCount = 0;
    static int lossCount = 0;

    static int userChoice = -1;
    static int gameChoice = -1;

    ImageView ivRock, ivPaper, ivScissor;
    TextView tvUserChoice, tvGameChoice, tvResult, tvWins, tvDraws, tvLosses;
    MaterialButton btnRestart, btnClear;
    FloatingActionButton fabProfile;
    CardView resultCard;

    // For storing the original colors
    int originalWinColor, originalLossColor, originalDrawColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setupClickListeners();
    }

    private void init() {
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ivRock = findViewById(R.id.ivRock);
        ivPaper = findViewById(R.id.ivPaper);
        ivScissor = findViewById(R.id.ivSessior);
        tvUserChoice = findViewById(R.id.tvUserChoice);
        tvGameChoice = findViewById(R.id.tvGameChoice);
        tvResult = findViewById(R.id.tvResult);
        tvWins = findViewById(R.id.tvNoOfWins);
        tvDraws = findViewById(R.id.tvNoOfDraws);
        tvLosses = findViewById(R.id.tvNoOfLosses);
        btnClear = findViewById(R.id.btnClear);
        btnRestart = findViewById(R.id.btnRestart);
        fabProfile = findViewById(R.id.fabProfile);

        // Find the result card for color animations
        resultCard = (CardView) tvResult.getParent();

        // Store original colors
        originalWinColor = Color.parseColor("#4CAF50");
        originalLossColor = Color.parseColor("#F44336");
        originalDrawColor = Color.parseColor("#FF9800");

        // Initialize stats
        updateStats();
    }

    private void setupClickListeners() {
        ivRock.setOnClickListener(v -> handleUserChoice(1, ivRock, "Rock"));
        ivPaper.setOnClickListener(v -> handleUserChoice(2, ivPaper, "Paper"));
        ivScissor.setOnClickListener(v -> handleUserChoice(3, ivScissor, "Scissors"));

        btnClear.setOnClickListener(v -> clearGame());
        btnRestart.setOnClickListener(v -> restartGame());

        fabProfile.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, DevelporContactScreen.class));
            finish();
        });
    }

    private void handleUserChoice(int choice, ImageView selectedView, String choiceName) {
        userChoice = choice;

        // Generate computer choice immediately (simultaneous selection)
        gameChoice = generateNumber();

        // Animate selected choice
        animateChoice(selectedView);

        // Update user choice text immediately
        tvUserChoice.setText("You chose: " + choiceName);
        animateTextChange(tvUserChoice);

        // Show computer choice immediately (no thinking delay)
        String gameChoiceName = getChoiceName(gameChoice);
        tvGameChoice.setText("Computer chose: " + gameChoiceName);
        animateTextChange(tvGameChoice);

        // Small delay just for dramatic reveal of result (not for computer thinking)
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            determineWinner();
        }, 800); // Reduced from 1500ms to 800ms - just for result reveal
    }

    private void animateChoice(ImageView selectedView) {
        // Get the CardView parent of the ImageView
        CardView cardView = (CardView) selectedView.getParent();

        // Scale up animation
        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(selectedView, "scaleX", 1f, 1.3f);
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(selectedView, "scaleY", 1f, 1.3f);

        // Scale down animation
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(selectedView, "scaleX", 1.3f, 1f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(selectedView, "scaleY", 1.3f, 1f);

        // Elevation animation for depth effect
        ObjectAnimator elevation = ObjectAnimator.ofFloat(cardView, "cardElevation", 12f, 20f, 12f);

        AnimatorSet scaleUp = new AnimatorSet();
        scaleUp.playTogether(scaleUpX, scaleUpY, elevation);
        scaleUp.setDuration(200);
        scaleUp.setInterpolator(new AccelerateDecelerateInterpolator());

        AnimatorSet scaleDown = new AnimatorSet();
        scaleDown.playTogether(scaleDownX, scaleDownY);
        scaleDown.setDuration(300);
        scaleDown.setInterpolator(new BounceInterpolator());

        AnimatorSet fullAnimation = new AnimatorSet();
        fullAnimation.playSequentially(scaleUp, scaleDown);
        fullAnimation.start();

        // Add pulse effect
        addPulseEffect(selectedView);
    }

    private void addPulseEffect(View view) {
        ObjectAnimator pulse = ObjectAnimator.ofFloat(view, "alpha", 1f, 0.7f, 1f);
        pulse.setDuration(150);
        pulse.setRepeatCount(2);
        pulse.start();
    }

    private void animateTextChange(TextView textView) {
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(textView, "alpha", 1f, 0f);
        fadeOut.setDuration(150);

        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(textView, "alpha", 0f, 1f);
        fadeIn.setDuration(150);

        AnimatorSet textAnimation = new AnimatorSet();
        textAnimation.playSequentially(fadeOut, fadeIn);
        textAnimation.start();
    }

    private void determineWinner() {
        String result;
        int resultColor;

        if (userChoice == gameChoice) {
            result = "IT'S A DRAW!";
            drawsCount++;
            resultColor = originalDrawColor;
            animateStatChange(tvDraws);
        } else if ((userChoice == 1 && gameChoice == 3) ||
                (userChoice == 2 && gameChoice == 1) ||
                (userChoice == 3 && gameChoice == 2)) {
            result = "YOU WIN! 🎉";
            winsCount++;
            resultColor = originalWinColor;
            animateStatChange(tvWins);
            animateWinCelebration();
        } else {
            result = "YOU LOSE! 😔";
            lossCount++;
            resultColor = originalLossColor;
            animateStatChange(tvLosses);
            animateLossShake();
        }

        // Animate result appearance
        animateResultText(result, resultColor);
        updateStats();
    }

    private void animateResultText(String result, int color) {
        // Scale and color animation for result
        tvResult.setText(result);

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(tvResult, "scaleX", 0.5f, 1.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(tvResult, "scaleY", 0.5f, 1.2f, 1f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(tvResult, "alpha", 0f, 1f);

        // Color animation for the card background
        ValueAnimator colorAnimator = ValueAnimator.ofArgb(Color.parseColor("#4CAF50"), color);
        colorAnimator.addUpdateListener(animation -> {
            resultCard.setCardBackgroundColor((int) animation.getAnimatedValue());
        });

        AnimatorSet resultAnimation = new AnimatorSet();
        resultAnimation.playTogether(scaleX, scaleY, alpha, colorAnimator);
        resultAnimation.setDuration(600);
        resultAnimation.setInterpolator(new BounceInterpolator());
        resultAnimation.start();
    }

    private void animateWinCelebration() {
        // Bounce animation for the entire result card
        ObjectAnimator bounce = ObjectAnimator.ofFloat(resultCard, "translationY", 0f, -30f, 0f);
        bounce.setDuration(500);
        bounce.setInterpolator(new BounceInterpolator());
        bounce.start();
    }

    private void animateLossShake() {
        // Shake animation for loss
        ObjectAnimator shake = ObjectAnimator.ofFloat(resultCard, "translationX", 0f, -25f, 25f, -25f, 25f, 0f);
        shake.setDuration(500);
        shake.start();
    }

    private void animateStatChange(TextView statView) {
        // Pulse animation for changed stat - animate the CardView container
        View cardView = (View) statView.getParent().getParent();

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(cardView, "scaleX", 1f, 1.1f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(cardView, "scaleY", 1f, 1.1f, 1f);

        AnimatorSet pulseAnimation = new AnimatorSet();
        pulseAnimation.playTogether(scaleX, scaleY);
        pulseAnimation.setDuration(300);
        pulseAnimation.setInterpolator(new DecelerateInterpolator());
        pulseAnimation.start();
    }

    private void clearGame() {
        // Animate clearing
        AnimatorSet clearAnimation = new AnimatorSet();

        ObjectAnimator fadeOutUser = ObjectAnimator.ofFloat(tvUserChoice, "alpha", 1f, 0f);
        ObjectAnimator fadeOutGame = ObjectAnimator.ofFloat(tvGameChoice, "alpha", 1f, 0f);
        ObjectAnimator fadeOutResult = ObjectAnimator.ofFloat(tvResult, "alpha", 1f, 0f);

        clearAnimation.playTogether(fadeOutUser, fadeOutGame, fadeOutResult);
        clearAnimation.setDuration(300);
        clearAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                userChoice = 0;
                gameChoice = 0;
                tvUserChoice.setText("Make your choice!");
                tvGameChoice.setText("Computer is ready...");
                tvResult.setText("Ready to Play?");
                resultCard.setCardBackgroundColor(originalWinColor);

                // Fade back in
                ObjectAnimator fadeInUser = ObjectAnimator.ofFloat(tvUserChoice, "alpha", 0f, 1f);
                ObjectAnimator fadeInGame = ObjectAnimator.ofFloat(tvGameChoice, "alpha", 0f, 1f);
                ObjectAnimator fadeInResult = ObjectAnimator.ofFloat(tvResult, "alpha", 0f, 1f);

                AnimatorSet fadeInAnimation = new AnimatorSet();
                fadeInAnimation.playTogether(fadeInUser, fadeInGame, fadeInResult);
                fadeInAnimation.setDuration(300);
                fadeInAnimation.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {}
            @Override
            public void onAnimationRepeat(Animator animation) {}
        });

        clearAnimation.start();
    }

    private void restartGame() {
        // Reset all values
        userChoice = 0;
        gameChoice = 0;
        winsCount = 0;
        lossCount = 0;
        drawsCount = 0;

        // Animate restart
        ObjectAnimator spinButton = ObjectAnimator.ofFloat(btnRestart, "rotation", 0f, 360f);
        spinButton.setDuration(500);
        spinButton.start();

        // Reset UI
        tvUserChoice.setText("Make your choice!");
        tvGameChoice.setText("Computer is ready...");
        tvResult.setText("Game Restarted!");
        resultCard.setCardBackgroundColor(originalWinColor);

        // Animate stats reset
        animateStatsReset();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            tvResult.setText("Ready to Play?");
            updateStats();
        }, 1000);
    }

    private void animateStatsReset() {
        // Get the CardViews containing the stats
        View winsCard = (View) tvWins.getParent().getParent();
        View drawsCard = (View) tvDraws.getParent().getParent();
        View lossesCard = (View) tvLosses.getParent().getParent();

        ObjectAnimator resetWins = ObjectAnimator.ofFloat(winsCard, "scaleY", 1f, 0f, 1f);
        ObjectAnimator resetDraws = ObjectAnimator.ofFloat(drawsCard, "scaleY", 1f, 0f, 1f);
        ObjectAnimator resetLosses = ObjectAnimator.ofFloat(lossesCard, "scaleY", 1f, 0f, 1f);

        AnimatorSet resetAnimation = new AnimatorSet();
        resetAnimation.playTogether(resetWins, resetDraws, resetLosses);
        resetAnimation.setDuration(400);
        resetAnimation.start();
    }

    private void updateStats() {
        tvWins.setText(String.valueOf(winsCount));
        tvDraws.setText(String.valueOf(drawsCount));
        tvLosses.setText(String.valueOf(lossCount));
    }

    private String getChoiceName(int choice) {
        switch (choice) {
            case 1: return "Rock";
            case 2: return "Paper";
            case 3: return "Scissors";
            default: return "Unknown";
        }
    }

    private int generateNumber() {
        Random rand = new Random();
        return rand.nextInt(3) + 1;
    }
}