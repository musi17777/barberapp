package com.example.barberapp.fragment;

import static java.lang.Boolean.FALSE;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.barberapp.R;
import com.example.barberapp.activitys.MainActivity;
import com.example.barberapp.models.clientUsers;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ClientSignUp extends Fragment {
    private FirebaseAuth mAuth;
    private EditText emailEditText;
    private  EditText phoneEditText;
    private  EditText passwordEditText;
    private EditText repassEditText;
    private EditText firstNmeEditText;
    private  EditText lastNmeEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();

        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_client_sign_up, container, false);

        this.emailEditText = v.findViewById(R.id.clientSignUpEmailEditText);
        this.phoneEditText = v.findViewById(R.id.clientPhoneEditText);
        this.passwordEditText = v.findViewById(R.id.clientSignUpPasswordEditText);
        this.repassEditText = v.findViewById(R.id.clientConfirmPasswordEditText);
        this.firstNmeEditText = v.findViewById(R.id.clientFirstNameEditText);
        this.lastNmeEditText = v.findViewById(R.id.clientLastNameEditText);


        Button client_SignUp_Button = v.findViewById(R.id.clientSignUpButton);

        client_SignUp_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Client_SignUp(v);

            }
        });

        return v;


    }
    public void Client_SignUp(View v){
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String firstName = firstNmeEditText.getText().toString();
        String lastName = lastNmeEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String repassword = repassEditText.getText().toString();

        Log.d("result",email + " " +password);
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

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(requireContext(), "Sign Up Successful.", Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(v).navigate(R.id.action_clientSignUp_to_clientLogin);
                            addDataClient(firstName,lastName,email,password,phone);

                        }
                        else
                        {
                            Toast.makeText(requireContext(), "Sign Up Failed.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
    public void addDataClient(String firstName,String lastName,String email,String password,String phone)
    {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("client users").child(phone);
        clientUsers uc = new clientUsers(firstName,lastName,email,password,phone);
        myRef.setValue(uc);

    }

}