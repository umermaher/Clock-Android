package com.example.clock;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ManageAlarms {
    List<Alarm> alarmList;
    Context context;
    PendingIntent pendingIntent;
    int[] daysOfWeek={2,3,4,5,6,7,1};
    ManageAlarms(Context context){
        AlarmDb db=new AlarmDb(context);
        alarmList=db.getAllData();
        this.context=context;
    }
    public void updatingAlarms() {
        List<Calendar> calendarList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < alarmList.size(); i++) {
            Alarm alarm = alarmList.get(i);
            calendar.set(Calendar.HOUR, alarm.getHour());
            calendar.set(Calendar.MINUTE, alarm.getMin());
            calendar.set(Calendar.SECOND, 0);
            //To which reciever it will be send
            Intent intent = new Intent(context.getApplicationContext(), AlarmReceiver.class);
            //How to set some task
            AlarmManager manager = null;

            manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            if (calendar.getTimeInMillis() <= Calendar.getInstance().getTimeInMillis()) {
//                calendar.add(Calendar.DATE, 1);
//                //calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);


                if (alarm.getRepeat().equals("Only ring once")) {
                    deletingAlarms(alarm.getId());
                } else if (alarm.getRepeat().equals("Every day")) {
                    calendar.add(Calendar.DAY_OF_WEEK, 1);
                } else {
//                    if schedule is customize by user
                    //setting calendar day given by user
                    String daysInString = alarm.getRepeat()
                            ;
                    List<Integer> daysInInt = new ArrayList<>();
                    for (int j = 0; j < AlarmRepeatDialog.days.length; j++) {
                        if (daysInString.contains(AlarmRepeatDialog.days[i])) {
                            daysInInt.add(daysOfWeek[i]);
                        }
                    }
                    for(int j=0;j<daysInInt.size();j++) {
                        if(daysInInt.get(i)==calendar.get(Calendar.DAY_OF_WEEK)){
                            Integer nums=null;
                            try{
                                nums=daysInInt.get(i+1);
                                if(nums!=null){
                                    calendar.set(Calendar.DAY_OF_WEEK, nums);
                                    break;
                                }
                            }catch(Exception e){
                                nums=daysInInt.get(0);
                                calendar.set(Calendar.DAY_OF_WEEK, nums);
                            }
                        }
                    }
                }
            continue;
        }
            intent.putExtra("id",alarm.getId());
            intent.putExtra("hour",alarm.getHour());
            intent.putExtra("min",alarm.getMin());
            intent.putExtra("timeInMillis",alarm.getTimeInMillis());
            intent.putExtra("repeat",alarm.getRepeat());
            intent.putExtra("sound",alarm.getSound());
            intent.putExtra("duration",alarm.getRingDuration());
            intent.putExtra("snooze",alarm.getSnooze());
            intent.putExtra("vibration",alarm.getVibration());
            intent.putExtra("midday",alarm.getMidday());
            int requestCode=alarm.getId();
            //pending alarm performs an action according to the given time
            pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            assert manager != null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }
    }

    public void deletingAlarms(int id) {

        Intent intent = new Intent(context, AlarmReceiver.class);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context, id, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        if (pendingIntent != null && alarmManager != null) {
            alarmManager.cancel(pendingIntent);
//            Toast.makeText(context, "cancelled", Toast.LENGTH_SHORT).show();
        }
    }
}
