package com.example.clock;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class AlarmDurationDialog extends AppCompatDialogFragment {
    String[] time={"1 minute","5 minute","10 minute","15 minute","20 minute"};
    int[] timeInInt={1,5,10,15,20};
    public static String duration="1 minute";
    public static String snooze="1 minute";
    public static int SELECTED_DURATION_TIME=1;
    public static int SELECTED_SNOOZE_TIME=1;

    String title;
    public static boolean CLICKED=false;
    public AlarmDurationDialog(String title){

        this.title=title;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        builder.setTitle(title)
                .setSingleChoiceItems(time, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(title.equals("Ring duration")) {
                            SELECTED_DURATION_TIME = timeInInt[i];
                            duration=time[i];
                        }
                        else {
                            SELECTED_SNOOZE_TIME = timeInInt[i];
                            snooze=time[i];
                        }
                        dismiss();
//                        Toast.makeText(getActivity(), SELECTED_SNOOZE_TIME, Toast.LENGTH_SHORT).show();
                        ListView featureList=getActivity().findViewById(R.id.feature_list);
                        featureList.setAdapter(new AddAlarmListAdapter(getActivity()));
                    }
                });
        return builder.create();
    }
}
