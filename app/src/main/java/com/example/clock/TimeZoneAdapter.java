package com.example.clock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.jar.Attributes;

public class TimeZoneAdapter extends BaseAdapter {

    List<TimeZoneModel>timeZoneList;
    Context context;
    TimeZoneDb db;
    boolean test=true;   //for checking

    //For showing timezones for adding
    public TimeZoneAdapter(Context context, List<TimeZoneModel> timeZoneList){
        this.context=context;
        this.timeZoneList=timeZoneList;
        test=true;
    }

//    added timezones
    public TimeZoneAdapter(Context context){
        this.context=context;
        db=new TimeZoneDb(context);
        timeZoneList=db.getAllData();
        test=false;
    }
    @Override
    public int getCount() {
        return timeZoneList.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint({"InflateParams", "ViewHolder"}) View v=inflater.inflate(R.layout.time_zone_row,null);
        TextView timeZoneName=v.findViewById(R.id.timezone_name);
        TextView timeZoneTime=v.findViewById(R.id.timezone_time);
        TimeZoneModel timeZoneModel=timeZoneList.get(i);
        if(test) {
            timeZoneName.setText(timeZoneModel.getName());
            timeZoneTime.setText(timeZoneModel.getTime());
        }
        else {
            String id = timeZoneList.get(i).getTimeZoneId();
            TimeZone timeZone = TimeZone.getTimeZone(id);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM, hh:mm a");
            Date now = new Date();
            dateFormat.setTimeZone(timeZone);

            timeZoneName.setText(AddTimeZoneActivity.getDisplay(timeZone.getID()));
            timeZoneTime.setText(dateFormat.format(now));
        }
        return v;
    }

    @Override
    public Object getItem(int i) {
        return timeZoneList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
}
