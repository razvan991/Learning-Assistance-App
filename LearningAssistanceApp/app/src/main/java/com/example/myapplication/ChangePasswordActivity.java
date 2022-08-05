package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class ChangePasswordActivity extends AppCompatActivity {
    EditText oldPassword,newPassword,repeatNewPassword;
    Button changePassword;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        oldPassword=findViewById(R.id.oldPassword);
        newPassword=findViewById(R.id.newPassword);
        changePassword=findViewById(R.id.btn_changePassword);
        repeatNewPassword=findViewById(R.id.newPasswordRepeat);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseAuth=FirebaseAuth.getInstance();
        changePassword.setOnClickListener(v -> {
            String oldpassword=oldPassword.getText().toString();
            String newpassword=newPassword.getText().toString();
            String repeatnewPassword=repeatNewPassword.getText().toString();

            if(TextUtils.isEmpty(newpassword)){
                newPassword.setError("Este necesar sa introduceti parola noua !");
                return;
            }
            if(!newpassword.equals(repeatnewPassword)){
                repeatNewPassword.setError("Parola noua nu este identica cu parola din campul Repeta parola noua");
            }

            AuthCredential credential = null;
            if (user != null) {
                credential = EmailAuthProvider.getCredential(Objects.requireNonNull(user.getEmail()),oldpassword);
            }
            if (user != null) {
                user.reauthenticate(credential).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        user.updatePassword(newpassword).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Toast.makeText(ChangePasswordActivity.this,"Ai schimbat parola cu succes !",Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(ChangePasswordActivity.this, ChatActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(ChangePasswordActivity.this,"Eroare la schimbarea parolei !",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        Toast.makeText(ChangePasswordActivity.this,"Eroare la reautentificare !",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
