package com.example.clock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AlarmDb extends SQLiteOpenHelper {
    public static final String DBName="Alarms.db";
    public static final String TABLE="alarms";
    public static final String ID="id";
    public static final String HOUR="hours";
    public static final String MIN="mins";
    public static final String REPEAT="repeats";
    public static final String SOUND="sounds";
    public static final String DURATION="duration";
    public static final String SNOOZE="snooze";
    public static final String VIBRATE="vibrate";
    public static final String TIMEINMILLIS="timeinmillis";
    public static final String MIDDAY="midday";
    Context context;

    public AlarmDb(Context context) {
        super(context, DBName, null, 2);
        this.context=context;
    }
    //To create table(s) in your database
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query="create table alarms (id integer primary key, hours text, mins text,timeinmillis text, midday text, repeats text, sounds text, duration text, snooze text,vibrate text);";
        sqLiteDatabase.execSQL(query);
        Toast.makeText(context, "created", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    //    Insert data into database
    public void insert(Alarm alarm) {
        SQLiteDatabase sql=this.getWritableDatabase();
        ContentValues cv=new ContentValues();

        cv.put(HOUR,alarm.getHour());
        cv.put(MIN,alarm.getMin());
        cv.put(TIMEINMILLIS,alarm.getTimeInMillis());
        cv.put(MIDDAY,alarm.getMidday());
        cv.put(REPEAT,alarm.getRepeat());
        cv.put(SOUND,alarm.getSound());
        cv.put(DURATION,alarm.getRingDuration());
        cv.put(SNOOZE,alarm.getSnooze());
        cv.put(VIBRATE,alarm.getVibration());
//        cv.put(MERIDIEM,alarm.getMeridiem());
        long test = sql.insert(TABLE,null,cv);
        if(test==-1){
            Toast.makeText(context, "Data not inserted!", Toast.LENGTH_SHORT).show();
        }
        sql.close();
    }
    //for update
    public void update(Alarm alarm,String id){
        SQLiteDatabase sql=this.getWritableDatabase();
        ContentValues cv=new ContentValues();

        cv.put(HOUR,alarm.getHour());
        cv.put(MIN,alarm.getMin());
        cv.put(TIMEINMILLIS,alarm.getTimeInMillis());
        cv.put(MIDDAY,alarm.getMidday());
        cv.put(REPEAT,alarm.getRepeat());
        cv.put(SOUND,alarm.getSound());
        cv.put(DURATION,alarm.getRingDuration());
        cv.put(SNOOZE,alarm.getSnooze());
        cv.put(VIBRATE,alarm.getVibration());

        sql.update(TABLE,cv,"id = ?",new String[]{id});
        Toast.makeText(context, "Alarm updated", Toast.LENGTH_SHORT).show();
//        in 4th argument string array is passed to check multiple columns e.g,
        sql.close();
    }
    //delete row
    public void delete(String id){
        SQLiteDatabase sql=this.getWritableDatabase();
        sql.delete(TABLE,"id = ?",new String[]{id});
 //       Toast.makeText(context, "alarm data deleted", Toast.LENGTH_SHORT).show();
    }

    public List<Alarm> getAllData(){
        List<Alarm> alarmsData=new ArrayList<>();
        SQLiteDatabase sql=this.getReadableDatabase();
        Cursor cursor = sql.query(TABLE, null, null, null, null, null, null);
//        if only table is passed then the whole data can get
        //This interface provides random read-write access to the result set returned by a database query.
        //In simple words cursor points a particular row to access data from it.
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            int id = cursor.getInt(0);
            int hour = cursor.getInt(1);
            int min = cursor.getInt(2);
            long timeInMillis=cursor.getLong(3);
            String midday =cursor.getString(4);
            String repeat = cursor.getString(5);
            String sound=cursor.getString(6);
            int duration=cursor.getInt(7);
            int snooze=cursor.getInt(8);
            String vibration=cursor.getString(9);
//            String meridiem=cursor.getString(9);

            Alarm temp = new Alarm(hour, min, timeInMillis,repeat, sound,duration,snooze,vibration,midday);
            temp.setId(id);
            alarmsData.add(temp);

            cursor.moveToNext();
        }

        sql.close();
        return alarmsData;
    }
    public Alarm searchData(String search){
        Alarm alarm = null;
        SQLiteDatabase sql=this.getReadableDatabase();
        Cursor cursor=sql.query(TABLE,null,"id = ?", new String[]{search},null,null,null);
        cursor.moveToFirst();
        for(int i=0;i<cursor.getCount();i++){
            int id = cursor.getInt(0);
            int hour = cursor.getInt(1);
            int min = cursor.getInt(2);
            long timeInMillis=cursor.getLong(3);
            String midday =cursor.getString(4);
            String repeat = cursor.getString(5);
            String sound=cursor.getString(6);
            int duration=cursor.getInt(7);
            int snooze=cursor.getInt(8);
            String vibration=cursor.getString(9);

            alarm = new Alarm(hour, min, timeInMillis,repeat, sound,duration,snooze,vibration,midday);
            alarm.setId(id);
            cursor.moveToNext();
        }
        return alarm;
    }
}
