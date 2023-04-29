package com.example.smarthomeapp;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.example.smarthomeapp.databinding.ActivityLivingRoomBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudyRoom extends AppCompatActivity {
    private Switch fanSwitch, curtainSwitch, autoSwitch;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_room);
        setControl();
        setEvent();
    }

    private void setControl() {
        fanSwitch = findViewById(R.id.switch_fan);
        curtainSwitch = findViewById(R.id.switch_curtain);
        autoSwitch = findViewById(R.id.switch_auto);
        imageView = findViewById(R.id.reg_back);

    }

    private void setEvent() {

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference fanRef = database.getReference("fan");
        DatabaseReference curtainRef = database.getReference("curtain");
        DatabaseReference irSensorStatusRef = database.getReference("ir_sensor_status");

        // Read from the database
        fanRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Integer value = dataSnapshot.getValue(Integer.class);
                Log.d(TAG, "Value is: " + value);
                assert value != null;
                if(value == 1) {
                    fanSwitch.setChecked(true);
                }
                else if(value == 0) {
                    fanSwitch.setChecked(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        fanSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    fanRef.setValue(1);

                }
                else {
                    fanRef.setValue(0);
                }
            }
        });

        // Read from the database
        curtainRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Integer value = dataSnapshot.getValue(Integer.class);
                Log.d(TAG, "Value is: " + value);
                assert value != null;
                if(value == 1) {
                    curtainSwitch.setChecked(true);
                }
                else if(value == 0) {
                    curtainSwitch.setChecked(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        curtainSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    curtainRef.setValue(1);

                }
                else {
                    curtainRef.setValue(0);
                }
            }
        });


        // Read from the database
        irSensorStatusRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Integer value = dataSnapshot.getValue(Integer.class);
                Log.d(TAG, "Value is: " + value);
                assert value != null;
                if(value == 1) {
                    autoSwitch.setChecked(true);
                }
                else if(value == 0) {
                    autoSwitch.setChecked(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        autoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    irSensorStatusRef.setValue(1);

                }
                else {
                    irSensorStatusRef.setValue(0);
                }
            }
        });
    }
}