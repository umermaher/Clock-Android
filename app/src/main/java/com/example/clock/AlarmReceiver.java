package com.example.clock;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Parcelable;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import java.util.Calendar;
import java.util.Locale;

import static com.example.clock.AlarmRingtoneDialog.ringtones;
import static com.example.clock.App.CHANNEL_ID;

public class AlarmReceiver extends BroadcastReceiver {
    @SuppressLint("StaticFieldLeak")
    public static NotificationManagerCompat alarmNotificationManagerCompat;
    Notification notification;
    public static MediaPlayer player;
    int[] soundRecources={R.raw.clock_alarm,R.raw.alarm_rooster_hug,R.raw.snoop_dogg,R.raw.bugle_call,R.raw.still_dre,R.raw.funny_alarm,R.raw.beep_alarm};
    static Vibrator vibrator;
    public static int ALARM_NOTIFICATION_ID=2, SNOOZE_ALARM_NOTIFICATION_ID=3;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra("id", 0);
        int hour = intent.getIntExtra("hour", 0);
        int min = intent.getIntExtra("min", 0);
        long timeInMillis = intent.getLongExtra("timeInMillis", 0);
        String repeat = intent.getStringExtra("repeat");
        String sound = intent.getStringExtra("sound");
        int duration = intent.getIntExtra("duration", 1);
        int snooze = intent.getIntExtra("snooze", 1);
        String vibration = intent.getStringExtra("vibration");
        String midday = intent.getStringExtra("midday");
        long durationInMillis = (long) duration * 60000;
        durationInMillis += Calendar.getInstance().getTimeInMillis();

        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibration.equals("not")) {
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                vibrator.vibrate(VibrationEffect.createWaveform(new long[]{1000, 1000, 1000}, 1));
            else {
                long[] pattern = {0, 200, 0, 100, 0};
                vibrator.vibrate(pattern, 1);
            }
        }

        Intent stopIntent = new Intent(context, AlarmStopReceiver.class);
        stopIntent.putExtra("id", id);
        PendingIntent stopPendingIntent = PendingIntent.getBroadcast(context, 100, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent snoozeIntent = new Intent(context, AlarmSnoozeReceiver.class);
        snoozeIntent.putExtra("AlarmId", id);
        snoozeIntent.putExtra("min", min);
        snoozeIntent.putExtra("hour", hour);
        snoozeIntent.putExtra("snooze", snooze);
        snoozeIntent.putExtra("sound", sound);
        snoozeIntent.putExtra("duration", duration);
        snoozeIntent.putExtra("vibration", vibration);
        PendingIntent snoozePendingIntent = PendingIntent.getBroadcast(context, 101, snoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmNotificationManagerCompat = NotificationManagerCompat.from(context);
        alarmNotificationManagerCompat.cancel(SNOOZE_ALARM_NOTIFICATION_ID);

        notification = new NotificationCompat.Builder(context.getApplicationContext(), App.CHANNEL_ID)
                .setSmallIcon(R.drawable.alarm_icon)
                .setContentTitle("Alarm")
                .setContentText(String.format(Locale.getDefault(), "%02d:%02d", hour, min))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSilent(true)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .addAction(R.drawable.alarm_icon, "Stop", stopPendingIntent)
                .addAction(R.drawable.alarm_icon, "snooze", snoozePendingIntent)
                .setAutoCancel(true)
                .setOngoing(true)
                .setColor(Color.rgb(35, 35, 255))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .build();
        alarmNotificationManagerCompat.notify(ALARM_NOTIFICATION_ID, notification);

        for (int i = 0; i < ringtones.length; i++) {
            if (ringtones[i].equals(sound)) {
                player = MediaPlayer.create(context.getApplicationContext(), soundRecources[i]);
                player.start();
            }
        }
        long finalDurationInMillis = durationInMillis;
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (finalDurationInMillis > System.currentTimeMillis())
                    player.start();
                else {
                    alarmNotificationManagerCompat.cancel(ALARM_NOTIFICATION_ID);
                    vibrator.cancel();
                }
            }
        });
//        Intent soundIntent=new Intent(context,AlarmServices.class);
//        soundIntent.putExtra("sound",sound);
//        soundIntent.putExtra("duration",duration);
//        context.startService(soundIntent);
//            Toast.makeText(context, " " + id + " " + hour + " " + min + " " + timeInMillis + " " + repeat + " " + sound + " " + duration + " " + snooze + " " + vibration + " " + midday, Toast.LENGTH_SHORT).show();
    }
}