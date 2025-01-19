package com.example.barberapp.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Spinner;
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
import java.util.HashMap;
import java.util.List;

public class ClientAppointment extends Fragment {

    private Spinner barberSpinner;
    private CalendarView calendarView;
    private ListView timeSlotListView;
    private ListView futureAppointmentsListView;
    private Button bookAppointmentButton;
    private Button cancelAppointmentButton;

    private FirebaseDatabase database;
    private DatabaseReference barberRef, clientRef;
    private boolean isTimeSlotSelected = false;

    private String selectedBarber;
    private String selectedDate;
    private String selectedTime;
    private String clientPhone;
    private String clientFirstName;
    private String clientLastName;

    private List<String> availableTimeSlots = new ArrayList<>();
    private List<String> futureAppointments = new ArrayList<>();
    private List<String> barbers = new ArrayList<>();
    private HashMap<String, String> barberDetails = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_appointment, container, false);

        // Initialize Firebase
        database = FirebaseDatabase.getInstance();
        barberRef = database.getReference("barber users");
        clientRef = database.getReference("client users");

        // Initialize UI elements
        barberSpinner = view.findViewById(R.id.barberSpinner);
        calendarView = view.findViewById(R.id.calendarView);
        timeSlotListView = view.findViewById(R.id.timeSlotListView);
        futureAppointmentsListView = view.findViewById(R.id.futureAppointmentsListView);
        bookAppointmentButton = view.findViewById(R.id.bookAppointmentButton);
        cancelAppointmentButton = view.findViewById(R.id.cancelAppointmentButton);

        bookAppointmentButton.setEnabled(false);
        cancelAppointmentButton.setEnabled(false);

        fetchClientPhone();
        fetchBarbers();

        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
            fetchAvailableTimeSlots(selectedBarber, selectedDate);
        });

        // Spinner listener
        barberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedBarber = barbers.get(position);
                if (selectedDate != null) {
                    fetchAvailableTimeSlots(selectedBarber, selectedDate);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedBarber = null;
            }
        });

        // ListView listener for time slots
        timeSlotListView.setOnItemClickListener((parent, view1, position, id) -> {
            selectedTime = availableTimeSlots.get(position);
            isTimeSlotSelected = true;
            Toast.makeText(getContext(), "Time slot selected: " + selectedTime, Toast.LENGTH_SHORT).show();
            bookAppointmentButton.setEnabled(true);
            cancelAppointmentButton.setEnabled(true);
        });

        // Book appointment button
        bookAppointmentButton.setOnClickListener(v -> {
            if (!isTimeSlotSelected) {
                Toast.makeText(getContext(), "Please select a time slot before booking.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectedBarber == null || selectedDate == null || selectedTime == null) {
                Toast.makeText(getContext(), "Please select a barber, date, and time.", Toast.LENGTH_SHORT).show();
            } else {
                bookAppointment(clientPhone, clientFirstName, clientLastName, selectedBarber, selectedDate, selectedTime);
            }
        });

        // Cancel appointment button
        cancelAppointmentButton.setOnClickListener(v -> {
            if (!isTimeSlotSelected) {
                Toast.makeText(getContext(), "Please select a time slot before canceling.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectedBarber == null || selectedDate == null || selectedTime == null) {
                Toast.makeText(getContext(), "Please select a barber, date, and time to cancel.", Toast.LENGTH_SHORT).show();
            } else {
                cancelAppointment(clientPhone, selectedBarber, selectedDate, selectedTime);
            }
        });

        return view;
    }

    private void fetchClientPhone() {
        clientRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot clientSnapshot : snapshot.getChildren()) {
                    String fetchedPhone = clientSnapshot.getKey();
                    String fetchedFirstName = clientSnapshot.child("firstName").getValue(String.class);
                    String fetchedLastName = clientSnapshot.child("lastName").getValue(String.class);
                    if (fetchedPhone != null) {
                        clientPhone = fetchedPhone;
                        clientFirstName = fetchedFirstName;
                        clientLastName = fetchedLastName;
                        break;
                    }
                }

                if (clientPhone == null) {
                    Toast.makeText(getContext(), "No valid client phone found.", Toast.LENGTH_SHORT).show();
                } else {
                    fetchFutureAppointments();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to fetch client phone.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchBarbers() {
        barberRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                barbers.clear();
                barberDetails.clear();
                for (DataSnapshot barberSnapshot : snapshot.getChildren()) {
                    String businessName = barberSnapshot.getKey();
                    String address = barberSnapshot.child("address").getValue(String.class);
                    String phone = barberSnapshot.child("phone").getValue(String.class);

                    if (businessName != null) {
                        barbers.add(businessName);
                        barberDetails.put(businessName, address + " | " + phone + " | " + businessName);
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, barbers);
                barberSpinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to fetch barbers.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchFutureAppointments() {
        if (clientPhone == null || clientPhone.isEmpty()) {
            Toast.makeText(getContext(), "Client phone is not available. Cannot fetch appointments.", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference clientAppointmentsRef = clientRef.child(clientPhone).child("appointments");
        clientAppointmentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                futureAppointments.clear();
                for (DataSnapshot dateSnapshot : snapshot.getChildren()) {
                    String date = dateSnapshot.getKey();
                    for (DataSnapshot timeSnapshot : dateSnapshot.getChildren()) {
                        String time = timeSnapshot.getKey();
                        String details = timeSnapshot.getValue(String.class);
                        if (date != null && time != null && details != null) {
                            futureAppointments.add(date + " | " + time + " | " + details);
                        }
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, futureAppointments);
                futureAppointmentsListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to fetch future appointments.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchAvailableTimeSlots(String barber, String date) {
        DatabaseReference blockedDaysRef = barberRef.child(barber).child("blockedDays").child(date);

        // Check if the selected day is blocked
        blockedDaysRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Day is blocked
                    Toast.makeText(getContext(), "This day is blocked. No appointments available.", Toast.LENGTH_SHORT).show();
                    availableTimeSlots.clear();
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, availableTimeSlots);
                    timeSlotListView.setAdapter(adapter);
                    bookAppointmentButton.setEnabled(false);
                } else {
                    // Day is not blocked, load available time slots
                    loadAvailableTimeSlots(barber, date);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to check blocked days.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAvailableTimeSlots(String barber, String date) {
        DatabaseReference barberAppointmentsRef = barberRef.child(barber).child("appointments").child(date);
        barberAppointmentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                availableTimeSlots.clear();
                for (int hour = 9; hour <= 21; hour++) {
                    String timeSlot1 = hour + ":00";
                    String timeSlot2 = hour + ":30";
                    if (!snapshot.hasChild(timeSlot1)) {
                        availableTimeSlots.add(timeSlot1);
                    }
                    if (!snapshot.hasChild(timeSlot2)) {
                        availableTimeSlots.add(timeSlot2);
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, availableTimeSlots);
                timeSlotListView.setAdapter(adapter);

                // Enable booking if slots are available
                bookAppointmentButton.setEnabled(!availableTimeSlots.isEmpty());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to fetch time slots.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bookAppointment(String clientPhone, String firstName, String lastName, String barber, String date, String time) {
        DatabaseReference barberAppointmentsRef = barberRef.child(barber).child("appointments").child(date).child(time);
        barberAppointmentsRef.setValue(clientPhone + " | " + firstName + " " + lastName);

        DatabaseReference clientAppointmentsRef = clientRef.child(clientPhone).child("appointments").child(date).child(time);
        clientAppointmentsRef.setValue(barberDetails.get(barber));

        Toast.makeText(getContext(), "Appointment booked successfully.", Toast.LENGTH_SHORT).show();
        fetchFutureAppointments();
    }

    private void cancelAppointment(String clientPhone, String barber, String date, String time) {
        DatabaseReference barberAppointmentsRef = barberRef.child(barber).child("appointments").child(date).child(time);
        barberAppointmentsRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DatabaseReference clientAppointmentsRef = clientRef.child(clientPhone).child("appointments").child(date).child(time);
                clientAppointmentsRef.removeValue().addOnCompleteListener(clientTask -> {
                    if (clientTask.isSuccessful()) {
                        Toast.makeText(getContext(), "Appointment canceled successfully.", Toast.LENGTH_SHORT).show();
                        cancelAppointmentButton.setEnabled(false);
                        fetchFutureAppointments();
                    } else {
                        Toast.makeText(getContext(), "Failed to cancel appointment for client.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(getContext(), "Failed to cancel appointment for barber.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
