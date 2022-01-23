package com.example.clock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SnoozedAlarmDismissReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ManageAlarms manageAlarms=new ManageAlarms(context);
        int alarmId=intent.getIntExtra("id",0);
        manageAlarms.deletingAlarms(alarmId);

        AlarmReceiver.alarmNotificationManagerCompat.cancel(AlarmReceiver.SNOOZE_ALARM_NOTIFICATION_ID);
    }
}
