package com.example.clock;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.INotificationSideChannel;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.Calendar;
import java.util.Locale;

import static com.example.clock.AlarmReceiver.SNOOZE_ALARM_NOTIFICATION_ID;
import static com.example.clock.AlarmReceiver.alarmNotificationManagerCompat;
import static com.example.clock.AlarmReceiver.player;

public class AlarmSnoozeReceiver extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {
//        Intent soundIntent=new Intent(context,AlarmServices.class);
//        context.stopService(soundIntent);
        if(player!=null)
            player.stop();
        alarmNotificationManagerCompat.cancel(AlarmReceiver.ALARM_NOTIFICATION_ID);
        AlarmReceiver.vibrator.cancel();

        int snooze=intent.getIntExtra("snooze",1);
        int id=intent.getIntExtra("alarmId",0);
        int hour=intent.getIntExtra("hour",0);
        int min=intent.getIntExtra("min",0);
        String sound=intent.getStringExtra("sound");
        String duration=intent.getStringExtra("duration");
        String vibration=intent.getStringExtra("vibration");

        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.MINUTE,snooze);

//        if dismiss btn is click then alarm will be cancel, otherwise after snoozing alarm will be work
        Intent dismissIntent=new Intent(context, SnoozedAlarmDismissReceiver.class);
        dismissIntent.putExtra("id",id);
         PendingIntent dismissPendingIntent=PendingIntent.getBroadcast(context,id,dismissIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification=new NotificationCompat.Builder(context.getApplicationContext(),App.CHANNEL_ID)
                .setSmallIcon(R.drawable.alarm_icon)
                .setContentTitle("Alarm(snoozed)")
                .setContentText("Snoozing until "+String.format(Locale.getDefault(),"%02d:%02d",calendar.getTime().getHours(),calendar.getTime().getMinutes()))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSilent(true)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .addAction(R.drawable.alarm_icon,"dismiss",dismissPendingIntent)
                .setAutoCancel(true)
                .setOngoing(true)
                .setColor(Color.BLUE)
                .build();
        alarmNotificationManagerCompat.notify(SNOOZE_ALARM_NOTIFICATION_ID,notification);

        //after snoozing(given time)alarm will work/ring.
        Intent snoozeIntent=new Intent(context,AlarmReceiver.class);
        snoozeIntent.putExtra("sound",sound);
        snoozeIntent.putExtra("duration",duration);
        snoozeIntent.putExtra("vibration",vibration);
        snoozeIntent.putExtra("min",min);
        snoozeIntent.putExtra("hour",hour);

        PendingIntent pendingIntent=PendingIntent.getBroadcast(context,id,snoozeIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager=(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);

        ManageAlarms manageAlarms=new ManageAlarms(context);
        manageAlarms.updatingAlarms();
    }
}
