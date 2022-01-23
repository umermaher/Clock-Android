package com.example.clock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;

import java.util.List;
import java.util.Locale;

public class AlarmListAdapter extends BaseAdapter {
    List<Alarm> alarmList;
    Context context;
    TextView rowTime;
    public AlarmListAdapter(Context context) {
        this.context = context;
        AlarmDb db=new AlarmDb(context);
        alarmList=db.getAllData();
    }

    @Override
    public int getCount() {
        return alarmList.size();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint({"ViewHolder", "InflateParams"}) View v=inflater.inflate(R.layout.alarm_list_row,null);
         rowTime=v.findViewById(R.id.row_time);
        TextView rowRepeat=v.findViewById(R.id.row_repeat_text);
        TextView amPm=v.findViewById(R.id.ampm);
//        SwitchCompat switchCompat=v.findViewById(R.id.set_alarm);
        Alarm alarm=alarmList.get(i);
        rowTime.setText(String.format(Locale.getDefault(),"%02d:%02d",alarm.getHour(),alarm.getMin()));
        amPm.setText(alarm.getMidday());
        rowRepeat.setText("Alarm, "+alarm.repeat);

        int count=1;
//        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if(b){
//                    Toast.makeText(context, "On", Toast.LENGTH_SHORT).show();
//                }else
//                    Toast.makeText(context, "Off", Toast.LENGTH_SHORT).show();
//            }
//        });
        return v;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
}
