package com.example.barberapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barberapp.R;

import java.util.List;

public class /**/AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.ViewHolder> {

    private final List<String> appointments;

    // Constructor לקבלת רשימת תורים
    public AppointmentsAdapter(List<String> appointments) {
        this.appointments = appointments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate של הפריט מהרשימה
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_appointment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // קישור הנתונים של התור לאלמנט בתצוגה
        String appointment = appointments.get(position);
        holder.appointmentTextView.setText(appointment);
    }

    @Override
    public int getItemCount() {
        // מספר הפריטים ברשימה
        return appointments.size();
    }

    // מחלקה פנימית לניהול ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView appointmentTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            appointmentTextView = itemView.findViewById(R.id.appointmentTextView);
        }
    }
}
