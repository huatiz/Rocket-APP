package com.example.finalterm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class readyPage extends AppCompatActivity {
    private ImageView rocket;
    private TextView text;
    private long time;
    private String rocketColor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready_page);

        findView();
        setTimer();
        getValue();
        setFont();
        setImage();
        hideSystemNavigationBar();
    }

    private void findView() {
        rocket = (ImageView) findViewById(R.id.rocket);
        text = (TextView) findViewById(R.id.countdown);
    }

    private void getValue() {
        Intent intent = getIntent();
        time = intent.getLongExtra("time", 30);
        rocketColor = intent.getStringExtra("color");
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getAssets(), "ChakraPetch-Medium.ttf");
        text.setTypeface(font);
    }

    private void setImage() {
        switch (rocketColor) {
            case "b":
                rocket.setImageResource(R.drawable.a1_removebg_preview);
                break;
            case "r":
                rocket.setImageResource(R.drawable.a2_removebg_preview);
                break;
            case "y":
                rocket.setImageResource(R.drawable.a3_removebg_preview);
                break;
        }

        rocket.getLayoutParams().height = 375;
        rocket.getLayoutParams().width = 250;
        rocket.requestLayout();
    }

    private void setTimer() {
        new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long timeLeft = millisUntilFinished/1000;
                text.setText(timeLeft+"");
                if (timeLeft==0)
                    text.setText("START");
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(readyPage.this, playPage.class);
                intent.putExtra("time", time);
                intent.putExtra("color", rocketColor);
                startActivity(intent);
                finish();
            }
        }.start();
    }

    private void hideSystemNavigationBar() {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            View view = this.getWindow().getDecorView();
            view.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}