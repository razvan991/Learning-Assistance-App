package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Adapter.MessageAdapter;
import com.example.myapplication.Fragments.APIService;
import com.example.myapplication.Models.User;
import com.example.myapplication.Models.Chat;
import com.example.myapplication.Notifications.Client;
import com.example.myapplication.Notifications.Data;
import com.example.myapplication.Notifications.MyResponse;
import com.example.myapplication.Notifications.Sender;
import com.example.myapplication.Notifications.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    CircleImageView profile_image;
    ImageButton btn_send;
    TextView username;
    boolean notify=false;
    EditText text_send;
    FirebaseUser fuser;
    DatabaseReference reference;
    MessageAdapter messageAdapter;
    List<Chat> mChat;
    APIService apiService;
    ValueEventListener seenListener;
    String userid;
    RecyclerView recyclerView;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        recyclerView.setLayoutManager(linearLayoutManager);
        profile_image=findViewById(R.id.profile_image);
        username=findViewById(R.id.username);
        btn_send=findViewById(R.id.btn_send);
        text_send=findViewById(R.id.text_send);
        intent = getIntent();
        userid=intent.getStringExtra("userid");
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    username.setText(user.getUsername());
                }
                if (user != null) {
                    if(user.getImageURL().equals("default")){
                        profile_image.setImageResource(R.mipmap.ic_launcher);
                    }else{
                        Glide.with(getApplicationContext()).load(user.getImageURL()).into(profile_image);
                    }
                }
                if (user != null) {
                    readMessage(fuser.getUid(),userid,user.getImageURL());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_send.setOnClickListener(v -> {
            notify=true;
            String msg=text_send.getText().toString();
            if(!msg.equals("")){
                sendMessage(fuser.getUid(),userid,msg);
            }else{
                Toast.makeText(MessageActivity.this,"Nu poti trimite un mesaj gol !",Toast.LENGTH_SHORT).show();
            }
            text_send.setText("");
        });
        seenMessage(userid);
        drawerLayout=findViewById(R.id.drawer_layout);
    }
    private void seenMessage(final String userid){
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat != null && chat.getReceiver().equals(fuser.getUid()) && chat.getSender().equals(userid)) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isseen", true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void sendMessage(String sender,String receiver,String message){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashmap = new HashMap<>();
        hashmap.put("sender",sender);
        hashmap.put("receiver",receiver);
        hashmap.put("message",message);
        hashmap.put("isseen",false);
        reference.child("Chats").push().setValue(hashmap);
        DatabaseReference chatRef=FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(fuser.getUid()).child(userid);
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    chatRef.child("id").setValue(userid);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        final String msg=message;
        reference=FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if(notify) {
                    if (user != null) {
                        sendNotification(receiver, user.getUsername(), msg);
                    }
                } notify=false;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void sendNotification(String receiver,String username,String message){
        DatabaseReference tokens=FirebaseDatabase.getInstance().getReference("Tokens");
        Query query=tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    Token token = snapshot1.getValue(Token.class);
                    Data data = new Data(fuser.getUid(),R.mipmap.ic_launcher,username+": "+message,"New message",userid);
                    Sender sender = null;
                    if (token != null) {
                        sender = new Sender(data,token.getToken());
                    }
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(@NonNull Call<MyResponse> call, @NonNull Response<MyResponse> response) {
                                    if (response.code() == 200){
                                        if (response.body() != null && response.body().success != 1) {
                                            Toast.makeText(MessageActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                                @Override
                                public void onFailure(@NonNull Call<MyResponse> call, @NonNull Throwable t) {

                                }
                            });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Message error", String.valueOf(error));
            }
        });
    }
    private void status(String status){
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);
    }
    private void currentUser(String userid){
        SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
        editor.putString("currentuser", userid);
        editor.apply();
    }
    @Override
    protected void onResume() {
        super.onResume();
        status("online");
        currentUser(userid);
    }
    protected void onPause(){
        super.onPause();
        reference.removeEventListener(seenListener);
        currentUser("none");
        status("offline");
    }
    private void readMessage(String myid,String userid,String imageurl){
        mChat=new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mChat.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    Chat chat = snapshot1.getValue(Chat.class);
                    assert chat != null;
                    if(chat.getReceiver().equals(myid) && chat.getSender().equals(userid)
                    || chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){
                        mChat.add(chat);
                    }
                }
                messageAdapter = new MessageAdapter(MessageActivity.this,mChat,imageurl);
                recyclerView.setAdapter(messageAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
