package com.banrossyn.merge2048;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.banrossyn.merge2048.Bote.MainActivity;
import com.banrossyn.merge2048.newweb.GamesPlayActivity;
import com.banrossyn.merge2048.org.ChooseBoardActivity;
import com.banrossyn.merge2048.org.GameActivity;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class HomeActivity extends AppCompatActivity {

    private SharedPreferences sp;

//    private InterstitialAd interstitialAdHome;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);


        hideSystemUI();
//        interstitialAd();
        Animation btnScaleAnim = AnimationUtils.loadAnimation(HomeActivity.this, R.anim.scale_animation);


        Button btnSimpleGame = findViewById(R.id.btn_Simple);
        btnSimpleGame.startAnimation(btnScaleAnim);
        btnSimpleGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(HomeActivity.this, GamesPlayActivity.class));
            }
        });
        Button btnClassicGame = findViewById(R.id.btn_Classic);
        btnClassicGame.startAnimation(btnScaleAnim);
        btnClassicGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, MainActivity.class));

            }
        });

        Button btnAdvanceGame = findViewById(R.id.btn_Advance);
        btnAdvanceGame.startAnimation(btnScaleAnim);
        btnAdvanceGame.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeActivity.this, ChooseBoardActivity.class);
                startActivity(intent);
            }
        });

        Button btnHowtoPlay = findViewById(R.id.btn_how_to_play);
        btnHowtoPlay.startAnimation(btnScaleAnim);
        btnHowtoPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeActivity.this, GameActivity.class);
                intent.putExtra("tutorial", true);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right_side, R.anim.slide_out_left_side);
            }
        });


        Button btnAbout = findViewById(R.id.btn_About);
        btnAbout.startAnimation(btnScaleAnim);
        btnAbout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });
        Button btnmoregames = findViewById(R.id.btn_moregames);
        btnmoregames.startAnimation(btnScaleAnim);
        btnmoregames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gotourl(getResources().getString(R.string.moregamesurl));
            }
        });
    }


    private void gotourl(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    @Override
    public void onBackPressed() {
//        if (interstitialAdHome != null) {
//            interstitialAdHome.show(this);
//            interstitialAdHome.setFullScreenContentCallback(
//                    new FullScreenContentCallback() {
//                        @Override
//                        public void onAdDismissedFullScreenContent() {
//                            HomeActivity.this.interstitialAdHome = null;
//                            finish();
//                        }
//
//                        @Override
//                        public void onAdFailedToShowFullScreenContent(AdError adError) {
//                            HomeActivity.this.interstitialAdHome = null;
//                        }
//
//                        @Override
//                        public void onAdShowedFullScreenContent() {
//                        }
//                    });
//        } else {
            super.onBackPressed();
//        }

    }


//    public void interstitialAd() {
//        AdRequest adRequest = new AdRequest.Builder().build();
//        InterstitialAd.load(
//                this,
//                getString(R.string.InterstitialAd),
//                adRequest,
//                new InterstitialAdLoadCallback() {
//                    @Override
//                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
//                        HomeActivity.this.interstitialAdHome = interstitialAd;
//                        interstitialAd.setFullScreenContentCallback(
//                                new FullScreenContentCallback() {
//                                    @Override
//                                    public void onAdDismissedFullScreenContent() {
//                                        HomeActivity.this.interstitialAdHome = null;
//                                    }
//
//                                    @Override
//                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
//                                        HomeActivity.this.interstitialAdHome = null;
//                                    }
//
//                                    @Override
//                                    public void onAdShowedFullScreenContent() {
//                                    }
//                                });
//                    }
//
//                    @Override
//                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                        interstitialAdHome = null;
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
        } else {

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
