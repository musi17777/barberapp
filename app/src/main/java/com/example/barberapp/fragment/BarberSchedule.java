package com.example.barberapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.barberapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BarberSchedule extends Fragment {

    private CalendarView calendarView;
    private Button blockDayButton;
    private Button unblockTimeButton;
    private ListView futureAppointmentsListView;

    private FirebaseDatabase database;
    private DatabaseReference barberRef;

    private String selectedDate;
    private String barberBusinessName;

    private List<String> futureAppointments = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_barber_schedule, container, false);

        // Initialize Firebase
        database = FirebaseDatabase.getInstance();
        barberRef = database.getReference("barber users");

        // Initialize UI elements
        calendarView = view.findViewById(R.id.calendarView);
        blockDayButton = view.findViewById(R.id.blockDayButton);
        unblockTimeButton = view.findViewById(R.id.unblockTimeButton);
        futureAppointmentsListView = view.findViewById(R.id.futureAppointmentsListView);

        // Fetch barber's business name dynamically
        fetchBarberBusinessName();

        // CalendarView listener
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
        });

        // Block Day button
        blockDayButton.setOnClickListener(v -> {
            if (selectedDate == null) {
                Toast.makeText(getContext(), "Please select a date to block.", Toast.LENGTH_SHORT).show();
            } else {
                blockDay(selectedDate);
            }
        });

        // Unblock Time button
        unblockTimeButton.setOnClickListener(v -> {
            if (selectedDate == null) {
                Toast.makeText(getContext(), "Please select a date to unblock.", Toast.LENGTH_SHORT).show();
            } else {
                unblockDay(selectedDate);
            }
        });

        return view;
    }

    private void fetchBarberBusinessName() {
        barberRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot barberSnapshot : snapshot.getChildren()) {
                    String fetchedBusinessName = barberSnapshot.getKey();
                    if (fetchedBusinessName != null) {
                        barberBusinessName = fetchedBusinessName;
                        fetchFutureAppointments(); // Fetch appointments after business name is fetched
                        break;
                    }
                }

                if (barberBusinessName == null) {
                    Toast.makeText(getContext(), "Failed to fetch barber business name.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to fetch barber business name.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchFutureAppointments() {
        if (barberBusinessName == null || barberBusinessName.isEmpty()) {
            Toast.makeText(getContext(), "Barber business name is not available. Cannot fetch appointments.", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference appointmentsRef = barberRef.child(barberBusinessName).child("appointments");
        appointmentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                futureAppointments.clear();
                for (DataSnapshot dateSnapshot : snapshot.getChildren()) {
                    String date = dateSnapshot.getKey();
                    for (DataSnapshot timeSnapshot : dateSnapshot.getChildren()) {
                        String time = timeSnapshot.getKey();
                        String clientDetails = timeSnapshot.getValue(String.class);
                        if (date != null && time != null && clientDetails != null) {
                            futureAppointments.add(date + " | " + time + " | " + clientDetails);
                        }
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, futureAppointments);
                futureAppointmentsListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to fetch appointments.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void blockDay(String date) {
        DatabaseReference blockedDaysRef = barberRef.child(barberBusinessName).child("blockedDays").child(date);
        blockedDaysRef.setValue(true).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Day blocked successfully: " + date, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Failed to block the day.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void unblockDay(String date) {
        DatabaseReference blockedDaysRef = barberRef.child(barberBusinessName).child("blockedDays").child(date);
        blockedDaysRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Day unblocked successfully: " + date, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Failed to unblock the day.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
