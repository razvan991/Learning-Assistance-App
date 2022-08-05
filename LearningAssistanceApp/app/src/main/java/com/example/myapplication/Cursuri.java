package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Cursuri extends AppCompatActivity {
    DrawerLayout drawerLayout;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    TextView clickOOP,clickBD;
    ImageView burgerMenu;
    LinearLayout homeButton,chatButton,courseButton,logoutButton,quizButton;
    ImageView logoButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursuri);
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        burgerMenu=findViewById(R.id.clickBurgerMenu);
        clickBD=findViewById(R.id.clickCursBD);
        clickOOP=findViewById(R.id.clickCursOOP);

        Hooks();

        homeButton.setOnClickListener(v -> MainActivity.redirectActivity(Cursuri.this,MainActivity.class));
        chatButton.setOnClickListener(v -> MainActivity.redirectActivity(Cursuri.this, ChatActivity.class));
        quizButton.setOnClickListener(v -> MainActivity.redirectActivity(Cursuri.this,Quizpage.class));
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(Cursuri.this,RegisterActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        });
        courseButton.setOnClickListener(v -> recreate());
        logoButton.setOnClickListener(v -> MainActivity.closeDrawer(drawerLayout));

        burgerMenu.setOnClickListener(v -> MainActivity.openDrawer(drawerLayout));
        clickOOP.setOnClickListener(v -> MainActivity.redirectActivity(Cursuri.this,CursOOP.class));
        clickBD.setOnClickListener(v -> MainActivity.redirectActivity(Cursuri.this,CursBD.class));
        drawerLayout=findViewById(R.id.drawer_layout);
    }
    public void Hooks(){
        drawerLayout=findViewById(R.id.drawer_layout);
        logoButton=findViewById(R.id.logoClick);
        logoutButton=findViewById(R.id.logoutButton);
        homeButton=findViewById(R.id.homeButton);
        chatButton=findViewById(R.id.chatButton);
        quizButton=findViewById(R.id.quizButton);
        courseButton=findViewById(R.id.courseButton);
        logoutButton=findViewById(R.id.logoutButton);
    }
    @Override
    public void onPause(){
        super.onPause();
        status("offline");
        MainActivity.closeDrawer(drawerLayout);
    }
    private void status(String status){
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);
    }
    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }
}