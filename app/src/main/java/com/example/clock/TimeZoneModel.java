package com.example.clock;

public class TimeZoneModel {
    private String time;
    private String name;

    public String getTimeZoneId() {
        return timeZoneId;
    }

    public void setTimeZoneId(String timeZoneId) {
        this.timeZoneId = timeZoneId;
    }

    private String timeZoneId;
    private int id;

    public TimeZoneModel(String timeZoneId,String name, String time) {
        this.time = time;
        this.name = name;
        this.timeZoneId=timeZoneId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getTime() {
        return time;
    }
    public String getName() {
        return name;
    }
}
