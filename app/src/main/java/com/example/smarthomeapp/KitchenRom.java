package com.example.smarthomeapp;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.xw.repo.BubbleSeekBar;

public class KitchenRom extends AppCompatActivity {
    private Button btnSave;
    private TextView tvGasVal, tvThreshold;
    private BubbleSeekBar seekBar;
    private Switch gasWarningSwitch;
    private ImageView imageView;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen_rom);
        setControl();
        setEvent();
    }

    private void setControl() {
        btnSave = findViewById(R.id.btn_save);
        tvGasVal = findViewById(R.id.tv_gas_val);
        tvThreshold = findViewById(R.id.tv_threshold);
        seekBar = findViewById(R.id.seekbar);
        imageView = findViewById(R.id.reg_back);
    }

    private void setEvent() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference gasThresholdRef = database.getReference("gas_threshold");
        DatabaseReference gasCurrentRef = database.getReference("gas_current");


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        gasThresholdRef.addValueEventListener(new ValueEventListener() {
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

        gasCurrentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Integer value = dataSnapshot.getValue(Integer.class);
                Log.d(TAG, "Value is: " + value);
                assert value != null;

               tvGasVal.setText(value.toString());
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
                System.out.println(progress + "");
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int value = seekBar.getProgress();
                if(value < 0 || value > 1000) {
                    CustomToast.toastCenterTransparent(context, "Ngưỡng không hợp lệ!", CustomToast.LENGTH_LONG, CustomToast.ERROR).show();

                }
                else {
                    gasThresholdRef.setValue(value);
                    CustomToast.toastCenterTransparent(context, "Lưu ngưỡng cảnh báo thành công!", CustomToast.LENGTH_LONG, CustomToast.SUCCESS).show();
                }
            }
        });




    }
}