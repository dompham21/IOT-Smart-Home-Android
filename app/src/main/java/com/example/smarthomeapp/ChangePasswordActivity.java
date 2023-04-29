package com.example.smarthomeapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangePasswordActivity extends AppCompatActivity {
    private TextInputEditText edtNewPassword, edtConfirmPassword;
    private MaterialButton btnUpdatePassword;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        setControl();
        setEvent();
    }

    private void setControl() {
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnUpdatePassword = findViewById(R.id.btnUpdatePassword);
    }


    private void setEvent() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference passwordRef = database.getReference("password");



        btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassword = edtNewPassword.getText().toString().trim();
                String confirmPassword = edtConfirmPassword.getText().toString().trim();



                if(newPassword.length() != 6 || confirmPassword.length() != 6) {
                    CustomToast.toastCenterTransparent(context,"Hãy nhập đầy đủ thông tin!", CustomToast.LENGTH_LONG, CustomToast.ERROR).show();
                }

                else if(!newPassword.equals(confirmPassword)) {
                    CustomToast.toastCenterTransparent(context,"Xác nhận mật khẩu không khớp!", CustomToast.LENGTH_LONG, CustomToast.ERROR).show();
                }
                else {
                    passwordRef.setValue(newPassword);
                    CustomToast.toastCenterTransparent(context,"Đổi mật khẩu thành công!", CustomToast.LENGTH_LONG, CustomToast.SUCCESS).show();

                }
            }
        });
    }

}