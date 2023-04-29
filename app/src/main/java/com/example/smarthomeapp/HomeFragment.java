package com.example.smarthomeapp;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.smarthomeapp.databinding.FragmentHomeBinding;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater,container,false);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference humidityRef = database.getReference("humidity");
        DatabaseReference temperatureRef = database.getReference("temperature");

        binding.livingroom.setOnClickListener(view -> {
            Intent intent = new Intent(requireContext(), LivingRoom.class);
            startActivity(intent);
        });

        binding.garden.setOnClickListener(view -> {
            Intent intent = new Intent(requireContext(), Garden.class);
            startActivity(intent);
        });

        binding.doorLock.setOnClickListener(view -> {
            Intent intent = new Intent(requireContext(), DoorLock.class);
            startActivity(intent);
        });

        binding.kitchenroom.setOnClickListener(view -> {
            Intent intent = new Intent(requireContext(), KitchenRom.class);
            startActivity(intent);
        });

        binding.studyroom.setOnClickListener(view -> {
            Intent intent = new Intent(requireContext(), StudyRoom.class);
            startActivity(intent);
        });

        humidityRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Float value = dataSnapshot.getValue(Float.class);
                Log.d(TAG, "Value is: " + value);
                assert value != null;
                binding.doam.setText(value.toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        temperatureRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Float value = dataSnapshot.getValue(Float.class);
                Log.d(TAG, "Value is: " + value);
                assert value != null;
                binding.nhietdo.setText(value.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });



        return binding.getRoot();
    }




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}