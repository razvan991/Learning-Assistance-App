package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ResetPasswordActivity extends AppCompatActivity {
    EditText send_email;
    Button btn_reset;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        send_email=findViewById(R.id.send_email);
        btn_reset=findViewById(R.id.btn_reset);

        firebaseAuth=FirebaseAuth.getInstance();
        btn_reset.setOnClickListener(v -> {
            String email=send_email.getText().toString();
            if(email.equals("")){
                Toast.makeText(ResetPasswordActivity.this,"Completati cu adresa de email !",Toast.LENGTH_SHORT).show();
            }else{
             firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                 if(task.isSuccessful()) {
                     Toast.makeText(ResetPasswordActivity.this, "Please check your email", Toast.LENGTH_SHORT).show();
                     startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
                 }else{
                     String error= Objects.requireNonNull(task.getException()).getMessage();
                     Toast.makeText(ResetPasswordActivity.this,error,Toast.LENGTH_SHORT).show();
                 }
             });
            }
        });
    }
}
