package com.example.barberapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.barberapp.FirebaseService;
import com.example.barberapp.R;
import com.example.barberapp.activitys.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BarberLogin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BarberLogin extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BarberLogin() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BarberLogin.
     */
    // TODO: Rename and change types and number of parameters
    public static BarberLogin newInstance(String param1, String param2) {
        BarberLogin fragment = new BarberLogin();
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
        View v =  inflater.inflate(R.layout.fragment_barber_login, container, false);
        EditText emailEditText = v.findViewById(R.id.barberLoginEmailEditText);
        EditText passwordEditText = v.findViewById(R.id.barberPasswordEditText);

        Button barber_login_button = v.findViewById(R.id.barberLoginButton);
        barber_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if(email.isEmpty() ||password.isEmpty())
                {
                    Toast.makeText(getContext(), "Email or password cannot be empty.", Toast.LENGTH_SHORT).show();
                }
                /*else if(!isValidEmail(email))
                {
                    Toast.makeText(getContext(), "Please enter a valid email address.", Toast.LENGTH_SHORT).show();
                }*/
                else if(password.length() < 6)
                {
                    Toast.makeText(getContext(), "Password must be at least 6 characters..", Toast.LENGTH_SHORT).show();
                }
                else {
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.Barber_Login();


                }
            }
        });


        return v;
    }
}