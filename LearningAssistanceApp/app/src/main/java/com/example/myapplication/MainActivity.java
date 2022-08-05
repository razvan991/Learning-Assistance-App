package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    FirebaseUser firebaseUser;
    LinearLayout homeButton,chatButton,courseButton,logoutButton,quizButton;
    DatabaseReference reference;
    ImageView logoButton;
    ImageView burgerMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        findItemsByID();

        homeButton.setOnClickListener(v -> recreate());
        chatButton.setOnClickListener(v -> redirectActivity(MainActivity.this, ChatActivity.class));
        quizButton.setOnClickListener(v -> redirectActivity(MainActivity.this,Quizpage.class));
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this,RegisterActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        });
        courseButton.setOnClickListener(v -> redirectActivity(MainActivity.this,Cursuri.class));
        logoButton.setOnClickListener(v -> closeDrawer(drawerLayout));

        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        burgerMenu=findViewById(R.id.clickBurgerMenu);
        burgerMenu.setOnClickListener(v -> openDrawer(drawerLayout));
    }
    public void findItemsByID(){
        drawerLayout=findViewById(R.id.drawer_layout);
        logoButton=findViewById(R.id.logoClick);
        logoutButton=findViewById(R.id.logoutButton);
        homeButton=findViewById(R.id.homeButton);
        chatButton=findViewById(R.id.chatButton);
        quizButton=findViewById(R.id.quizButton);
        courseButton=findViewById(R.id.courseButton);
        logoutButton=findViewById(R.id.logoutButton);
    }
    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public static void closeDrawer(DrawerLayout drawerLayout) {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    public static void redirectActivity(Activity activity,Class aClass) {
        Intent intent=new Intent(activity,aClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
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
    protected void onPause(){
        super.onPause();
        status("offline");
    }

}