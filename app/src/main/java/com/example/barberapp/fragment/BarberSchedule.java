package com.example.barberapp.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barberapp.FirebaseService;
import com.example.barberapp.R;
import com.example.barberapp.AppointmentsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BarberSchedule extends Fragment {

    private FirebaseService firebaseService;
    private String selectedDate;

    private FirebaseAuth auth;
    private CalendarView calendarView;
    private Button blockDayButton, unblockTimeButton;
    private RecyclerView futureAppointmentsRecyclerView;
    private List<String> futureAppointments = new ArrayList<>();
    private AppointmentsAdapter appointmentsAdapter;
    private Set<String> blockedDays = new HashSet<>(); // רשימת ימים חסומים

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_barber_schedule, container, false);

        firebaseService = new FirebaseService();
        auth = FirebaseAuth.getInstance(); // אתחול Firebase Authentication
        calendarView = view.findViewById(R.id.calendarView);
        blockDayButton = view.findViewById(R.id.blockDayButton);
        unblockTimeButton = view.findViewById(R.id.unblockTimeButton);
        futureAppointmentsRecyclerView = view.findViewById(R.id.futureAppointmentsRecyclerView);

        // הגדרת RecyclerView
        futureAppointmentsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        appointmentsAdapter = new AppointmentsAdapter(futureAppointments);
        futureAppointmentsRecyclerView.setAdapter(appointmentsAdapter);

        setupCalendar();
        setupBlockDayButton();
        setupUnblockTimeButton();
        loadFutureAppointments();

        return view;
    }

    private void setupCalendar() {
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // שמירת התאריך שנבחר
            selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
            Toast.makeText(getContext(), "Selected date: " + selectedDate, Toast.LENGTH_SHORT).show();
        });
    }


    private void setupBlockDayButton() {
        blockDayButton.setOnClickListener(v -> {
            if (selectedDate == null) {
                Toast.makeText(getContext(), "Please select a date first!", Toast.LENGTH_SHORT).show();
            } else {
                // הוספת היום לרשימת הימים החסומים
                blockedDays.add(selectedDate);

                // עדכון Firebase
                String barberId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                firebaseService.blockTime(barberId, selectedDate);

                // הודעת הצלחה
                Toast.makeText(getContext(), "Day blocked: " + selectedDate, Toast.LENGTH_SHORT).show();
            }
        });
    }




    private void setupUnblockTimeButton() {
        unblockTimeButton.setOnClickListener(v -> {
            if (selectedDate == null) {
                Toast.makeText(getContext(), "Please select a date first!", Toast.LENGTH_SHORT).show();
            } else {
                // בדוק אם התאריך נמצא ברשימת החסומים
                if (blockedDays.contains(selectedDate)) {
                    // הסרת התאריך מ-Firebase
                    String barberId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    firebaseService.unblockTime(barberId, selectedDate);

                    // הסרת התאריך מרשימת החסומים
                    blockedDays.remove(selectedDate);

                    // הודעת הצלחה
                    Toast.makeText(getContext(), "Block removed for: " + selectedDate, Toast.LENGTH_SHORT).show();
                } else {
                    // אם התאריך לא חסום
                    Toast.makeText(getContext(), "Date is not blocked: " + selectedDate, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void loadFutureAppointments() {
        String barberId = auth.getCurrentUser().getUid(); // קבלת מזהה הספר המחובר

        firebaseService.getBarberAppointments(barberId, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                futureAppointments.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    String appointment = child.getKey();
                    if (appointment != null) {
                        futureAppointments.add(appointment);
                    }
                }
                appointmentsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error loading appointments", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
