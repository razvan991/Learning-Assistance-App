package com.example.myapplication.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.MessageActivity;
import com.example.myapplication.Models.Chat;
import com.example.myapplication.R;
import com.example.myapplication.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterUser extends RecyclerView.Adapter<AdapterUser.MyHolder> {
    Context context;
    FirebaseAuth firebaseAuth;
    String uid;
    final private boolean ischat;
    List<User> list;
    String theLastMessage;

    public AdapterUser(Context context, List<User> list,boolean ischat) {
        this.context = context;
        this.list=list;
        this.ischat=ischat;
        firebaseAuth = FirebaseAuth.getInstance();
        uid=firebaseAuth.getUid();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_users,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        final String hisuid = list.get(position).getId();
        final User user = list.get(position);
        String userImage=list.get(position).getImageURL();
        String username=list.get(position).getUsername();
        holder.username.setText(username);
        try{
            if(userImage.equals("default")){
                holder.profilepic.setImageResource(R.mipmap.ic_launcher);
            }else {
                Glide.with(context).load(userImage).into(holder.profilepic);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        if (ischat){
            lastMessage(user.getId(), holder.last_msg);
        } else {
            holder.last_msg.setVisibility(View.GONE);
        }
        if (ischat){
            if (user.getStatus().equals("online")){
                holder.img_on.setVisibility(View.VISIBLE);
                holder.img_off.setVisibility(View.GONE);
            } else {
                holder.img_on.setVisibility(View.GONE);
                holder.img_off.setVisibility(View.VISIBLE);
            }
        } else {
            holder.img_on.setVisibility(View.GONE);
            holder.img_off.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MessageActivity.class);
            intent.putExtra("userid",hisuid);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void lastMessage(final String userid, final TextView last_msg){
        theLastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (firebaseUser != null && chat != null) {
                        if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                                chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid())) {
                            theLastMessage = chat.getMessage();
                        }
                    }
                }
                if ("default".equals(theLastMessage)) {
                    last_msg.setText("No Message");
                } else {
                    last_msg.setText(theLastMessage);
                }

                theLastMessage = "default";
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    class MyHolder extends RecyclerView.ViewHolder{
        final private ImageView img_on;
        final private ImageView img_off;
        final private TextView last_msg;
        CircleImageView profilepic;
        TextView username;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            profilepic=itemView.findViewById(R.id.imagep);
            username=itemView.findViewById(R.id.namep);
            img_on = itemView.findViewById(R.id.img_on);
            img_off = itemView.findViewById(R.id.img_off);
            last_msg = itemView.findViewById(R.id.last_msg);
        }
    }
}
