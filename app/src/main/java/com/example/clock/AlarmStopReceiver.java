package com.example.clock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import static com.example.clock.AlarmReceiver.ALARM_NOTIFICATION_ID;
import static com.example.clock.AlarmReceiver.player;

public class AlarmStopReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        Intent soundIntent=new Intent(context,AlarmServices.class);
//        context.stopService(soundIntent);
        if(player!=null)
            player.stop();
        AlarmReceiver.alarmNotificationManagerCompat.cancel(ALARM_NOTIFICATION_ID);
        AlarmReceiver.vibrator.cancel();
        int alarmId=intent.getIntExtra("id",0);
        ManageAlarms ma=new ManageAlarms(context);
        ma.deletingAlarms(alarmId);
        ma.updatingAlarms();
    }
}
