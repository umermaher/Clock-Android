package com.example.clock;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class EditTimeZoneActivity extends AppCompatActivity {
    ListView timeZoneListView;
    TextView toolbarTitle;
    ImageView saveBtn,cancelBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_time_zone);
        timeZoneListView=findViewById(R.id.edit_timezone_list);
        toolbarTitle=findViewById(R.id.toolbar_title);
        saveBtn=findViewById(R.id.save);
        cancelBtn=findViewById(R.id.cancel);

        toolbarTitle.setText("Edit cities");
        EditTimeZoneListAdapter adapter=new EditTimeZoneListAdapter(this);
        timeZoneListView.setAdapter(adapter);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimeZoneDb db=new TimeZoneDb(EditTimeZoneActivity.this);
                for(int i=0;i<adapter.ids.size();i++){
                    db.delete(adapter.ids.get(i));
                    onBackPressed();
                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


}