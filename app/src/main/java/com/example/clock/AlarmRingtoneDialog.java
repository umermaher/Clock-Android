package com.example.clock;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class AlarmRingtoneDialog extends AppCompatDialogFragment {
    public static String[] ringtones={"Clock","Alarm rooster","Snoop Dogg","bugle","Still Dre","Mixed alarm","Alarm beep"};
    public static String selectedSound="Clock";
    List<MediaPlayer> players;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        players=new ArrayList<>();
        players.add(MediaPlayer.create(getActivity(),R.raw.clock_alarm));
        players.add(MediaPlayer.create(getActivity(),R.raw.alarm_rooster_hug));
        players.add(MediaPlayer.create(getActivity(),R.raw.snoop_dogg));
        players.add(MediaPlayer.create(getActivity(),R.raw.bugle_call));
        players.add(MediaPlayer.create(getActivity(),R.raw.still_dre));
        players.add(MediaPlayer.create(getActivity(),R.raw.funny_alarm));
        players.add(MediaPlayer.create(getActivity(),R.raw.beep_alarm));

        builder.setTitle("Sound")
                .setSingleChoiceItems(ringtones, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        stopSound();
                        players.get(i).start();
                        selectedSound=ringtones[i];
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                stopSound();
                            }
                        })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        stopSound();
                        ListView featureList=getActivity().findViewById(R.id.feature_list);
                        featureList.setAdapter(new AddAlarmListAdapter(getActivity()));
                    }
                });

        return builder.create();
    }

    private void stopSound(){
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).isPlaying()) {
                players.get(i).stop();
                players.get(i).prepareAsync();
            }
        }
    }
}
