package com.example.clock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

import java.time.Clock;

import static com.example.clock.ClockTimerFragment.timerBeep;

public class timerNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        timerBeep.stop();
        timerBeep=null;
        int notificationId=intent.getIntExtra("id",0);
        ClockTimerFragment.managerCompat.cancel(notificationId);
    }
}
