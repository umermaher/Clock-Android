package com.example.clock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class LapsListAdapter extends BaseAdapter {
    List<String> lapsList;
    Context context;
    public LapsListAdapter(Context context) {
        this.context = context;
        StopWatchLapsDb db=new StopWatchLapsDb(context);
        lapsList=db.getAllData();
    }
    @Override
    public int getCount() {
        return lapsList.size();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint({"ViewHolder", "InflateParams"}) View v=inflater.inflate(R.layout.stop_watch_laps_row,null);
        TextView lapNum=v.findViewById(R.id.lap_num);
        TextView lap=v.findViewById(R.id.lap);
        String s=lapsList.get(i);
        lap.setText(s);
        lapNum.setText("Lap "+(i+1));
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
