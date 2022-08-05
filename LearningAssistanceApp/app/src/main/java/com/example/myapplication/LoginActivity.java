package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    EditText email,password;
    TextView forgotPass;
    Button btnLogin;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        forgotPass=findViewById(R.id.forgotPassword);
        email=findViewById(R.id.inputEmailLogin);
        password=findViewById(R.id.inputPasswordLogin);
        fAuth=FirebaseAuth.getInstance();
        btnLogin=findViewById(R.id.btnConfirmLogin);

        forgotPass.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this,ResetPasswordActivity.class)));
        btnLogin.setOnClickListener(v -> {
            //Get the information from app and verify
            String Email=email.getText().toString().trim();
            String Password=password.getText().toString().trim();
            if(TextUtils.isEmpty(Email)){
                email.setError("Email is required !");
                return;
            }
            if(TextUtils.isEmpty(Password)){
                password.setError("Password is required !");
                return;
            }
            if(Password.length() < 6){
                password.setError("Password must have at least 6 characters");
                return;
            }
            //authenticate the user
            fAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this,"Logged in successfully !",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }else{
                    Toast.makeText(LoginActivity.this,"Error ! " + Objects.requireNonNull(task.getException()).getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        });

        TextView btn=findViewById(R.id.textViewSignUp);
        btn.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this,RegisterActivity.class)));
    }
}