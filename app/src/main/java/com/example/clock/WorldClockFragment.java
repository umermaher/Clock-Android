package com.example.clock;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class WorldClockFragment extends Fragment {
    FloatingActionButton addCityBtn;
    ListView timeZoneLists;
    TimeZoneAdapter addedTimeZoneAdapter;
    TextView timeZoneName,timeZoneTime;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.world_clock_fragment,container,false);
        addCityBtn=v.findViewById(R.id.add_city_btn);
        timeZoneLists=v.findViewById(R.id.time_zone_List);
        timeZoneName=v.findViewById(R.id.timezone_name);
        timeZoneTime=v.findViewById(R.id.timezone_time);

        TimeZone timeZone=TimeZone.getDefault();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat=new SimpleDateFormat("EEEE, d MMMM");
        Date now=new Date();
        dateFormat.setTimeZone(timeZone);
        timeZoneName.setText(AddTimeZoneActivity.getDisplay(timeZone.getID()));  //getDisplay edit timezone id
        timeZoneTime.setText(dateFormat.format(now));

        addedTimeZoneAdapter=new TimeZoneAdapter(getActivity());
        timeZoneLists.setAdapter(addedTimeZoneAdapter);

        TimeZoneDb db=new TimeZoneDb(getActivity());

        timeZoneLists.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getActivity(),EditTimeZoneActivity.class);
                intent.putExtra("toolbarTitle","");
                startActivity(intent);
                return true;
            }
        });

        addCityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),AddTimeZoneActivity.class);
                getActivity().startActivity(intent);
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        addedTimeZoneAdapter=new TimeZoneAdapter(getActivity());
        timeZoneLists.setAdapter(addedTimeZoneAdapter);
    }
}
