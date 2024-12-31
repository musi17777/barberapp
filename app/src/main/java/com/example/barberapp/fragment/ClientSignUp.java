package com.example.barberapp.fragment;

import static java.lang.Boolean.FALSE;

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
 * Use the {@link ClientSignUp#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClientSignUp extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ClientSignUp() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClientSignUp.
     */
    // TODO: Rename and change types and number of parameters
    public static ClientSignUp newInstance(String param1, String param2) {
        ClientSignUp fragment = new ClientSignUp();
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
        View v =  inflater.inflate(R.layout.fragment_client_sign_up, container, false);

        EditText emailEditText = v.findViewById(R.id.clientSignUpEmailEditText);
        EditText phoneEditText = v.findViewById(R.id.clientPhoneEditText);
        EditText passwordEditText = v.findViewById(R.id.clientSignUpPasswordEditText);
        EditText repassEditText = v.findViewById(R.id.clientConfirmPasswordEditText);
        EditText firstNmeEditText = v.findViewById(R.id.clientFirstNameEditText);
        EditText lastNmeEditText = v.findViewById(R.id.clientLastNameEditText);

        Button client_SignUp_Button = v.findViewById(R.id.clientSignUpButton);

        client_SignUp_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = firstNmeEditText.getText().toString();
                String lastName = lastNmeEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String repassword = repassEditText.getText().toString();

                if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || repassword.isEmpty()) {
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
                    mainActivity.Client_SignUp();
                    Navigation.findNavController(v).navigate(R.id.action_clientSignUp_to_clientLogin);

                }


            }
        });

        return v;


    }

}