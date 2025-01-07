package com.example.barberapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.barberapp.R;
import com.example.barberapp.activitys.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class BarberLogin extends Fragment {
    private FirebaseAuth mAuth;
    private EditText emailEditText;
    private EditText passwordEditText;


    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_barber_login, container, false);
        this.emailEditText = v.findViewById(R.id.barberLoginEmailEditText);
        this.passwordEditText = v.findViewById(R.id.barberPasswordEditText);



        Button barber_login_button = v.findViewById(R.id.barberLoginButton);
        barber_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Barber_Login(v);

            }
        });

        return v;
    }
    public void Barber_Login(View v){
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Email or password cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!isValidEmail(email)){
            Toast.makeText(getContext(), "Please enter a valid email address.", Toast.LENGTH_SHORT).show();
        }

        if (password.length() < 6) {
            Toast.makeText(requireContext(), "Password must be at least 6 characters.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(requireContext(), "Login Successful.", Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(v).navigate(R.id.action_barberLogin2_to_barberSchedule);
                        }
                        else
                        {
                            Toast.makeText(requireContext(), "Login Failed.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }


}