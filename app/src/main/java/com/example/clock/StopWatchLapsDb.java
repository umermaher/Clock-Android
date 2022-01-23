package com.example.clock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class StopWatchLapsDb extends SQLiteOpenHelper {
    public static final String DBName="StopWatch.db";
    public static final String TABLE="stopwatchlaps";
    public static final String TIMER="timer";
    public static final String ID="id";

    Context context;

    public StopWatchLapsDb(@Nullable Context context) {
        super(context, DBName, null, 1);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query="create table stopwatchlaps (id integer primary key, timer text);";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if(i>i1) {
            String query="create table stopwatchlaps (id integer primary key, timer text);";
            sqLiteDatabase.execSQL(query);
        }
    }

    public void insert(String timer) {
        SQLiteDatabase sql = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TIMER,timer);
        sql.insert(TABLE,null,cv);
        sql.close();
    }

    public List<String> getAllData(){
        List<String > lapsData=new ArrayList<>();
        SQLiteDatabase sql=this.getReadableDatabase();
        Cursor cursor = sql.query(TABLE, null, null, null, null, null, null);
//        if only table is passed then the whole data can get
        //This interface provides random read-write access to the result set returned by a database query.
        //In simple words cursor points a particular row to access data from it.
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            String laps=cursor.getString(1);

            lapsData.add(laps);

            cursor.moveToNext();
        }

        sql.close();
        return lapsData;
    }


    public void deleteAll() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        //deleting rows
        sqLiteDatabase.delete(TABLE, null, null);
        sqLiteDatabase.close();
    }
}
