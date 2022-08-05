package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    EditText username,email,password,repeatpass;
    Button btnRegister;
    FirebaseAuth fAuth;
    EditText numarMatricol;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username=findViewById(R.id.inputUsername);
        email=findViewById(R.id.inputEmail);
        numarMatricol=findViewById(R.id.inputNumarMatricol);
        password=findViewById(R.id.inputPassword);
        repeatpass=findViewById(R.id.inputRepeatPassword);
        btnRegister=findViewById(R.id.btnConfirmReg);

        fAuth=FirebaseAuth.getInstance();
        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        btnRegister.setOnClickListener(v -> {
            String Username=username.getText().toString().trim();
            String Email=email.getText().toString().trim();
            String Password=password.getText().toString().trim();
            String NumarMatricol=numarMatricol.getText().toString().trim();
            if(!NumarMatricol.matches("(\\d){9}([A-w]{3})+(\\d){6}")){
                numarMatricol.setError("Numar matricol format invalid !");
                return;
            }
            if(TextUtils.isEmpty(NumarMatricol)){
                numarMatricol.setError("Numar matricol is required !");
                return;
            }
            if(TextUtils.isEmpty(Username)){
                username.setError("Username is required !");
                return;
            }
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
            if(NumarMatricol.length()!=18){
                numarMatricol.setError("Numar matricol must have at least 18 characters");
                return;
            }
            fAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(task -> {
                FirebaseUser firebaseUser= fAuth.getCurrentUser();
                String userid=firebaseUser.getUid();
                databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(userid);

                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put("id",userid);
                hashMap.put("username",Username);
                hashMap.put("email",Email);
                hashMap.put("status", "offline");
                hashMap.put("imageURL","default");
                hashMap.put("numarMatricol",NumarMatricol);
                databaseReference.setValue(hashMap).addOnCompleteListener(task1 -> {
                    if(task1.isSuccessful()){
                        Toast.makeText(RegisterActivity.this,"User created !",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                    }else{
                        Toast.makeText(RegisterActivity.this,"Error ! " + Objects.requireNonNull(task1.getException()).getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });
        TextView btn=findViewById(R.id.alreadyHaveAccount);
        btn.setOnClickListener(v -> startActivity(new Intent(RegisterActivity.this,LoginActivity.class)));
    }

    private void updateUserInfo(final String name, Uri pickedImgUri, final FirebaseUser currentUser) {
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");
        final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(taskSnapshot -> {
            // image uploaded succesfully
            // now we can get our image url
            imageFilePath.getDownloadUrl().addOnSuccessListener(uri -> {
                // uri contain user image url
                UserProfileChangeRequest profleUpdate = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .setPhotoUri(uri)
                        .build();
                currentUser.updateProfile(profleUpdate)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this,"User created with photo!",Toast.LENGTH_SHORT).show();
                                updateUI();
                            }
                        });
            });
        });
    }
    private void updateUI() {
        Intent homeActivity = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(homeActivity);
        finish();
    }

}