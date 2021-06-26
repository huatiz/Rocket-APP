package com.example.finalterm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import static java.lang.Thread.sleep;

import com.neurosky.thinkgear.TGDevice;
import com.neurosky.thinkgear.TGEegPower;

import java.util.ArrayList;

public class playPage extends AppCompatActivity {
    private SurfaceView surface;
    private long time, timeLeft;
    private int colorId;
    private CountDownTimer timer;

    private Bitmap earth, rocket;
    private Display display;
    private Point point;
    private Paint paint, paint1, paint2, paint3;
    private int dWidth, dHeight;
    private float rocketX, rocketY;
    private float earthY;
    private int velocity=0, gravity = 30;
    private boolean mIsRunning, isOver=false;
    private RectF rect, rect1;

    private static BluetoothAdapter btAdapter;
    private TGDevice tgDevice;
    private String dStatus="";
    private int attention=0;
    private float bottom;
    private ArrayList<Integer> attentionList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_page);

        findView();
        deviceInit();
        getValue();
        setSurface();
        hideSystemNavigationBar();
        keepScreenLight();
    }

    private void findView() {
        surface = (SurfaceView) findViewById(R.id.surface);
    }

    private void getValue() {
        Intent intent = getIntent();
        time = intent.getLongExtra("time", 30) * 1000;
        timeLeft = time;
        String rocketColor = intent.getStringExtra("color");
        switch (rocketColor) {
            case "b":
                colorId = R.drawable.a1_removebg_preview;
                break;
            case "r":
                colorId = R.drawable.a2_removebg_preview;
                break;
            case "y":
                colorId = R.drawable.a3_removebg_preview;
                break;
        }
    }

    private void setTimer() {
        attentionList = new ArrayList<Integer>();
        timer = new CountDownTimer(time+8000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished <= time) {
                    timeLeft = millisUntilFinished;
                    attentionList.add(attention);
                }
            }

            @Override
            public void onFinish() {
                if (isOver == false)
                    gameEnd("WIN THE GAME");
            }
        }.start();
    }

    private void gameEnd(String text) {
        mIsRunning = false;
        Intent intent = new Intent(playPage.this, endPage.class);
        intent.putExtra("text", text);
        intent.putExtra("attention", attentionList);
        startActivity(intent);
        finish();
    }

    private void setSurface() {
        surface.setZOrderOnTop(true);
        SurfaceHolder surHolder = surface.getHolder();
        surHolder.setFormat((PixelFormat.TRANSPARENT));
        initCanvas();

        surHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                mIsRunning = true;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (mIsRunning) {
                            drawCanvas(holder);
                            if (isOver) {
                                try {
                                    sleep(500);
                                    gameEnd("GAME OVER");
                                    timeLeft = 0;
                                } catch (InterruptedException e){
                                    e.printStackTrace();
                                }
                            }
                            else if (timeLeft > 0) {
                                try {
                                    sleep(100);
                                } catch (InterruptedException e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }).start();
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                mIsRunning = false;
            }
        });
    }

    private void initCanvas() {
        earth = BitmapFactory.decodeResource(getResources(), R.drawable.u5);
        display = getWindowManager().getDefaultDisplay();
        point = new Point();
        display.getSize(point);
        dWidth = point.x;
        dHeight = point.y;

        setRocket();
        setTimerText();
        setConnectText();
        setAttentionBar();
    }

    private void setRocket() {
        rocket = BitmapFactory.decodeResource(getResources(), colorId);
        rocket = Bitmap.createScaledBitmap(rocket, 250, 375, true);
        Matrix matrix = new Matrix();
        matrix.postRotate(10);
        rocketX = dWidth/2 - rocket.getWidth()/2;
        rocketY = dHeight/2 - rocket.getHeight()/2;
        earthY = dHeight-earth.getHeight()/2;
    }

    private void setTimerText() {
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.CENTER);
        float textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 50, getResources().getDisplayMetrics());
        paint.setTextSize(textSize);
        Typeface font = Typeface.createFromAsset(getAssets(), "ChakraPetch-Medium.ttf");
        paint.setTypeface(font);
    }

    private void setConnectText() {
        paint1 = new Paint();
        paint1.setColor(Color.WHITE);
        paint1.setTextAlign(Paint.Align.CENTER);
        float textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics());
        paint1.setTextSize(textSize);
        Typeface font = Typeface.createFromAsset(getAssets(), "ChakraPetch-Medium.ttf");
        paint1.setTypeface(font);
    }

    private void setAttentionBar() {
        paint2 = new Paint();
        paint2.setColor(Color.WHITE);
        bottom = dHeight/2+375;
        rect = new RectF(50, dHeight/2-375, 150, bottom);

        paint3 = new Paint();
        paint3.setColor(Color.parseColor("#FFC107"));
    }

    private void drawCanvas(SurfaceHolder holder) {
        if (timeLeft<time-100)
            if (rocketY < dHeight) {
                if (attention <= 50) {
                    velocity = (int)((100-attention)*0.01 * gravity);
                    rocketY += velocity;
                }
                else {
                    velocity =(int) (attention*0.01 * gravity);
                    rocketY -= velocity;
                    if (rocketY < 0)
                        rocketY = 0;
                    earthY += 2;
                }
            }
            else
                isOver = true;

        float barH = (float)(bottom-7.5*attention);
        rect1 = new RectF(50, barH, 150, bottom);

        Canvas canvas = holder.lockCanvas();
        canvas.drawColor(getResources().getColor(R.color.blue));
        canvas.drawBitmap(earth, dWidth-earth.getWidth(), earthY, null);
        canvas.drawBitmap(rocket, rocketX, rocketY, null);
        canvas.drawText(timeLeft/1000+"", dWidth/2, dHeight/4, paint);
        canvas.drawText(attention+"", 100, dHeight/2-395, paint1);
        canvas.drawText(dStatus, dWidth/2, 200, paint1);
        canvas.drawRect(rect, paint2);
        canvas.drawRect(rect1, paint3);
        holder.unlockCanvasAndPost(canvas);
    }

    private void hideSystemNavigationBar() {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            View view = this.getWindow().getDecorView();
            view.setSystemUiVisibility(View.GONE);
        }
        else if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    private void keepScreenLight() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void deviceInit() {
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if(btAdapter == null) {
            Toast.makeText(this, "Bluetooth initialize failed", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        else {
            tgDevice = new TGDevice(btAdapter, handler);
            if(tgDevice == null) {
                Toast.makeText(this, "TGDivice initialize failed", Toast.LENGTH_LONG).show();
                finish();
                return;
            }
            else {
                if(tgDevice.getState() != TGDevice.STATE_CONNECTING) {
                    tgDevice.connect(true);
                }
            }
        }
    }

    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what) {
                case TGDevice.MSG_STATE_CHANGE:
                    switch(msg.arg1) {
                        case TGDevice.STATE_IDLE:
                            break;

                        case TGDevice.STATE_CONNECTING:
                            dStatus = "Connecting...";
                            break;

                        case TGDevice.STATE_CONNECTED:
                            dStatus = "Connected";
                            tgDevice.start();
                            setTimer();
                            break;

                        case TGDevice.STATE_NOT_FOUND:
                            dStatus = "Can't find";
                            reConnect();
                            break;

                        case TGDevice.STATE_NOT_PAIRED:
                            dStatus = "Not paired";
                            break;

                        case TGDevice.STATE_DISCONNECTED:
                            dStatus = "Disconnected";
                            break;
                    }
                    break;

                case TGDevice.MSG_ATTENTION:
                    attention = msg.arg1;
                    break;

                case TGDevice.MSG_LOW_BATTERY:
                    Toast.makeText(getApplicationContext(), "Low battery", Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }
        }
    };

    private void reConnect() {
        if(tgDevice != null) {
            if(tgDevice.getState() != TGDevice.STATE_CONNECTING) {
                tgDevice.connect(true);
            }
        }
    }

}