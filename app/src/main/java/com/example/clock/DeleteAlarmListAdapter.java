package com.example.clock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DeleteAlarmListAdapter extends BaseAdapter {
    List<Alarm> alarmList;
    int[] deleteAlarmsId;
    Context context;
    TextView rowTime;
    CheckBox checkBox;
    public DeleteAlarmListAdapter(Context context) {
        this.context = context;
        AlarmDb db=new AlarmDb(context);
        alarmList=db.getAllData();
        deleteAlarmsId=new int[alarmList.size()];
    }

    @Override
    public int getCount() {
        return alarmList.size();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint({"ViewHolder", "InflateParams"}) View v=inflater.inflate(R.layout.delete_alarm_list_row,null);

        rowTime=v.findViewById(R.id.row_time);
        TextView rowRepeat=v.findViewById(R.id.row_repeat_text);
        TextView amPm=v.findViewById(R.id.ampm);
        checkBox=v.findViewById(R.id.checked);

        Alarm alarm=alarmList.get(i);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    deleteAlarmsId[i]=alarm.getId();
//                    Toast.makeText(context, deleteAlarmsId[i]+"", Toast.LENGTH_SHORT).show();
                }
                else{
                    deleteAlarmsId[i]=0;
                }
            }
        });

        setMin(alarm);

        amPm.setText(alarm.getMidday());

        rowRepeat.setText("Alarm, "+alarm.repeat);

        return v;
    }

    @SuppressLint("SetTextI18n")
    private void setMin (Alarm alarm){
        if(alarm.getMin()<10)
            rowTime.setText(alarm.getHour()+":"+"0"+alarm.getMin());
        else
            rowTime.setText(alarm.getHour()+":"+alarm.getMin());
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
