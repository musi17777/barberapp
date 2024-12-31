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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OpeningFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OpeningFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OpeningFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OpeningFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OpeningFragment newInstance(String param1, String param2) {
        OpeningFragment fragment = new OpeningFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

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