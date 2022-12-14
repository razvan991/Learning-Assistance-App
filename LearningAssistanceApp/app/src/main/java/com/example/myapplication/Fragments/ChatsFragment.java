package com.example.myapplication.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Adapter.AdapterUser;
import com.example.myapplication.Models.Chatlist;
import com.example.myapplication.Models.User;
import com.example.myapplication.Notifications.Token;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {
    private RecyclerView recyclerView;
    private AdapterUser adapterUser;
    private List<User> allUsers;
    FirebaseUser fuser;
    DatabaseReference reference;
    private List<Chatlist> usersList;

    public ChatsFragment() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats,container,false);
        recyclerView=view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fuser= FirebaseAuth.getInstance().getCurrentUser();
        usersList=new ArrayList<>();

        reference= FirebaseDatabase.getInstance().getReference("Chatlist").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chatlist chatlist = snapshot.getValue(Chatlist.class);
                    usersList.add(chatlist);
                }
                chatList();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        updateToken(FirebaseInstanceId.getInstance().getToken());
        return view;
    }

    private void updateToken(String token){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1=new Token(token);
        reference.child(fuser.getUid()).setValue(token1);
    }
    private void chatList() {
    allUsers=new ArrayList<>();
    reference=FirebaseDatabase.getInstance().getReference("Users");
    reference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            allUsers.clear();
            for(DataSnapshot snapshot1 : snapshot.getChildren()){
                User user = snapshot1.getValue(User.class);
                for (Chatlist chatlist : usersList){
                    if (user != null && user.getId().equals(chatlist.getId())) {
                        allUsers.add(user);
                    }
                }
            }
            adapterUser = new AdapterUser(getContext(), allUsers, true);
            recyclerView.setAdapter(adapterUser);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
    }

}