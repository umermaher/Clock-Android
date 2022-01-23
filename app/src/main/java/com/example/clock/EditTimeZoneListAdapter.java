package com.example.clock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class EditTimeZoneListAdapter extends BaseAdapter {
    Context context;
    List<TimeZoneModel> timeZoneLists;
    TimeZoneDb db;
    List<String > ids;
    public EditTimeZoneListAdapter(Context context){
        this.context=context;
        db=new TimeZoneDb(context);
        this.timeZoneLists=db.getAllData();
        ids=new ArrayList<>();
    }
    @Override
    public int getCount() {
        return timeZoneLists.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint({"ViewHolder", "InflateParams"}) View v=inflater.inflate(R.layout.edit_timezone_list_row,null);
        ImageView deleteTimeZone=v.findViewById(R.id.delete_timezone);
        TextView timeZoneName=v.findViewById(R.id.edit_timezone_name);
        TimeZoneModel model=timeZoneLists.get(i);
        timeZoneName.setText(model.getName());
        deleteTimeZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id=String.valueOf(model.getId());
                ids.add(id);
                timeZoneLists.remove(i);
                notifyDataSetChanged();
            }
        });
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

