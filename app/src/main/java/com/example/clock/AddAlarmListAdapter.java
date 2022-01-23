package com.example.clock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.clock.AlarmRepeatDialog.*;

public class AddAlarmListAdapter extends BaseAdapter {
    String[] featureName={"Repeat", "Sound", "Ring Duration", "Snooze Duration"};
    public static int SELECTED_DAYS_NUM=0;
    Context context;

    public AddAlarmListAdapter(Context context){
        this.context=context;
    }

    @Override
    public int getCount() {
        return featureName.length;
    }

    @SuppressLint({"ViewHolder", "InflateParams", "SetTextI18n"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v=layoutInflater.inflate(R.layout.add_alarm_row_layout,null);
        TextView featureNameTextView=v.findViewById(R.id.feature);
        TextView featureTextView =v.findViewById(R.id.feature_selected);

        if(i==0){
            if(selectedItems!=null){
                for(int j = 0; j< selectedItems.size(); j++){
                    if(featureTextView.getText().toString().equals("Only ring once"))
                        featureTextView.setText(selectedItems.get(j)+" ");
                    else
                        featureTextView.append(selectedItems.get(j)+" ");
                }
            }
            else
                featureTextView.setText("Only ring once");
            String s=featureNameTextView.getText().toString();
            boolean test = false;
            for(int j=0;j< days.length;j++){
                if(s.contains(days[j]))
                    test=true;
                else
                    test=false;
            }
            if(test)
                featureTextView.setText("Every day");
        }

        else if(i==1)
            featureTextView.setText(AlarmRingtoneDialog.selectedSound);
        else if(i==2)
            featureTextView.setText(AlarmDurationDialog.duration);
        else
            featureTextView.setText(AlarmDurationDialog.snooze);
        featureNameTextView.setText(featureName[i]);
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
