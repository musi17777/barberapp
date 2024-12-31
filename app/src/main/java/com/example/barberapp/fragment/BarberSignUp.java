package com.example.barberapp.fragment;

import android.os.Bundle;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BarberSignUp#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BarberSignUp extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BarberSignUp() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BarberSignUp.
     */
    // TODO: Rename and change types and number of parameters
    public static BarberSignUp newInstance(String param1, String param2) {
        BarberSignUp fragment = new BarberSignUp();
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
        View v =  inflater.inflate(R.layout.fragment_barber_sign_up, container, false);

        EditText businessNameEditText = v.findViewById(R.id.barberBusinessNameEditText);
        EditText addressEditText = v.findViewById(R.id.barberAddressEditText);
        EditText emailEditText = v.findViewById(R.id.barberSignUpEmailEditText);
        EditText passwordEditText = v.findViewById(R.id.barberSignUpPasswordEditText);
        EditText repassEditText = v.findViewById(R.id.barberConfirmPasswordEditText);
        EditText phoneEditText = v.findViewById(R.id.barberPhoneEditText);

        Button barber_signUp_Button = v.findViewById(R.id.barberSignUpButton);
        barber_signUp_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String repassword = repassEditText.getText().toString();
                String businessName = businessNameEditText.getText().toString();
                String address = addressEditText.getText().toString();
                if (businessName.isEmpty() || address.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || repassword.isEmpty()) {
                    Toast.makeText(getContext(), "All fields are required.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(getContext(), "Invalid email address.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!phone.matches("^[0-9]{10}$")) {
                    Toast.makeText(getContext(), "Invalid phone number. Must be 10 digits.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getContext(), "Password must be at least 6 characters.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.matches(".*[A-Z].*")) {
                    Toast.makeText(getContext(), "Password must contain at least one uppercase letter.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.matches(".*[a-z].*")) {
                    Toast.makeText(getContext(), "Password must contain at least one lowercase letter.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.matches(".*\\d.*")) {
                    Toast.makeText(getContext(), "Password must contain at least one digit.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(repassword)) {
                    Toast.makeText(getContext(), "Passwords do not match.", Toast.LENGTH_SHORT).show();
                    return;
                }

                MainActivity mainActivity = (MainActivity) getActivity();
                if (mainActivity != null) {
                    mainActivity.Barber_SignUp();
                    Navigation.findNavController(v).navigate(R.id.action_barberSignUp_to_barberLogin2);
                }

            }
        });



        return v;
    }
}