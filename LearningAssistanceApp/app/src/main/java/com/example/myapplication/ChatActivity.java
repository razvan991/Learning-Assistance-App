package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.myapplication.Fragments.ChatsFragment;
import com.example.myapplication.Fragments.ProfileFragment;
import com.example.myapplication.Fragments.UsersFragment;
import com.example.myapplication.Models.Chat;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    ImageView burgerMenu;
    LinearLayout homeButton,chatButton,courseButton,logoutButton,quizButton;
    ImageView logoButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Hooks();

        homeButton.setOnClickListener(v -> MainActivity.redirectActivity(ChatActivity.this,MainActivity.class));
        chatButton.setOnClickListener(v -> recreate());
        quizButton.setOnClickListener(v -> MainActivity.redirectActivity(ChatActivity.this, Quizpage.class));
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(ChatActivity.this,RegisterActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        });
        courseButton.setOnClickListener(v -> MainActivity.redirectActivity(ChatActivity.this,Cursuri.class));

        logoButton.setOnClickListener(v -> MainActivity.closeDrawer(drawerLayout));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        burgerMenu.setOnClickListener(v -> MainActivity.openDrawer(drawerLayout));
        TabLayout tabLayout=findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
                int unread = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat != null && chat.getReceiver().equals(firebaseUser.getUid()) && !chat.isIsseen()) {
                        unread++;
                    }
                }
                if (unread == 0){
                    viewPagerAdapter.addFragment(new ChatsFragment(), "Chats");
                } else {
                    viewPagerAdapter.addFragment(new ChatsFragment(), "("+unread+") Chats");
                }
                viewPagerAdapter.addFragment(new UsersFragment(), "Users");
                viewPagerAdapter.addFragment(new ProfileFragment(), "Profile");

                viewPager.setAdapter(viewPagerAdapter);

                tabLayout.setupWithViewPager(viewPager);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void Hooks(){
        drawerLayout=findViewById(R.id.drawer_layout);
        burgerMenu=findViewById(R.id.clickBurgerMenu);
        drawerLayout=findViewById(R.id.drawer_layout);
        logoButton=findViewById(R.id.logoClick);
        logoutButton=findViewById(R.id.logoutButton);
        homeButton=findViewById(R.id.homeButton);
        chatButton=findViewById(R.id.chatButton);
        quizButton=findViewById(R.id.quizButton);
        courseButton=findViewById(R.id.courseButton);
        logoutButton=findViewById(R.id.logoutButton);
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
    @Override
    public void onPause(){
        super.onPause();
        status("offline");
        MainActivity.closeDrawer(drawerLayout);
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        final private ArrayList<Fragment> fragments;
        final private ArrayList<String> titles;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragments=new ArrayList<>();
            this.titles=new ArrayList<>();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
        public void addFragment(Fragment fragment, String title){
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}