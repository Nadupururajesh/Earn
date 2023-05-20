package com.rajantech.earn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText email,password;
    Button login;
    TextView gotoSignup;

    FirebaseAuth auth;
    FirebaseUser user;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login_button);
        gotoSignup= findViewById(R.id.gotosignup);

        auth= FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        gotoSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String l_email=email.getText().toString();
                String l_password = password.getText().toString();

                if (l_email.isEmpty())
                {
                    email.setError("Please Enter Email");
                } else if (l_password.isEmpty())
                {
                    password.setError("Please Enter Password");
                }
                else
                {
                    signin(l_email,l_password);
                }
            }
        });
    }

    private void signin(final String email,final String password)
    {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    FirebaseUser user1 = auth.getCurrentUser();
                    if(user.isEmailVerified())
                    {
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        Toast.makeText(LoginActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Email is Not Verified", Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Error : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}