package com.example.clock;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

import static com.example.clock.AlarmRepeatDialog.days;
import static com.example.clock.AlarmRepeatDialog.selectedItems;

public class AddAlarm extends AppCompatActivity {
    int hour=-1, min=-1, listRow;
    ImageView cancelAlarm, saveAlarm;
    TimePicker timePicker;
    ListView featureList;
    long timeInMillis;
    Calendar calendar;
    TextView toolbarTitle;
    String vibration="",midday=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);
        saveAlarm = findViewById(R.id.save);
        cancelAlarm = findViewById(R.id.cancel);
        addAlarmToolbarFunctions();
        timePicker = findViewById(R.id.time_picker);
        featureList = findViewById(R.id.feature_list);
        toolbarTitle=findViewById(R.id.toolbar_title);
        String title=getIntent().getStringExtra("toolbarTitle");
        toolbarTitle.setText(title);

        String s=getIntent().getStringExtra("cancel");



        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                hour = i;
                min = i1;
//                Toast.makeText(AddAlarm.this, hour+":"+min, Toast.LENGTH_SHORT).show();
                if(hour>=12)
                    midday="pm";
                else
                    midday="am";
                calendar = Calendar.getInstance();

//                int currentHour = calendar.get(Calendar.HOUR);
//                int currentMin = calendar.get(Calendar.MINUTE);
//                int currentSec = calendar.get(Calendar.SECOND);

//                if (currentHour > hour)
//                    calendar.add(Calendar.HOUR, hour - currentHour);
//                else
//                    calendar.add(Calendar.HOUR, currentHour - hour);
//
//
//                if (currentMin > min)
//                    calendar.add(Calendar.MINUTE, currentMin - min);
//                else
//                    calendar.add(Calendar.MINUTE, min - currentMin);
                calendar.set(Calendar.HOUR,hour);
                calendar.set(Calendar.MINUTE,min);
                calendar.set(Calendar.SECOND,0);
                //   calendar.add(Calendar.SECOND,60-currentSec);

                //                Time at which broadcast is send
                //while setting the alarm, we get current seconds, therefore current seconds must be neglect to get the alarm at perfect time
                timeInMillis = calendar.getTimeInMillis();///- (currentSec * 1000);

            }
        });


        AddAlarmListAdapter adp = new AddAlarmListAdapter(AddAlarm.this);
        featureList.setAdapter(adp);
        featureList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listRow = i;
                if (i == 0) {
                    AlarmRepeatDialog.selectedItems = null;
                    openAlarmDialog(new AlarmRepeatDialog(), "Repeat days Dialog");
                    //openAlarmRepeatDialog();
                } else if (i == 1) {
                    openAlarmDialog(new AlarmRingtoneDialog(), "Select sound dialog");
                    //openAlarmRingtoneDialog();
                } else if (i == 2) {
                    openAlarmDialog(new AlarmDurationDialog("Ring duration"), "Select duration dialog");
                } else if (i == 3) {
                    openAlarmDialog(new AlarmDurationDialog("Snooze duration"), "Select snooze dialog");
                }
            }
        });
    }

    private void openAlarmDialog(AppCompatDialogFragment f, String s) {
        f.setCancelable(false);
        f.show(getSupportFragmentManager(), s);

    }

    private void addAlarmToolbarFunctions() {
        saveAlarm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                if(hour==-1 && min==-1 && toolbarTitle.getText().toString().equals("Edit alarm")){
                    int idForUpdate=getIntent().getIntExtra("id",0);
                    AlarmDb db=new AlarmDb(AddAlarm.this);
                    Alarm alarm = (Alarm) db.searchData(String.valueOf(idForUpdate));
                    calendar.set(Calendar.HOUR,alarm.getHour());
                    calendar.set(Calendar.MINUTE,alarm.getMin());
                    calendar.set(Calendar.SECOND,0);
                }
                else if(hour==-1 && min==-1 && toolbarTitle.getText().toString().equals("Add alarm")){
                    calendar=Calendar.getInstance();
                }

//                Toast.makeText(AddAlarm.this, "Ring at "+String.format(Locale.getDefault(),
//                          "%02d:%02d",calendar.get(Calendar.HOUR),calendar.get(Calendar.MINUTE)), Toast.LENGTH_SHORT).show();

                AlarmDb db = new AlarmDb(AddAlarm.this);
                String h = String.valueOf(calendar.get(Calendar.HOUR));
                String m = String.valueOf(calendar.get(Calendar.MINUTE));
                String l = String.valueOf(timeInMillis);
                String repeat= "Only ring once";
                if(selectedItems!=null){
                    repeat="";
                    for(int i = 0; i< selectedItems.size(); i++){
                        repeat+=" "+selectedItems.get(i);
                    }
                    boolean test=false;
                    for(int j=0;j< days.length;j++){
                        if(repeat.contains(days[j]))
                            test=true;
                        else
                            test=false;
                    }
                    if(test)
                        repeat="Every day";
                }

                String sound=AlarmRingtoneDialog.selectedSound;

                Alarm alarm = new Alarm(Integer.parseInt(h), Integer.parseInt(m), Long.parseLong(l), repeat,
                        sound, AlarmDurationDialog.SELECTED_DURATION_TIME,AlarmDurationDialog.SELECTED_SNOOZE_TIME,vibration,midday);

                int idForUpdate=getIntent().getIntExtra("id",0);
                if(toolbarTitle.getText().toString().equals("Add alarm")){
                    db.insert(alarm);
                }
                else if(toolbarTitle.getText().toString().equals("Edit alarm")) {
                    db.update(alarm, String.valueOf(idForUpdate));
                }
                ManageAlarms ma=new ManageAlarms(AddAlarm.this);
                ma.updatingAlarms();

                Calendar remainingTime=Calendar.getInstance();
                int remainingHour=Integer.parseInt(h)-remainingTime.get(Calendar.HOUR);
                int remainingMin=Integer.parseInt(m)-remainingTime.get(Calendar.MINUTE);
//                remainingTime.set(Calendar.HOUR,remainingHour);

                if(remainingHour>=1) {
                    if (remainingMin>=1)
                        Toast.makeText(AddAlarm.this, "Ring in "+remainingHour+" hours "+remainingMin+" minutes", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(AddAlarm.this, "Ring in "+remainingHour+" hours", Toast.LENGTH_SHORT).show();
                }
                else if(remainingMin>1)
                    Toast.makeText(AddAlarm.this, "Ring in "+remainingMin+" minutes", Toast.LENGTH_SHORT).show();
                else if(remainingMin==1)
                    Toast.makeText(AddAlarm.this, "Ring within a minute", Toast.LENGTH_SHORT).show();

                Intent done=new Intent(AddAlarm.this,MainActivity.class);
                startActivity(done);
            }
        });
        cancelAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    int count=1;
    public void vibrate(View view) {
        count++;
        if(count%2==0)
            vibration="vibrate";
        else
            vibration="not";
    }
}