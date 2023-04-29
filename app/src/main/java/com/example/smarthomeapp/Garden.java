package com.example.smarthomeapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.xw.repo.BubbleSeekBar;

public class Garden extends AppCompatActivity {
    private TimePicker timePicker;
    private Switch bomSwitch;
    private Integer hour, minute;
    private Button btnSave, btnSaveLight;
    private TextView tvHour, tvMinute, tvThreshold, tvLightCurrent;
    private BubbleSeekBar seekBar;
    private ImageView reg_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garden);
        setControl();
        setEvent();

    }

    private void setControl() {
        timePicker = (TimePicker) this.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true); // 24H Mode.

        bomSwitch = findViewById(R.id.switch_bom);
        btnSave = findViewById(R.id.btn_save);
        tvHour = findViewById(R.id.tv_hour);
        tvMinute = findViewById(R.id.tv_minute);
        btnSaveLight = findViewById(R.id.btn_save_light);
        seekBar = findViewById(R.id.seekbar);
        tvThreshold = findViewById(R.id.tv_threshold);
        reg_back = findViewById(R.id.reg_back);
        tvLightCurrent = findViewById(R.id.tv_light_current);
    }

    private void setEvent() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference hourRef = database.getReference("water_hour");
        DatabaseReference minuteRef = database.getReference("water_minute");
        DatabaseReference lightThresholdRef = database.getReference("light_threshold");
        DatabaseReference lightCurrentRef = database.getReference("light_current");
        DatabaseReference pumpStatusRef = database.getReference("pump_status");





        reg_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        hourRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                hour = dataSnapshot.getValue(Integer.class);
                Log.d(TAG, "Hour is: " + hour);
                timePicker.setHour(hour);
                tvHour.setText(hour.toString());

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        minuteRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                minute = dataSnapshot.getValue(Integer.class);
                timePicker.setMinute(minute);
                tvMinute.setText(minute.toString());
                Log.d(TAG, "Minute is: " + minute);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        pumpStatusRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Integer value = dataSnapshot.getValue(Integer.class);
                Log.d(TAG, "Value is: " + value);
                assert value != null;

                if(value == 1) {
                    bomSwitch.setChecked(true);
                }
                else if(value == 0) {
                    bomSwitch.setChecked(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        bomSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    pumpStatusRef.setValue(1);

                }
                else {
                    pumpStatusRef.setValue(0);
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Integer valHour = timePicker.getHour();
                Integer valMinute = timePicker.getMinute();

                if(valHour < 0 || valHour > 24) {
                    Log.e(TAG, "Loi");
                }
                else if(valMinute > 60 || valMinute < 0) {
                    Log.e(TAG, "Loi");
                }
                else {
                    hourRef.setValue(valHour);
                    minuteRef.setValue(valMinute);
                    CustomToast.toastCenterTransparent(Garden.this,"Hẹn giờ thành công!", CustomToast.LENGTH_LONG, CustomToast.SUCCESS).show();

                }
            }
        });

        lightThresholdRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Integer value = dataSnapshot.getValue(Integer.class);
                Log.d(TAG, "Value is: " + value);
                assert value != null;
                tvThreshold.setText(value.toString());
                seekBar.setProgress(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        lightCurrentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Integer value = dataSnapshot.getValue(Integer.class);
                Log.d(TAG, "Value is: " + value);
                assert value != null;

                tvLightCurrent.setText(value.toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        seekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                super.onProgressChanged(bubbleSeekBar, progress, progressFloat, fromUser);
            }
        });


        btnSaveLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int value = seekBar.getProgress();
                if(value < 0 || value > 1000) {
                    CustomToast.toastCenterTransparent(Garden.this,"Ngưỡng không hợp lệ!", CustomToast.LENGTH_LONG, CustomToast.ERROR).show();
                }
                else {
                    lightThresholdRef.setValue(value);
                    CustomToast.toastCenterTransparent(Garden.this,"Lưu ngưỡng thành công!", CustomToast.LENGTH_LONG, CustomToast.SUCCESS).show();
                }
            }
        });



    }
}