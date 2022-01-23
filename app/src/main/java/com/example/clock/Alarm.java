package com.example.clock;

public class Alarm {
    int id;
    int hour;
    int min;
    int ringDuration;
    int snooze;
    String repeat;
    String sound;
    Long timeInMillis;
    String midday;
    String vibration;

    public String getMidday() {
        return midday;
    }

    public void setMidday(String midday) {
        this.midday= midday;
    }

    public Alarm() {

    }
    public Alarm(int hour, int min, long timeInMillis ,String repeat, String sound,int ringDuration,int snooze,String vibration,String midday) {
        this.hour = hour;
        this.min = min;
        this.timeInMillis=timeInMillis;
        this.repeat = repeat;
        this.sound = sound;
        this.ringDuration=ringDuration;
        this.snooze=snooze;
        this.vibration=vibration;
        this.midday=midday;
    }

    public String getVibration() {
        return vibration;
    }

    public void setVibration(String vibration) {
        this.vibration = vibration;
    }

    public int getSnooze() {
        return snooze;
    }

    public void setSnooze(int snooze) {
        this.snooze = snooze;
    }

    public int getRingDuration() {
        return ringDuration;
    }

    public void setRingDuration(int ringDuration) {
        this.ringDuration = ringDuration;
    }

    public Long getTimeInMillis() {
        return timeInMillis;
    }

    public void setTimeInMillis(Long timeInMillis) {
        this.timeInMillis = timeInMillis;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }
}
