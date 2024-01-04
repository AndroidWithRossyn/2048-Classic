package com.banrossyn.merge2048.org;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.banrossyn.merge2048.HomeActivity;
import com.banrossyn.merge2048.R;
import com.banrossyn.merge2048.org.GameCode.ThreadMain;

import com.banrossyn.merge2048.org.Tiles.BitmapCreator;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
//import com.google.android.gms.auth.api.signin.GoogleSignIn;

import java.util.ArrayList;
import java.util.Objects;


public class GameActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private TextView scoreTextView, topScoreTextView;

//    private SharedPreferences sp;
    private int boardRows, boardCols, boardExponent, gameMode;
    private boolean isTutorialFromMainScreen;
//    private InterstitialAd interstitialAd;
//    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        boardRows = getIntent().getIntExtra("rows", 4);
        boardCols = getIntent().getIntExtra("cols", 4);
        boardExponent = getIntent().getIntExtra("exponent", 2);
        gameMode = getIntent().getIntExtra("game_mode", 0);
        isTutorialFromMainScreen = getIntent().getBooleanExtra("tutorial", false);
        setContentView(R.layout.game);

     
hideSystemUI();
        changeLayout();

//        bannerAd();

//        interstitialAd();

        Animation btnScaleAnim = AnimationUtils.loadAnimation(GameActivity.this, R.anim.scale_animation);

        ImageView homeBtn = findViewById(R.id.ib_home);
        homeBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//        ImageButton btnSetting = findViewById(R.id.btn_settings);
//        btnSetting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//            }
//        });

        ImageButton btnScoreBoard = findViewById(R.id.btn_Score_board);
        btnScoreBoard.startAnimation(btnScaleAnim);
        btnScoreBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                scoreBoardDialog();
            }
        });


    }

    public static Context getContext() {
        return context;
    }

    public int getBoardRows() {
        return boardRows;
    }

    public int getBoardCols() {
        return boardCols;
    }

    public int getBoardExponent() {
        return boardExponent;
    }

    public int getGameMode() {
        return gameMode;
    }

    public boolean isTutorial() {
        SharedPreferences sharedPreferences = getSharedPreferences("play_history", MODE_PRIVATE);

        if (!sharedPreferences.getBoolean("tutorial_played", false) || isTutorialFromMainScreen) {
            sharedPreferences.edit().putBoolean("tutorial_played", true).apply();
            return true;
        }
        return false;
    }

    public boolean isTutorialFromMainScreen() {
        return isTutorialFromMainScreen;
    }



    private void changeLayout() {

        LinearLayout layout = findViewById(R.id.game_liner_layout);
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        params.width = params.height = Resources.getSystem().getDisplayMetrics().widthPixels;
        double difference = (double) boardCols / boardRows;

        if (boardRows == 3 && boardCols == 3) {
            params.width = params.height = (int) (params.width * 0.8);
        }
        if (boardRows == 4 && boardCols == 4) {
            params.width = params.height = (int) (params.width * 0.85);
        }
        if (boardRows != boardCols) {
            params.width = params.height = (int) (params.width * 1.1);
            params.width = (int) (params.width * difference);
        }
        layout.setLayoutParams(params);
    }

    public void updateScore(final long score, final long topScore) {
        this.scoreTextView = findViewById(R.id.current_score_textview);
        this.topScoreTextView = findViewById(R.id.best_score_textview);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (score == 0) {
                    scoreTextView.setText(getString(R.string.start));
                } else {
                    scoreTextView.setText(String.valueOf(score));
                }
                topScoreTextView.setText(String.valueOf(topScore));
            }
        });

    }



    private void scoreBoardDialog() {
        SharedPreferences sharedPreferences = getSharedPreferences("2048", MODE_PRIVATE);
        final Dialog dialog = new Dialog(GameActivity.this);
        dialog.setContentView(R.layout.scoreboard);

        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.90);
        Objects.requireNonNull(dialog.getWindow()).setLayout(width, height);


        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final ListView listView = dialog.findViewById(R.id.listview_score_board);
        ArrayList<ScoreBoardBuilder.Score> classicScores = ScoreBoardBuilder.createClassicArrayList(sharedPreferences, "0");
        ArrayList<ScoreBoardBuilder.Score> blocksScores = ScoreBoardBuilder.createClassicArrayList(sharedPreferences, "1");
        ArrayList<ScoreBoardBuilder.Score> shuffleScores = ScoreBoardBuilder.createClassicArrayList(sharedPreferences, "2");
        final ScoreBoardBuilder.ScoreAdapter scoreAdapterClassic = new ScoreBoardBuilder.ScoreAdapter(classicScores, this);
        final ScoreBoardBuilder.ScoreAdapter scoreAdapterBlocks = new ScoreBoardBuilder.ScoreAdapter(blocksScores, this);
        final ScoreBoardBuilder.ScoreAdapter scoreAdapterShuffle = new ScoreBoardBuilder.ScoreAdapter(shuffleScores, this);
        listView.setAdapter(scoreAdapterClassic);

        final Animation rightInAnim = AnimationUtils.loadAnimation(GameActivity.this, R.anim.slide_in_right_side);
        final Animation leftInAnim = AnimationUtils.loadAnimation(GameActivity.this, R.anim.slide_in_left_side);
        final Animation rightOutAnim = AnimationUtils.loadAnimation(GameActivity.this, R.anim.slide_out_right_side);
        final Animation leftOutAnim = AnimationUtils.loadAnimation(GameActivity.this, R.anim.slide_out_left_side);

        Animation anim = AnimationUtils.loadAnimation(GameActivity.this, R.anim.scale_animation);
        final TextView currentModeTv = dialog.findViewById(R.id.textview_mode_type);
        final int[] index = {0};

        ImageButton btnRight = dialog.findViewById(R.id.right_btn);
        btnRight.startAnimation(anim);
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index[0] == 2) {
                    index[0] = 0;
                } else {
                    index[0]++;
                }
                leftOutAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        switch (index[0]) {
                            case 0:
                             
                                listView.setAdapter(scoreAdapterClassic);
                                currentModeTv.setText(getString(R.string.mode_classic));
                                break;
                            case 1:
                           
                                listView.setAdapter(scoreAdapterBlocks);
                                currentModeTv.setText(getString(R.string.mode_blocks));
                                break;
                            case 2:
                               
                                listView.setAdapter(scoreAdapterShuffle);
                                currentModeTv.setText(getString(R.string.mode_shuffle));
                                break;
                        }
                        listView.startAnimation(rightInAnim);
                    }
                });
                listView.startAnimation(leftOutAnim);


            }
        });

        ImageButton btnLeft = dialog.findViewById(R.id.left_btn);
        btnLeft.startAnimation(anim);
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index[0] == 0) {
                    index[0] = 2;
                } else {
                    index[0]--;
                }

                rightOutAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        switch (index[0]) {
                            case 0:
                              
                                listView.setAdapter(scoreAdapterClassic);
                                currentModeTv.setText(getString(R.string.mode_classic));
                                break;
                            case 1:
                            
                                listView.setAdapter(scoreAdapterBlocks);
                                currentModeTv.setText(getString(R.string.mode_blocks));
                                break;
                            case 2:
                             
                                listView.setAdapter(scoreAdapterShuffle);
                                currentModeTv.setText(getString(R.string.mode_shuffle));
                                break;
                        }
                        listView.startAnimation(leftInAnim);
                    }
                });
                listView.startAnimation(rightOutAnim);


            }
        });
        Animation btnScaleAnim = AnimationUtils.loadAnimation(GameActivity.this, R.anim.scale_animation);
        Button btnClose = dialog.findViewById(R.id.close_button);
        btnClose.setAnimation(btnScaleAnim);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             
                dialog.dismiss();
                hideSystemUI();

            }
        });
        dialog.show();

    }

    private ThreadMain thread;

    public void setThread(ThreadMain thread) {
        this.thread = thread;
    }

    public void destroyGameThread() {
        super.onBackPressed();
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
                retry = false;
                BitmapCreator bitmapCreator = new BitmapCreator();
                bitmapCreator.clearBitmapArray();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    

    @Override
    public void onBackPressed() {


//        if (interstitialAd != null) {
//            interstitialAd.show(this);
//
//            interstitialAd.setFullScreenContentCallback(
//                    new FullScreenContentCallback() {
//                        @Override
//                        public void onAdDismissedFullScreenContent() {
//                            GameActivity.this.interstitialAd = null;
//                            Intent intent = new Intent(GameActivity.this, HomeActivity.class);
//                            startActivity(intent);
//                            destroyGameThread();
//
//                        }
//
//                        @Override
//                        public void onAdFailedToShowFullScreenContent(AdError adError) {
//                            GameActivity.this.interstitialAd = null;
//                        }
//
//                        @Override
//                        public void onAdShowedFullScreenContent() {
//                        }
//                    });
//        } else {
            Intent intent = new Intent(GameActivity.this, HomeActivity.class);
            startActivity(intent);
            destroyGameThread();
            super.onBackPressed();
//        }
    }

    @Override
    protected void onResume() {
        hideSystemUI();
        super.onResume();
//        if (adView != null) {
//            adView.resume();
//        }
//        if (interstitialAd != null) {
//            interstitialAd.show(this);
//
//            interstitialAd.setFullScreenContentCallback(
//                    new FullScreenContentCallback() {
//                        @Override
//                        public void onAdDismissedFullScreenContent() {
//                            GameActivity.this.interstitialAd = null;
//
//                        }
//
//                        @Override
//                        public void onAdFailedToShowFullScreenContent(AdError adError) {
//                            GameActivity.this.interstitialAd = null;
//                        }
//
//                        @Override
//                        public void onAdShowedFullScreenContent() {
//                        }
//                    });
//        }
      

    }

    @Override
    protected void onPause() {
        super.onPause();
//        if (adView != null) {
//            adView.pause();
//        }
    

     
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (adView != null) {
//            adView.destroy();
//        }


    }

//
//    public final void bannerAd() {
//        adView = findViewById(R.id.ad_view_game);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        adView.loadAd(adRequest);
//
//        adView.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                super.onAdLoaded();
//            }
//
//            @Override
//            public void onAdFailedToLoad(LoadAdError adError) {
//                super.onAdFailedToLoad(adError);
//                adView.loadAd(adRequest);
//            }
//
//            @Override
//            public void onAdClosed() {
//                adView.loadAd(adRequest);
//                super.onAdClosed();
//            }
//        });
//    }

//    public void interstitialAd() {
//        AdRequest adRequest = new AdRequest.Builder().build();
//        InterstitialAd.load(
//                this,
//                getString(R.string.InterstitialAd),
//                adRequest,
//                new InterstitialAdLoadCallback() {
//                    @Override
//                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
//                        GameActivity.this.interstitialAd = interstitialAd;
//                    }
//
//                    @Override
//                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                        interstitialAd = null;
//
//                    }
//                });
//    }
    private void hideSystemUI() {
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        View mContentView = getWindow().getDecorView();

        if (Build.VERSION.SDK_INT >= 30) {
            mContentView.getWindowInsetsController().hide(
                    android.view.WindowInsets.Type.statusBars() | android.view.WindowInsets.Type.navigationBars());
        }
        else {

            mContentView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LOW_PROFILE
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            );
        }



    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        hideSystemUI();
        super.onWindowFocusChanged(hasFocus);
    }
}








