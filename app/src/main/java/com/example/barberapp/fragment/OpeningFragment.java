package com.example.barberapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.barberapp.R;
import com.google.firebase.auth.FirebaseAuth;


public class OpeningFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_opening, container, false);

        Button login_button = v.findViewById(R.id.loginButton);

        Button sighupbutton = v.findViewById(R.id.signUpButton);

        Spinner userTypeSpinner = v.findViewById(R.id.userTypeSpinner);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String selectedUserType = userTypeSpinner.getSelectedItem().toString();
                if (selectedUserType.equals("Barber")) {

                    Navigation.findNavController(v).navigate(R.id.action_openingFragment2_to_barberLogin2);
                } else if (selectedUserType.equals("Client")) {

                    Navigation.findNavController(v).navigate(R.id.action_openingFragment2_to_clientLogin);
                } else {

                    Toast.makeText(getContext(), "Please select a valid user type", Toast.LENGTH_SHORT).show();
                }



            }
        });
        sighupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedUserType = userTypeSpinner.getSelectedItem().toString();
                if (selectedUserType.equals("Barber")) {

                    Navigation.findNavController(v).navigate(R.id.action_openingFragment2_to_barberSignUp);
                } else if (selectedUserType.equals("Client")) {

                    Navigation.findNavController(v).navigate(R.id.action_openingFragment2_to_clientSignUp);
                } else {

                    Toast.makeText(getContext(), "Please select a valid user type", Toast.LENGTH_SHORT).show();
                }

            }
        });


        return v;
    }
}