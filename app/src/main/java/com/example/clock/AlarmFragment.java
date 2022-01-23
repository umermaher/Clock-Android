package com.example.clock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AlarmFragment extends Fragment {
    TextView digitalTime;                 //main time showing view
    FloatingActionButton newAlarmButton;  //Button for creating new alarm
    ListView alarmList;
    List<Alarm> alarms;
    TextView statusText;
    AlarmDb db;
    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable  Bundle savedInstanceState) {
         View view =inflater.inflate(R.layout.alarm_fragment,container,false);
         digitalTime=view.findViewById(R.id.digital_time);
         newAlarmButton=view.findViewById(R.id.fab);
        alarmList=view.findViewById(R.id.alarmList);
        statusText=view.findViewById(R.id.alarms_status);
        db=new AlarmDb(getActivity());


        List<Alarm> alarmsList=db.getAllData();
        if(alarmsList.size()!=0){
            statusText.setText(".  .  .  .  .");
        }
//        Alarm alarm=null;
//        try{
//            alarm = alarmsList.get(0);
//        }catch (Exception e){
//            alarm=null;
//        }
//        for(int i=0;i<alarmsList.size();i++){
//            long time1=alarmsList.get(i).getTimeInMillis();
//            for(int j=i+1;j<alarmsList.size();j++){
//                long time2=alarmsList.get(j).getTimeInMillis();
//                if(time2>time1) {
//                    alarm=alarmsList.get(i);
//                }
//            }
//        }
//        if(alarm!=null) {
//            Calendar c = Calendar.getInstance();
//            c.set(Calendar.HOUR, Objects.requireNonNull(alarm).getHour());
//            c.set(Calendar.MINUTE, alarm.getMin());
//            String s = String.format(Locale.getDefault(), "%02d:%02d", alarm.getHour(), alarm.getMin());
//            ringToBeText.setText("Ring at " + s);
//        }
         AlarmListAdapter adp=new AlarmListAdapter(getActivity());
         alarmList.setAdapter(adp);
         alarmList.setLongClickable(true);
         new StopWatchLapsDb(getActivity()).deleteAll();
         onCLick();
         onTouch();
         newAlarm();
         return view;
    }

    private void newAlarm() {
        newAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(),AddAlarm.class);
                i.putExtra("toolbarTitle","Add alarm");
                requireActivity().startActivity(i);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void onCLick(){
        alarmList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                alarms=db.getAllData();

                Alarm alarm=alarms.get(i);
                int id=alarms.get(i).getId();

                Intent intent=new Intent(getActivity(),AddAlarm.class);
                intent.putExtra("toolbarTitle","Edit alarm");
               intent.putExtra("id",id);
                requireActivity().startActivity(intent);
            }
        });
    }
    private void onTouch(){
        alarmList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getActivity(),DeleteAlarmActivity.class);
                startActivity(intent);
                return true;
            }
        });
    }
}
