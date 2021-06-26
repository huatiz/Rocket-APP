package com.example.finalterm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class setPage extends AppCompatActivity {
    private Button btn_30s, btn_60s, btn_90s, btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_page);

        findView();
        setListener();
        setFont();
        hideSystemNavigationBar();
    }

    private void findView() {
        btn_30s = (Button) findViewById(R.id.btn_30s);
        btn_60s = (Button) findViewById(R.id.btn_60s);
        btn_90s = (Button) findViewById(R.id.btn_90s);
        btn_back = (Button) findViewById(R.id.btn_back1);
    }

    private void setListener() {
        clickButton(btn_30s, 30, outfitPage.class, true);
        clickButton(btn_60s, 60, outfitPage.class, true);
        clickButton(btn_90s, 90, outfitPage.class, true);
        clickButton(btn_back, 0, MainActivity.class, false);
    }

    private void clickButton(Button btn, long time, Class page, Boolean isTime) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(setPage.this, page);
                if (isTime)
                    intent.putExtra("time", time);
                startActivity(intent);
                finish();
            }
        });
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

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getAssets(), "ChakraPetch-Medium.ttf");
        btn_30s.setTypeface(font);
        btn_60s.setTypeface(font);
        btn_90s.setTypeface(font);
    }
}