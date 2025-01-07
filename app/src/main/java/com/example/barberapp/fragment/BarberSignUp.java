package com.example.barberapp.fragment;

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
import com.example.barberapp.models.barberUsers;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class BarberSignUp extends Fragment {
    private FirebaseAuth mAuth;
    private EditText businessNameEditText;
    private EditText addressEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText repassEditText;
    private EditText phoneEditText;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_barber_sign_up, container, false);

        this.businessNameEditText = v.findViewById(R.id.barberBusinessNameEditText);
        this.addressEditText = v.findViewById(R.id.barberAddressEditText);
        this.emailEditText = v.findViewById(R.id.barberSignUpEmailEditText);
        this.passwordEditText = v.findViewById(R.id.barberSignUpPasswordEditText);
        this.repassEditText = v.findViewById(R.id.barberConfirmPasswordEditText);
        this.phoneEditText = v.findViewById(R.id.barberPhoneEditText);



        Button barber_signUp_Button = v.findViewById(R.id.barberSignUpButton);
        barber_signUp_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Barber_SignUp(v);

            }
        });



        return v;
    }

    public  void Barber_SignUp(View v){
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String businessName = businessNameEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String repassword = repassEditText.getText().toString();
        Log.d("result",email + " " +password);

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


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(requireContext(), "Sign Up Successful.", Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(v).navigate(R.id.action_barberSignUp_to_barberLogin2);
                            addDataBarber(businessName,address,email,password,phone);

                        }
                        else
                        {
                            Toast.makeText(requireContext(), "Sign Up Failed.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });


    }
    public void addDataBarber(String businessName,String address,String email,String password,String phone){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("barber users").child(businessName);
        barberUsers ub = new barberUsers(businessName,address,email,password,phone);
        myRef.setValue(ub);
    }
}