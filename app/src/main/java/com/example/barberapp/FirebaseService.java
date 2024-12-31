package com.example.barberapp;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.barberapp.models.barberUsers;
import com.example.barberapp.models.clientUsers;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseService {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    public FirebaseService() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    public void signUpUser(String email, String password, OnCompleteListener<AuthResult> listener) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(listener);
    }

    public void loginUser(String email, String password, OnCompleteListener<AuthResult> listener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(listener);
    }

    public void addClientToDatabase(String firstName, String lastName, String email, String password, String phone) {
        DatabaseReference myRef = database.getReference("client users").child(phone);
        clientUsers client = new clientUsers(firstName, lastName, email, password, phone);
        myRef.setValue(client);
    }

    public void addBarberToDatabase(String businessName, String address, String email, String password, String phone) {
        DatabaseReference myRef = database.getReference("barber users").child(phone);
        barberUsers barber = new barberUsers(businessName, address, email, password, phone);
        myRef.setValue(barber);
    }
}
