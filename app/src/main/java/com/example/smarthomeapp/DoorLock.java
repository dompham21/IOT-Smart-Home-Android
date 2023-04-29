package com.example.smarthomeapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DoorLock extends AppCompatActivity {
    private EditText edtPass;
    private Button btnUnlock;
    private TextView tvStatusDoor;
    private Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_door_lock);
        setControl();
        setEvent();

    }

    private void setControl() {
        btnUnlock = findViewById(R.id.btn_unlock);
        edtPass = findViewById(R.id.edt_pass);
        tvStatusDoor = findViewById(R.id.tv_status_door);


    }

    private void setEvent() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference doorRef = database.getReference("doorLCD");

        doorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer value = dataSnapshot.getValue(Integer.class);
                Log.d(TAG, "Value is: " + value);
                assert value != null;
                if (value == 1) {
                    tvStatusDoor.setText("mở");
                }
                else if(value == 0) {
                    tvStatusDoor.setText("đóng");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        btnUnlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String edtPassVal = edtPass.getText().toString().trim();

                if(edtPassVal.length() != 6)  {
                    CustomToast.toastCenterTransparent(context,"Hãy nhập đầy đủ thông tin!", CustomToast.LENGTH_LONG, CustomToast.ERROR).show();
                }
                else {
                    String password = edtPassVal;
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference doorPasswordRef = database.getReference("password");

                    doorPasswordRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            String value = dataSnapshot.getValue(String.class);
                            Log.d(TAG, "Value is: " + value);
                            assert value != null;
                            if(value.equals(password)) {
                                doorRef.setValue(1);
                                CustomToast.toastCenterTransparent(context,"Mở cửa thành công!", CustomToast.LENGTH_LONG, CustomToast.SUCCESS).show();

                            }
                            else {
                                doorRef.setValue(0);
                                CustomToast.toastCenterTransparent(context,"Mật khẩu sai!", CustomToast.LENGTH_LONG, CustomToast.ERROR).show();
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
        });
    }
}