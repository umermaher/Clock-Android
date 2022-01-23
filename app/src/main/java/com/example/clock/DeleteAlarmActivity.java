package com.example.clock;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.Calendar;
import java.util.List;

import static com.example.clock.AlarmRepeatDialog.days;
import static com.example.clock.AlarmRepeatDialog.selectedItems;

public class DeleteAlarmActivity extends AppCompatActivity {
    GridView deleteListView;
    ImageView cancelAlarm,deleteAlarm;
    List<Alarm> alarms;
    DeleteAlarmListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_alarm);
        deleteListView=findViewById(R.id.delete_alarm_list);
        cancelAlarm=findViewById(R.id.cancel_Activity);
        deleteAlarm=findViewById(R.id.delete_alarm);

        adapter=new DeleteAlarmListAdapter(DeleteAlarmActivity.this);
        deleteListView.setAdapter(adapter);
        deleteAlarmToolbarFunctions();
    }

    private void deleteAlarmToolbarFunctions() {
        deleteAlarm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
//                DeleteAlarmListAdapter adapter=new DeleteAlarmListAdapter(DeleteAlarmActivity.this);
                AlarmDb db=new AlarmDb(DeleteAlarmActivity.this);
                alarms=db.getAllData();
                ManageAlarms ma=new ManageAlarms(DeleteAlarmActivity.this);

//                Alarm alarm=alarms.get(i);
//                int id=alarms.get(i).getId();
                for(int i=0;i<alarms.size();i++){
                    if(alarms.get(i).getId()==adapter.deleteAlarmsId[i]) {
                        String id = String.valueOf(adapter.deleteAlarmsId[i]);
                        db.delete(id);
                        ma.deletingAlarms(alarms.get(i).getId());
                    }
                }

                Intent intent=new Intent(DeleteAlarmActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        cancelAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}