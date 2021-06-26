package com.example.finalterm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class outfitPage extends AppCompatActivity {
    private ImageButton image_b, image_r, image_y;
    private Button btn_back;
    private long time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outfit_page);
        findView();
        getTime();
        setListener();
        hideSystemNavigationBar();
    }

    private void findView() {
        image_b = (ImageButton) findViewById(R.id.image_b);
        image_r = (ImageButton) findViewById(R.id.image_r);
        image_y = (ImageButton) findViewById(R.id.image_y);
        btn_back = (Button) findViewById(R.id.btn_back2);
    }

    private void getTime() {
        Intent intent = getIntent();
        time = intent.getLongExtra("time", 30);
    }

    private void setListener() {
        clickButton(image_b, "b");
        clickButton(image_r, "r");
        clickButton(image_y, "y");

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(outfitPage.this, setPage.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void clickButton(ImageButton btn, String color) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(outfitPage.this, readyPage.class);
                intent.putExtra("time", time);
                intent.putExtra("color", color);
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
}