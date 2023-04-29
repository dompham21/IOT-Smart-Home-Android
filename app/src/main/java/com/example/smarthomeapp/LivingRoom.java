package com.example.smarthomeapp;

import static android.content.ContentValues.TAG;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smarthomeapp.databinding.ActivityLivingRoomBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class LivingRoom extends AppCompatActivity {
    private ActivityLivingRoomBinding binding;
    private TextView tvFlame;
    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLivingRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setControl();
        setEvent();


    }

    private void setControl() {
        tvFlame = findViewById(R.id.tv_flame_val);
        imageView = findViewById(R.id.reg_back);

    }

    private void setEvent() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference flameValueRef = database.getReference("flameValue");

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // Read from the database
        flameValueRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Boolean value = dataSnapshot.getValue(Boolean.class);
                Log.d(TAG, "Value is: " + value);
                assert value != null;

                if(value) {
                    tvFlame.setText("Phát hiện lửa");
                }
                else {
                    tvFlame.setText("Không phát hiện lửa");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

}