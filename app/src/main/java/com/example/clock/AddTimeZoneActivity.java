package com.example.clock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class AddTimeZoneActivity extends AppCompatActivity {
    TimeZoneAdapter addTimeZoneAdapter=null;
    List<TimeZoneModel> timeZoneList,resultList;
    GridView addTimeZoneListView;
    ImageView backBtn;
    SearchView searchCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_time_zone);
        addTimeZoneListView=findViewById(R.id.add_time_zone_list);
        backBtn=findViewById(R.id.back_timezone_btn);
        searchCity=findViewById(R.id.search_city);

        timeZoneList=new ArrayList<>();
        addTimeZoneAdapter=new TimeZoneAdapter(this,timeZoneList);
        addTimeZoneListView.setAdapter(addTimeZoneAdapter);
        TimeZoneDb db=new TimeZoneDb(this);
        addTimeZoneListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TimeZoneModel timeZone= (TimeZoneModel) addTimeZoneAdapter.getItem(i);
                boolean test=true; //to check whether new timezone is already added
                List<TimeZoneModel> savedTimeZones=db.getAllData();
                for(int j=0;j<savedTimeZones.size();j++){
                    if(timeZone.getTimeZoneId().equals(savedTimeZones.get(j).getTimeZoneId())){
                        test=false;
                        Toast.makeText(AddTimeZoneActivity.this, "You've already added this city", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                if(test){
                    db.insert(timeZone);
                }
                onBackPressed();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        searchCity.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                resultList=new ArrayList<>();
                for(int i=0;i<timeZoneList.size();i++){
                    if(timeZoneList.get(i).getName().toLowerCase().contains(newText.toLowerCase())){
                        resultList.add(timeZoneList.get(i));
                    }
                }
                if(resultList!=null){
                    addTimeZoneAdapter=new TimeZoneAdapter(AddTimeZoneActivity.this,resultList);
                    addTimeZoneListView.setAdapter(addTimeZoneAdapter);
                }
                if(newText.isEmpty()){
                    addTimeZoneAdapter=new TimeZoneAdapter(AddTimeZoneActivity.this,timeZoneList);
                    addTimeZoneListView.setAdapter(addTimeZoneAdapter);
                }
                return true;
            }
        });
    }

    @Override
    @SuppressLint("SimpleDateFormat")
    protected void onStart() {
        super.onStart();
        String[] listItems= TimeZone.getAvailableIDs();
        TimeZone timeZone=null;
        SimpleDateFormat format=new SimpleDateFormat("d MMMM, hh:mm a");
        Date now=new Date();

        for(int i=0;i<listItems.length;i++){
            timeZone= TimeZone.getTimeZone(listItems[i]);
            format.setTimeZone(timeZone);

            TimeZoneModel timeZoneModel=new TimeZoneModel(listItems[i],getDisplay(listItems[i]),format.format(now));
            timeZoneList.add(timeZoneModel);
            timeZone=null;
        }
    }

    public static String getDisplay(String timeZoneName) {
        String displayName=timeZoneName;
        int sep=displayName.indexOf("/");    //seperation
        if(-1!=sep){
            displayName=displayName.substring(0,sep)+", "+displayName.substring(sep+1);
            displayName=displayName.replace("_"," ");
        }
        return displayName;
    }
}