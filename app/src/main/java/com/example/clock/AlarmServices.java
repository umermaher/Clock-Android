package com.example.clock;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import static com.example.clock.AlarmReceiver.ALARM_NOTIFICATION_ID;
import static com.example.clock.AlarmReceiver.alarmNotificationManagerCompat;
import static com.example.clock.AlarmRingtoneDialog.ringtones;

public class AlarmServices extends Service {
    MediaPlayer player=null;
    int[] soundRecources={R.raw.clock_alarm,R.raw.alarm_rooster_hug,R.raw.snoop_dogg,R.raw.bugle_call,R.raw.still_dre,R.raw.funny_alarm,R.raw.beep_alarm};

    long durationInMillis;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int duration = intent.getIntExtra("duration", 1);
        String sound = intent.getStringExtra("sound");
        durationInMillis = (long) duration * 60000;
        durationInMillis += System.currentTimeMillis();
        long finalDurationInMillis = durationInMillis;

        for (int i = 0; i < ringtones.length; i++) {
            if (ringtones[i].equals(sound))
                player = MediaPlayer.create(getApplicationContext(), soundRecources[i]);
        }

        if (player != null)
            player.start();
        assert player != null;
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (finalDurationInMillis > System.currentTimeMillis())
                    player.start();
                else {
                    alarmNotificationManagerCompat.cancel(ALARM_NOTIFICATION_ID);
                    stopSelf();
                    onDestroy();
                }
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy() {
        stopSelf();
        if(player==null)
            player=MediaPlayer.create(getApplicationContext(),soundRecources[0]);
        player.stop();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
