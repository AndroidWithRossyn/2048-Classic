package com.banrossyn.merge2048.newweb;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.banrossyn.merge2048.R;

import java.util.Objects;
public class GamesPlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_games_play);


        WebView webView = findViewById(R.id.webView);
        webView.clearCache(true);
        webView.setWebViewClient(new MyBrowser());
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        WebSettings settingWebView = webView.getSettings();
        settingWebView.setJavaScriptEnabled(true);
        settingWebView.setJavaScriptCanOpenWindowsAutomatically(true);
        settingWebView.setAllowFileAccess(true);
        settingWebView.setDomStorageEnabled(true);
//        settingWebView.setAppCachePath("/data/data/" + getPackageName() + "/cache");
//        settingWebView.setAppCacheEnabled(true);
        settingWebView.setCacheMode(WebSettings.LOAD_DEFAULT);
//        webView.loadUrl(intentUrl);

        webView.loadUrl("file:///android_asset/twozero/index.html");

        webView.addJavascriptInterface(new JavaScriptInterface(this), "Android");


        ImageView imback = findViewById(R.id.backbtn);
        imback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void checkScore(String message) {
        int score = Integer.parseInt(message);
        showGameOverDialog("" + score);


    }


    public void showGameOverDialog(String Message) {
        Dialog customDialog = new Dialog(this);
        customDialog.setContentView(R.layout.gameover);
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.90);
        Objects.requireNonNull(customDialog.getWindow()).setLayout(width, height);
        Objects.requireNonNull(customDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        customDialog.setCancelable(true);
        TextView pd_title = customDialog.findViewById(R.id.game_over_score_num);
        Button okBtn = customDialog.findViewById(R.id.btn_play_again);
        pd_title.setText(Message);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();

            }
        });
        customDialog.show();
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    private class JavaScriptInterface {
        private final Context context;

        JavaScriptInterface(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public void gameOver(String message) {
            checkScore(message);
        }
    }


}