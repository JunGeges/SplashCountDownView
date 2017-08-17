package com.jensen.splashcountdownview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SplashCountDownView countDownView_1 = (SplashCountDownView) findViewById(R.id.splash_count_view_1);
        SplashCountDownView countDownView_2 = (SplashCountDownView) findViewById(R.id.splash_count_view_2);
        SplashCountDownView countDownView_3 = (SplashCountDownView) findViewById(R.id.splash_count_view_3);
        SplashCountDownView countDownView_4 = (SplashCountDownView) findViewById(R.id.splash_count_view_4);
        SplashCountDownView countDownView_5 = (SplashCountDownView) findViewById(R.id.splash_count_view_5);
        countDownView_1.startCountDown();
        countDownView_2.startCountDown();
        countDownView_3.startCountDown();
        countDownView_4.startCountDown();
        countDownView_5.startCountDown();

        countDownView_1.setOnCountDownCompleteListener(new SplashCountDownView.OnCountDownCompleteListener() {
            @Override
            public void complete() {
                Toast.makeText(MainActivity.this, "complete", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
