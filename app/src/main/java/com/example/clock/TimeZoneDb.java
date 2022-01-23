package com.example.clock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class TimeZoneDb extends SQLiteOpenHelper {
    public static final String DBName="TimeZones.db";
    public static final String TABLE="timezones";
    public static final String ID="id";
    public static final String TIMEZONE_ID="timezoneid";
    public static final String NAME="name";
    public static final String TIME="time";
    Context context;

    public TimeZoneDb(@Nullable Context context) {
        super(context, DBName, null, 2);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query="create table timezones (id integer primary key, timezoneid text, name text,time text);";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insert(TimeZoneModel timeZone){
        SQLiteDatabase sql=this.getWritableDatabase();
        ContentValues cv=new ContentValues();

        cv.put(TIMEZONE_ID,timeZone.getTimeZoneId());
        cv.put(NAME,timeZone.getName());
        cv.put(TIME,timeZone.getTime());

        long test = sql.insert(TABLE,null,cv);
        if(test==-1){
            Toast.makeText(context, "Data not inserted!", Toast.LENGTH_SHORT).show();
        }
        sql.close();
    }

    public void delete(String id){
        SQLiteDatabase sql=this.getWritableDatabase();
        sql.delete(TABLE,"id = ?",new String[]{id});
        //       Toast.makeText(context, "alarm data deleted", Toast.LENGTH_SHORT).show();
    }

    public List<TimeZoneModel> getAllData(){
        List<TimeZoneModel> timeZoneList=new ArrayList<>();
        SQLiteDatabase sql=this.getReadableDatabase();
        Cursor cursor = sql.query(TABLE, null, null, null, null, null, null);
        //        if only table is passed then the whole data can get
        //This interface provides random read-write access to the result set returned by a database query.
        //In simple words cursor points a particular row to access data from it.

        cursor.moveToNext();
        for(int i=0;i<cursor.getCount();i++){
            int id=cursor.getInt(0);
            String timeZoneId=cursor.getString(1);
            String name=cursor.getString(2);
            String time=cursor.getString(3);

            TimeZoneModel timeZone=new TimeZoneModel(timeZoneId,name,time);
            timeZone.setId(id);
            timeZoneList.add(timeZone);

            cursor.moveToNext();
        }
        sql.close();
        return timeZoneList;
    }

    public TimeZoneModel searchData(String search){
        TimeZoneModel timeZone = null;
        SQLiteDatabase sql=this.getReadableDatabase();
        Cursor cursor=sql.query(TABLE,null,"name = ? OR time = ?", new String[]{search,search},null,null,null);
        cursor.moveToFirst();
        for(int i=0;i<cursor.getCount();i++){
            int id = cursor.getInt(0);
            String timeZoneId=cursor.getString(1);
            String name=cursor.getString(2);
            String time=cursor.getString(3);

            timeZone=new TimeZoneModel(timeZoneId,name,time);
            timeZone.setId(id);

            cursor.moveToNext();
        }
        return timeZone;
    }
}
