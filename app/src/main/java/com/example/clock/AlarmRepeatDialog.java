package com.example.clock;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.ArrayList;
import java.util.List;

import static com.example.clock.R.drawable.dialog_background;

public class AlarmRepeatDialog extends AppCompatDialogFragment{
    ListView alarmRepititionList;

    public static String[] daysForUI={"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
    static String[] days={"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
    public static List<String> selectedItems;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater= getActivity().getLayoutInflater();

        selectedItems=new ArrayList<>();
         builder.setTitle("Repeat")
                .setMultiChoiceItems(daysForUI, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {

                        if(b){
                            selectedItems.add(days[i]);
                        }
                        else //if(selectedItems.contains(days[i]))
                            selectedItems.remove(days[i]);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        builder.create().dismiss();
                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                AddAlarmListAdapter.SELECTED_DAYS_NUM=selectedItems.size();
                                ListView featureList=getActivity().findViewById(R.id.feature_list);
                                featureList.setAdapter(new AddAlarmListAdapter(getActivity()));
                            }
                });
        return builder.create();
    }
}
