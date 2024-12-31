package com.example.barberapp.activitys;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.barberapp.R;
import com.example.barberapp.models.barberUsers;
import com.example.barberapp.models.clientUsers;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth = FirebaseAuth.getInstance();
    }
    public void Barber_Login(){
        String email = ((EditText) findViewById(R.id.barberLoginEmailEditText)).getText().toString();
        String password = ((EditText) findViewById(R.id.barberPasswordEditText)).getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(MainActivity.this, "Login Successful.", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Login Failed.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }
    public  void Barber_SignUp(){
        String email = ((EditText) findViewById(R.id.barberSignUpEmailEditText)).getText().toString();
        String password = ((EditText) findViewById(R.id.barberSignUpPasswordEditText)).getText().toString();
        String businessName = ((EditText) findViewById(R.id.barberBusinessNameEditText)).getText().toString();
        String address = ((EditText) findViewById(R.id.barberAddressEditText)).getText().toString();
        String phone = ((EditText) findViewById(R.id.barberPhoneEditText)).getText().toString();
        Log.d("result",email + " " +password);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(MainActivity.this, "Sign Up Successful.", Toast.LENGTH_SHORT).show();
                            addDataBarber(businessName,address,email,password,phone);

                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Sign Up Failed.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });


    }

    public void Client_Login() {
        String email = ((EditText) findViewById(R.id.clientLoginEmailEditText)).getText().toString();
        String password = ((EditText) findViewById(R.id.clientLoginPasswordEditText)).getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(MainActivity.this, "Login Successful.", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Login Failed.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
    public void Client_SignUp(){
        String email = ((EditText) findViewById(R.id.clientSignUpEmailEditText)).getText().toString();
        String password = ((EditText) findViewById(R.id.clientSignUpPasswordEditText)).getText().toString();
        String firstName = ((EditText) findViewById(R.id.clientFirstNameEditText)).getText().toString();
        String lastName = ((EditText) findViewById(R.id.clientLastNameEditText)).getText().toString();
        String phone = ((EditText) findViewById(R.id.clientPhoneEditText)).getText().toString();
        Log.d("result",email + " " +password);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(MainActivity.this, "Sign Up Successful.", Toast.LENGTH_SHORT).show();
                            addDataClient(firstName,lastName,email,password,phone);

                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Sign Up Failed.", Toast.LENGTH_SHORT).show();

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
    public void addDataBarber(String businessName,String address,String email,String password,String phone){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("barber users").child(phone);
        barberUsers ub = new barberUsers(businessName,address,email,password,phone);
        myRef.setValue(ub);
    }


}