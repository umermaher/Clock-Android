package com.example.clock;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.graphics.drawable.IconCompat;
import androidx.fragment.app.Fragment;

import java.util.Locale;
import java.util.Objects;

public class ClockTimerFragment extends Fragment{
    TextView txtTimer;
    ImageView toggle,reset;
    NumberPicker secPicker,minPicker;
    int sec=0,min=0;
    CountDownTimer countDownTimer;
    boolean isTimerRunning;
    long timeLeftInMillis=0,endTime;
    public static NotificationManagerCompat managerCompat;
    public static MediaPlayer timerBeep;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.timer_fragment, container, false);
        txtTimer=v.findViewById(R.id.timer);
        toggle=v.findViewById(R.id.toggle_timer_btn);
        reset=v.findViewById(R.id.timer_reset_btn);
        secPicker=v.findViewById(R.id.sec_num);
        minPicker=v.findViewById(R.id.min_num);

        DrawableCompat.setTint(DrawableCompat.wrap(reset.getDrawable()), ContextCompat.getColor(requireActivity(),R.color.gray));
        reset.setEnabled(false);
        //creating notification for time completion
        managerCompat =NotificationManagerCompat.from(requireActivity());


        secPicker.setMaxValue(60);
        secPicker.setMinValue(0);
        minPicker.setMaxValue(60);
        minPicker.setMinValue(0);

        if(txtTimer.getText().toString().equals("00:00"))
            toggle.setImageResource(R.drawable.start);
        secPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                sec=i1;
                txtTimer.setText(String.format(Locale.getDefault(),"%02d:%02d",min,sec));
                timeLeftInMillis=(min*60000)+(sec*1000);
            }
        });
        minPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                min=i1;
                timeLeftInMillis=(min*60000)+(sec*1000);
                txtTimer.setText(String.format(Locale.getDefault(),"%02d:%02d",min,sec));
            }
        });
        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endTime=System.currentTimeMillis()+timeLeftInMillis;
                if(isTimerRunning){
                    pauseTimer();
                    toggle.setImageResource(R.drawable.start);
                }
                else if(!txtTimer.getText().toString().equals("00:00")){
                        startTimer();
                        secPicker.setVisibility(View.INVISIBLE);
                        minPicker.setVisibility(View.INVISIBLE);
                    secPicker.setValue(0);
                    minPicker.setValue(0);
                    reset.setEnabled(true);
                    DrawableCompat.setTint(DrawableCompat.wrap(reset.getDrawable()), ContextCompat.getColor(requireActivity(),R.color.darkGray));
                }
            }
        });
        
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!txtTimer.getText().toString().equals("00:00")) {
                    resetTimer();
                    secPicker.setVisibility(View.VISIBLE);
                    minPicker.setVisibility(View.VISIBLE);
                    DrawableCompat.setTint(DrawableCompat.wrap(reset.getDrawable()), ContextCompat.getColor(requireActivity(),R.color.gray));
                    reset.setEnabled(false);
                }
            }
        });
        return v;
    }

    private void startTimer() {
        toggle.setImageResource(R.drawable.pause_timer);
        countDownTimer =new CountDownTimer(timeLeftInMillis,1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMillis=l;
                updateTxtTimer();
            }

            @RequiresApi(api = Build.VERSION_CODES.P)
            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish() {
                isTimerRunning=false;
                txtTimer.setText("00:00");
                toggle.setImageResource(R.drawable.start);
                secPicker.setVisibility(View.VISIBLE);
                minPicker.setVisibility(View.VISIBLE);
                DrawableCompat.setTint(DrawableCompat.wrap(reset.getDrawable()), ContextCompat.getColor(requireActivity(),R.color.gray));
                reset.setEnabled(false);

                Intent intent = new Intent(getActivity(), timerNotificationReceiver.class);
                intent.putExtra("id",1);
                PendingIntent pendingIntent= PendingIntent.getBroadcast(getActivity(), 500, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                timerBeep=MediaPlayer.create(getActivity(),R.raw.beep_alarm);
                timerBeep.start();
                timerBeep.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                            timerBeep.start();

                    }
                });

                Notification notification= new NotificationCompat.Builder(requireActivity(),App.CHANNEL_ID)
                        .setSmallIcon(R.drawable.timer)
                        .setContentTitle("Timer elapsed")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setSilent(true)
                        .setCategory(NotificationCompat.CATEGORY_ALARM)
                        .addAction(R.drawable.timer,"Stop", pendingIntent)
                        .setColor(Color.BLUE)
                        .setOngoing(true)
                        .setAutoCancel(true)
                        .build();
                managerCompat.notify(1,notification);

            }
        }.start();
        isTimerRunning=true;
        reset.setEnabled(true);
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        isTimerRunning=false;
    }

    private void resetTimer() {
        timeLeftInMillis=0;
        endTime=0;
        updateTxtTimer();
        isTimerRunning=false;
        toggle.setImageResource(R.drawable.start);
        reset.setEnabled(false);
        onStop();
    }

    private void updateTxtTimer() {
        min=(int)(timeLeftInMillis/1000)/60;
        sec=(int)(timeLeftInMillis/1000)%60;
        String timeLeftFormatted=String.format(Locale.getDefault(),"%02d:%02d",min,sec);
        txtTimer.setText(timeLeftFormatted);
    }

    private void updateButtons(){
        if(isTimerRunning)
            toggle.setImageResource(R.drawable.pause_timer);
        else{
            toggle.setImageResource(R.drawable.start);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences sp= getActivity().getSharedPreferences("time", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putLong("timeLeftInMillis",timeLeftInMillis);
        editor.putBoolean("isTimerRunning",isTimerRunning);
        editor.putLong("endTime",endTime);

        editor.apply();
        if(countDownTimer!=null)
            countDownTimer.cancel();
    }

    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences sp= requireActivity().getSharedPreferences("time", Context.MODE_PRIVATE);
        timeLeftInMillis=sp.getLong("timeLeftInMillis",timeLeftInMillis);
        isTimerRunning=sp.getBoolean("isTimerRunning",isTimerRunning);

        updateTxtTimer();
        updateButtons();

        if(!txtTimer.getText().toString().equals("00:00")){
            if(isTimerRunning){
                endTime=sp.getLong("endTime",endTime);
                timeLeftInMillis=endTime-System.currentTimeMillis();
//            startTimer();
                if(timeLeftInMillis<1000){
                    timeLeftInMillis=0;
                    isTimerRunning=false;
                    updateButtons();
                    updateTxtTimer();
                }else
                    startTimer();
            }
        }
    }
}
