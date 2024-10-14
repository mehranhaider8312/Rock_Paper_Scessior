package com.example.rps;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    static int winsCount = 0;
    static int drawsCount = 0;
    static int lossCount = 0;

    static int userChoice = -1;
    static int gameChoice = -1;

    ImageView ivRock,ivPaper,ivScessior;
    TextView tvUserChoice,tvGameChoice,tvResult,tvWins,tvDraws,tvLosses;
    Button btnRestart,btnClear;
    FloatingActionButton fabProfile;
    Animation scaleUp,scaleDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

        ivRock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivRock.setAnimation(scaleUp);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        userChoice = 1;
                        tvUserChoice.setText(R.string.user_chose_rock);
                        gameChoice = genrateNumber();
                        if(gameChoice == 1){
                            tvGameChoice.setText(R.string.game_chose_rock);
                        }
                        if(gameChoice == 2){
                            tvGameChoice.setText(R.string.game_chose_paper);
                        } else if (gameChoice == 3) {
                            tvGameChoice.setText(R.string.game_chose_scessior);
                        }

                        if(gameChoice == 1){
                            tvResult.setText(R.string.its_a_draw);
                            drawsCount++;
                            tvDraws.setText(" "+drawsCount);
                        }
                        if(gameChoice == 2){
                            tvResult.setText(R.string.loss_result);
                            lossCount++;
                            tvLosses.setText(" "+lossCount);
                        } else if (gameChoice == 3) {
                            tvResult.setText(R.string.win_result);
                            winsCount++;
                            tvWins.setText(" "+winsCount);
                        }
                    }
                },2000);
                ivRock.setAnimation(scaleDown);
            }
        });

        ivPaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userChoice = 2;
                tvUserChoice.setText(R.string.user_chose_paper);
                gameChoice = genrateNumber();
                if(gameChoice == 1){
                    tvGameChoice.setText(R.string.game_chose_rock);
                }else if(gameChoice == 2){
                    tvGameChoice.setText(R.string.game_chose_paper);
                } else if (gameChoice == 3) {
                    tvGameChoice.setText(R.string.game_chose_scessior);
                }

                if(gameChoice == 1){
                    tvResult.setText(R.string.win_result);
                    winsCount++;
                    tvWins.setText(" "+winsCount);
                }else if(gameChoice == 2){
                    tvResult.setText(R.string.its_a_draw);
                    drawsCount++;
                    tvDraws.setText(" "+drawsCount);
                } else if (gameChoice == 3) {
                    tvResult.setText(R.string.loss_result);
                    lossCount++;
                    tvLosses.setText(" "+lossCount);
                }
            }
        });


        ivScessior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userChoice = 3;
                tvUserChoice.setText(R.string.user_chose_scessior);
                gameChoice = genrateNumber();
                if(gameChoice == 1){
                    tvGameChoice.setText(R.string.game_chose_rock);
                }else if(gameChoice == 2){
                    tvGameChoice.setText(R.string.game_chose_paper);
                } else if (gameChoice == 3) {
                    tvGameChoice.setText(R.string.game_chose_scessior);
                }

                if(gameChoice == 1){
                    tvResult.setText(R.string.loss_result);
                    lossCount++;
                    tvLosses.setText(" "+lossCount);
                }else if(gameChoice == 2){
                    tvResult.setText(R.string.win_result);
                    winsCount++;
                    tvWins.setText(" "+winsCount);
                } else if (gameChoice == 3) {
                    tvResult.setText(R.string.its_a_draw);
                    drawsCount++;
                    tvDraws.setText(" "+drawsCount);
                }
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userChoice = 0;
                gameChoice = 0;
                tvGameChoice.setText("New Game");
                tvResult.setText("");
                tvUserChoice.setText("");
            }
        });

        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userChoice = 0;
                gameChoice = 0;
                winsCount = 0;
                lossCount = 0;
                drawsCount = 0;
                tvGameChoice.setText("Game Restarted");
                tvResult.setText("");
                tvUserChoice.setText("");
                tvLosses.setText("0");
                tvWins.setText("0");
                tvDraws.setText("0");
            }
        });
        fabProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DevelporContactScreen.class));
                finish();
            }
        });

    }
    public void init(){

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        scaleUp = AnimationUtils.loadAnimation(this,R.anim.image_scale_up);
        scaleDown = AnimationUtils.loadAnimation(this,R.anim.image_scale_down);
        ivRock = findViewById(R.id.ivRock);
        ivPaper = findViewById(R.id.ivPaper);
        ivScessior = findViewById(R.id.ivSessior);
        tvUserChoice = findViewById(R.id.tvUserChoice);
        tvGameChoice = findViewById(R.id.tvGameChoice);
        tvResult = findViewById(R.id.tvResult);
        tvWins = findViewById(R.id.tvNoOfWins);
        tvDraws = findViewById(R.id.tvNoOfDraws);
        tvLosses = findViewById(R.id.tvNoOfLosses);
        btnClear = findViewById(R.id.btnClear);
        btnRestart = findViewById(R.id.btnRestart);
        fabProfile = findViewById(R.id.fabProfile);
        tvLosses.setText("0");
        tvWins.setText("0");
        tvDraws.setText("0");
    }
    public int genrateNumber(){
        Random rand = new Random();
        return rand.nextInt(3)+1;
    }

}