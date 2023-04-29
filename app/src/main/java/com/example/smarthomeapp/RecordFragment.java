package com.example.smarthomeapp;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.smarthomeapp.databinding.FragmentHomeBinding;
import com.example.smarthomeapp.databinding.FragmentRecordBinding;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class RecordFragment extends Fragment {

    private FragmentRecordBinding binding;
    protected static final int RESULT_SPEECH = 1;
    private TextToSpeech textToSpeech;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRecordBinding.inflate(inflater,container,false);


        textToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.US);
                }
            }
        });

        binding.btnSpeak.setOnClickListener(view1 -> {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "vi-VN");
            try {

                startActivityForResult(intent, RESULT_SPEECH);
                binding.tvText.setText("");
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getContext(), "Thiết bị của bạn không hỗ trợ voice! ", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });


       


        return binding.getRoot();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode){
            case RESULT_SPEECH:
                if(resultCode == RESULT_OK && data != null){
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference fanRef = database.getReference("fan");
                    DatabaseReference curtainRef = database.getReference("curtain");
                    DatabaseReference pumpStatusRef = database.getReference("pump_status");
                    DatabaseReference irSensorStatusRef = database.getReference("ir_sensor_status");
                    DatabaseReference temperatureRef = database.getReference("temperature");
                    DatabaseReference gasRef = database.getReference("gas_current");
                    DatabaseReference humidityRef = database.getReference("humidity");

                    String dataVoice = new String(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0)).toLowerCase(Locale.ROOT);

                    if(dataVoice.contentEquals("bật quạt")){
                        fanRef.setValue(1);
                        textToSpeech.speak("Has turned on fan", TextToSpeech.QUEUE_FLUSH, null);
                        CustomToast.toastCenterTransparent(requireContext(), "Đã bật quạt", CustomToast.LENGTH_LONG, CustomToast.SUCCESS).show();
                    }
                    else if(dataVoice.contentEquals("mở cửa sổ")) {
                        curtainRef.setValue(1);
                        textToSpeech.speak("Opened the curtain", TextToSpeech.QUEUE_FLUSH, null);
                        CustomToast.toastCenterTransparent(requireContext(), "Đã mở cửa sổ", CustomToast.LENGTH_LONG, CustomToast.SUCCESS).show();
                    }
                    else if(dataVoice.contentEquals("bật máy bơm")) {
                        pumpStatusRef.setValue(1);
                        textToSpeech.speak("Has turned on pump", TextToSpeech.QUEUE_FLUSH, null);
                        CustomToast.toastCenterTransparent(requireContext(), "Đã bật máy bơm", CustomToast.LENGTH_LONG, CustomToast.SUCCESS).show();
                    }
                    else if(dataVoice.contentEquals("bật tự động")) {
                        irSensorStatusRef.setValue(1);
                        textToSpeech.speak("Has turned on automatic", TextToSpeech.QUEUE_FLUSH, null);
                        CustomToast.toastCenterTransparent(requireContext(), "Đã bật tự động", CustomToast.LENGTH_LONG, CustomToast.SUCCESS).show();
                    }
                    else if(dataVoice.contentEquals("tắt máy quạt")) {
                        fanRef.setValue(0);
                        textToSpeech.speak("Has turned off fan", TextToSpeech.QUEUE_FLUSH, null);
                        CustomToast.toastCenterTransparent(requireContext(), "Đã tắt quạt", CustomToast.LENGTH_LONG, CustomToast.SUCCESS).show();
                    }
                    else if(dataVoice.contentEquals("đóng cửa sổ")) {
                        curtainRef.setValue(0);
                        textToSpeech.speak("Closed the curtain", TextToSpeech.QUEUE_FLUSH, null);
                        CustomToast.toastCenterTransparent(requireContext(), "Đã đóng cửa sổ", CustomToast.LENGTH_LONG, CustomToast.SUCCESS).show();
                    }
                    else if(dataVoice.contentEquals("tắt máy bơm")) {
                        pumpStatusRef.setValue(0);
                        textToSpeech.speak("Has turned off pump", TextToSpeech.QUEUE_FLUSH, null);
                        CustomToast.toastCenterTransparent(requireContext(), "Đã tắt máy bơm", CustomToast.LENGTH_LONG, CustomToast.SUCCESS).show();
                    }
                    else if(dataVoice.contentEquals("tắt tự động")) {
                        irSensorStatusRef.setValue(0);
                        textToSpeech.speak("Has turned off automatic", TextToSpeech.QUEUE_FLUSH, null);
                        CustomToast.toastCenterTransparent(requireContext(), "Đã tắt tự động", CustomToast.LENGTH_LONG, CustomToast.SUCCESS).show();
                    }
                    else if(dataVoice.contentEquals("nhiệt độ hiện tại")) {
                        temperatureRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Float value = snapshot.getValue(Float.class);
                                Log.d(TAG, "Value is: " + value);
                                assert value != null;
                                textToSpeech.speak("Current temperature is " + value.toString() + " degrees Celsius", TextToSpeech.QUEUE_FLUSH, null);
                                CustomToast.toastCenterTransparent(requireContext(), "Nhiệt độ hiện tại là " +  value.toString() +" độ C!", CustomToast.LENGTH_LONG, CustomToast.SUCCESS).show();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                    else if(dataVoice.contentEquals("độ ẩm hiện tại")) {
                        humidityRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Float value = snapshot.getValue(Float.class);
                                Log.d(TAG, "Value is: " + value);
                                assert value != null;
                                textToSpeech.speak("Current humidity is " + value.toString() + " %", TextToSpeech.QUEUE_FLUSH, null);
                                CustomToast.toastCenterTransparent(requireContext(), "Độ ẩm hiện tại là " +  value.toString() +" %!", CustomToast.LENGTH_LONG, CustomToast.SUCCESS).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    else if(dataVoice.contentEquals("khí gas hiện tại")) {
                        gasRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Integer value = snapshot.getValue(Integer.class);
                                Log.d(TAG, "Value is: " + value);
                                assert value != null;
                                textToSpeech.speak("The current gas index is " + value.toString(), TextToSpeech.QUEUE_FLUSH, null);
                                CustomToast.toastCenterTransparent(requireContext(), "chỉ số khí gas hiện tại là " +  value.toString() +"!", CustomToast.LENGTH_LONG, CustomToast.SUCCESS).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    else
                    {
                        textToSpeech.speak("Device not found", TextToSpeech.QUEUE_FLUSH, null);
                        Toast.makeText(requireContext(), "Không tìm thấy thiết bị!!!", Toast.LENGTH_LONG).show();
                    }


                }
                break;
        }
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

}