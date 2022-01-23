package com.example.clock;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    TextView title;
    ImageView alarmBtn,worldClockBtn,timerBtn,stopWatchBtn;
    BottomNavigationView bottomNavigationView;
    public static boolean testForActivity=false;
    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        title = findViewById(R.id.main_toolbar_title);
//        alarmBtn=findViewById(R.id.alarm_btn);
//        worldClockBtn=findViewById(R.id.world_clock_btn);
//        timerBtn=findViewById(R.id.timer_watch_btn);
//        stopWatchBtn=findViewById(R.id.stop_watch_btn);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_alarm) {
                    loadFragment(new AlarmFragment());
                    title.setText("Alarm");
                } else if (item.getItemId() == R.id.nav_world_clock) {
                    loadFragment(new WorldClockFragment());
                    title.setText("World clock");
                } else if (item.getItemId() == R.id.nav_stopwatch) {
                    loadFragment(new StopWatchFragment());
                    title.setText("Stopwatch");
                } else if (item.getItemId() == R.id.nav_timer) {
                    loadFragment(new ClockTimerFragment());
                    title.setText("Timer");
                }
                return true;
            }
        });

        loadFragment(new AlarmFragment());
//        changeBtnColor(new ImageView[]{alarmBtn,worldClockBtn,stopWatchBtn,timerBtn});
//
//        alarmBtn.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("ResourceType")
//            @Override
//            public void onClick(View view) {
//                AlarmFragment f=new AlarmFragment();
//                loadFragment(f);
//                title.setText("Alarm");
//                changeBtnColor(new ImageView[]{alarmBtn,worldClockBtn,stopWatchBtn,timerBtn});
//            }
//        });
//
//        worldClockBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                title.setText("World clock");
//                changeBtnColor(new ImageView[]{worldClockBtn,alarmBtn,stopWatchBtn,timerBtn});
//            }
//        });
//
//        stopWatchBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                StopWatchFragment f=new StopWatchFragment();
//                loadFragment(f);
//                title.setText("Stopwatch");
//                changeBtnColor(new ImageView[]{stopWatchBtn,alarmBtn,worldClockBtn,timerBtn});
//            }
//        });
//
//        timerBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                ClockTimerFragment f=new ClockTimerFragment();
//                loadFragment(new ClockTimerFragment());
//                title.setText("Timer");
//                changeBtnColor(new ImageView[]{timerBtn,alarmBtn,worldClockBtn,stopWatchBtn});
//            }
//        });
    }
    public void loadFragment(Fragment f){
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.ss,f);
        ft.commit();
    }
//    @SuppressLint("ResourceType")
//    public void changeBtnColor(ImageView[] imgBtn){
//        DrawableCompat.setTint(DrawableCompat.wrap(imgBtn[0].getDrawable()), ContextCompat.getColor(MainActivity.this,R.color.blue));
//        DrawableCompat.setTint(DrawableCompat.wrap(imgBtn[1].getDrawable()), ContextCompat.getColor(MainActivity.this,R.color.gray));
//        DrawableCompat.setTint(DrawableCompat.wrap(imgBtn[2].getDrawable()), ContextCompat.getColor(MainActivity.this,R.color.gray ));
//        DrawableCompat.setTint(DrawableCompat.wrap(imgBtn[3].getDrawable()), ContextCompat.getColor(MainActivity.this,R.color.gray));
//    }
}


