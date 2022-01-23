package com.example.clock;
import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class StopWatchFragment extends Fragment {
    ImageView resetBtn, lapBtn;
    TextView txtTimer;
    long startTime=0,timeInMilliSeconds=0,timeSwapBuff=0,updateTime= 0;
    Handler handler=new Handler();
    StopWatchLapsDb db;
    ImageView toggleBtn;
    int count=1;
    ListView lapsList;
    List<String> laps;

    Runnable updateTimerThread =new Runnable() {
        int secs, mins, milliSeconds;

        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        @Override
        public void run() {
            timeInMilliSeconds = SystemClock.uptimeMillis() - startTime;
            updateTime = timeSwapBuff + timeInMilliSeconds;
            secs = (int) (updateTime / 1000);
            mins = secs / 60;
            secs %= 60;
            milliSeconds = (int) (updateTime % 1000) / 10;
            txtTimer.setText(String.format(Locale.getDefault(),"%02d:%02d:%02d",mins,secs,milliSeconds));

            handler.postDelayed(this, 0);
        }
    };

    @Nullable
    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stop_watch_fragment, container, false);

        toggleBtn = view.findViewById(R.id.toggle_btn);
        resetBtn = view.findViewById(R.id.reset_btn);
        lapBtn = view.findViewById(R.id.lap_btn);
        txtTimer = view.findViewById(R.id.txt_timer);
        lapsList = view.findViewById(R.id.lap_list);
        laps=new ArrayList<>();
        db=new StopWatchLapsDb(getActivity());
        lapBtn.setEnabled(false);
        resetBtn.setEnabled(false);
        DrawableCompat.setTint(DrawableCompat.wrap(lapBtn.getDrawable()), ContextCompat.getColor(requireActivity(),R.color.gray));
        DrawableCompat.setTint(DrawableCompat.wrap(resetBtn.getDrawable()), ContextCompat.getColor(requireActivity(),R.color.gray));

        toggleBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                count++;
                if(count%2==0){
                    lapBtn.setEnabled(true);
                    resetBtn.setEnabled(true);
                    DrawableCompat.setTint(DrawableCompat.wrap(lapBtn.getDrawable()), ContextCompat.getColor(requireActivity(),R.color.darkGray));
                    DrawableCompat.setTint(DrawableCompat.wrap(resetBtn.getDrawable()), ContextCompat.getColor(requireActivity(),R.color.darkGray));
                    toggleBtn.setImageResource(R.drawable.pause_timer);
                    startTime = SystemClock.uptimeMillis();
                    handler.postDelayed(updateTimerThread, 0);
                }
                else{
                    toggleBtn.setImageResource(R.drawable.start);
                    timeSwapBuff+=timeInMilliSeconds;
                    handler.removeCallbacks(updateTimerThread);
                }
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                startTime = SystemClock.uptimeMillis();
                handler.postDelayed(updateTimerThread, 0);
                toggleBtn.setImageResource(R.drawable.start);
                timeInMilliSeconds=0;
                timeSwapBuff=0;
                handler.removeCallbacks(updateTimerThread);
                txtTimer.setText("00:00:00");
                if (count%2==0)
                    count++;
                lapBtn.setEnabled(false);
                resetBtn.setEnabled(false);
                DrawableCompat.setTint(DrawableCompat.wrap(lapBtn.getDrawable()), ContextCompat.getColor(requireActivity(),R.color.gray));
                DrawableCompat.setTint(DrawableCompat.wrap(resetBtn.getDrawable()), ContextCompat.getColor(requireActivity(),R.color.gray));

                db.deleteAll();
                LapsListAdapter adapter=new LapsListAdapter(getActivity());
                lapsList.setAdapter(adapter);
            }
        });

        lapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.insert(txtTimer.getText().toString());
                LapsListAdapter adapter=new LapsListAdapter(getActivity());
                lapsList.setAdapter(adapter);
            }
        });
        return view;
    }
}
