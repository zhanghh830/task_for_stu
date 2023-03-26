package com.ear.task_for_stu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.ear.task_for_stu.R;
import com.ear.task_for_stu.ui.activity.user.LoginActivity;

public class Splash extends AppCompatActivity {
    private Button BtnSkip;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            tologin();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        initEvent();
        handler.postDelayed(runnable,2000);
    }
    private void initEvent() {
        BtnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tologin();
                handler.removeCallbacks(runnable);
            }
        });
    }

    private void initView() {
        BtnSkip = findViewById(R.id.id_skip);
    }

    public void tologin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}