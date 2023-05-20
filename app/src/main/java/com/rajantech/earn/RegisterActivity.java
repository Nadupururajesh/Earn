package com.rajantech.earn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Random;

public class RegisterActivity extends AppCompatActivity {
    EditText name, email, password, confirm;
    Button register;
    TextView gotologin;


    FirebaseAuth auth;

    DatabaseReference database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initilize();
        gotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });

        // click Button Responce
        register.setOnClickListener(v -> {
            String e_name = name.getText().toString();
            String e_email = email.getText().toString();
            String e_password = password.getText().toString();
            String e_confirm = confirm.getText().toString();


            if (e_name.isEmpty()) {
                name.setError("Please Enter Your Name");
            }
            if (e_email.isEmpty()) {
                email.setError("Please Enter Your Email");
            }
            if (e_password.isEmpty()) {
                password.setError("Please Enter Your Password");
            }
            if (e_confirm.isEmpty() || !e_password.equals(e_confirm)) {
                confirm.setError("Please Enter Your Correct Password");
            } else {
                createAccount(e_email, e_password);
            }
        });
    }

    private void createAccount(final String email, final String password) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isComplete()) {
                final FirebaseUser user = auth.getCurrentUser();
                assert user != null;
                // Send Mail Confirmatin
                auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(task1 -> {
                    if (task1.isComplete()) {
                        updateUi(user, email, password);

                    } else {
                        Toast.makeText(RegisterActivity.this, "Error" + task1.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


                Toast.makeText(RegisterActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RegisterActivity.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUi(final FirebaseUser user, final String email, final String password) {
        Random random = new Random();
        int refferalCode = random.nextInt(100000);
        HashMap<String, Object> map = new HashMap<>();
        map.put("Name", this.name.getText().toString());
        map.put("Email", email);
        map.put("UID", user.getUid());
        map.put("Coins", 0);
        map.put("Password", password);
        map.put("RefferalCode", "k" + refferalCode + "m");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.child(user.getUid()).setValue(map).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                Toast.makeText(RegisterActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initilize() {
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirm = findViewById(R.id.confirm);
        register = findViewById(R.id.register_button);
        gotologin = findViewById(R.id.gotologin);


        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference().child("Users");

    }
}